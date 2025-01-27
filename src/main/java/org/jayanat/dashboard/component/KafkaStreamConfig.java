package org.jayanat.dashboard.component;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.jayanat.dashboard.entity.JournalDayMetrics;
import org.jayanat.dashboard.entity.JournalLog;
import org.jayanat.dashboard.entity.JournalMinuteMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.jayanat.dashboard.config.MetricConstant.*;

@EnableKafkaStreams
@Configuration
public class KafkaStreamConfig {

    @Value("${app.dashboard.journal-log-topic}")
    private String topic;

    @Autowired
    private JournalMetricDataSupport journalMetricDataSupport;

    private static final Logger log = LoggerFactory.getLogger(KafkaStreamConfig.class);

    static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    @Bean
    public KStream<String, JournalLog> journalLogKStream(StreamsBuilder builder) {
        log.info("Creating Kafka Stream for topic: {}", topic);
        KStream<String, JournalLog> journalLogKStream = builder.stream(topic);

        Materialized<String, JournalMinuteMetrics, WindowStore<Bytes, byte[]>> var0 = Materialized.<String, JournalMinuteMetrics, WindowStore<Bytes, byte[]>>as(STORE_JOURNAL_MINUTE_METRIC).withKeySerde(Serdes.String()).withValueSerde(new JsonSerde<>(JournalMinuteMetrics.class));
        Materialized<String, JournalDayMetrics, WindowStore<Bytes, byte[]>> var1 = Materialized.<String, JournalDayMetrics, WindowStore<Bytes, byte[]>>as(STORE_JOURNAL_DAY_METRIC).withKeySerde(Serdes.String()).withValueSerde(new JsonSerde<>(JournalDayMetrics.class));
        Aggregator<String, JournalLog, JournalMinuteMetrics> minuteAggregator = (key, value, aggregate) -> aggregate.aggregate(key, value);
        Aggregator<String, JournalLog, JournalDayMetrics> dayAggregator = (key, value, aggregate) -> aggregate.aggregate(key, value);

        KTable<Windowed<String>, JournalDayMetrics> dayMetrics = journalLogKStream.groupBy((key, value) -> GROUP_JOURNAL_DAY_METRIC)
                .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofMinutes(1), Duration.ofMinutes(10)))
                .aggregate(JournalDayMetrics::empty, dayAggregator, var1);

        KTable<Windowed<String>, JournalMinuteMetrics> minuteMetrics = journalLogKStream.groupBy((key, value) -> GROUP_JOURNAL_MINUTE_METRIC)
                .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofMinutes(1), Duration.ofMinutes(10)))
                .aggregate(JournalMinuteMetrics::empty, minuteAggregator, var0);

        dayMetrics
                .suppress(Suppressed.untilTimeLimit(Duration.ofSeconds(1), Suppressed.BufferConfig.unbounded()))
                .toStream()
                .foreach((windowKey, metrics) -> {
                    Window window = windowKey.window();
                    log.info("Day Metrics:  {} ~ {}", df.format(window.startTime()), df.format(window.endTime()));
                    log.info("Day Metrics saving:   {}", metrics);
                    journalMetricDataSupport.saveDayMetrics(metrics);
                });

        minuteMetrics
                .suppress(Suppressed.untilTimeLimit(Duration.ofSeconds(1), Suppressed.BufferConfig.unbounded()))
                .toStream()
                .foreach((windowKey, metrics) -> {
                    Window window = windowKey.window();
                    log.info("Minute Metrics:  {} ~ {}", df.format(window.startTime()), df.format(window.endTime()));
                    log.info("Minute Metrics saving:   {}", metrics);
                    journalMetricDataSupport.saveMinuteMetrics(metrics);
                });

        return journalLogKStream;
    }

}
