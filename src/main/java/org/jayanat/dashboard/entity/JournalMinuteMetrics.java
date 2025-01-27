package org.jayanat.dashboard.entity;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class JournalMinuteMetrics {
    private static Logger log = LoggerFactory.getLogger(JournalMinuteMetrics.class);
    static Map<String, Long> metricMap = new HashMap<>();

    static {
        metricMap.put(MetricType.Minute.count.name(), 0L);
        metricMap.put(MetricType.Minute.fail_count.name(), 0L);
    }

    private LocalDateTime time;
    private Map<String, Long> metrics;

    public JournalMinuteMetrics() {
        this.metrics = metricMap;
    }

    public static JournalMinuteMetrics empty() {
        JournalMinuteMetrics minuteMetrics = new JournalMinuteMetrics();
        minuteMetrics.time = LocalDateTime.now();
        minuteMetrics.metrics = new HashMap<>(metricMap);
        return minuteMetrics;
    }


    public JournalMinuteMetrics aggregate(String key, JournalLog other) {
        if (time == null) {
            time = LocalDateTime.ofInstant(Instant.ofEpochMilli(other.eventTimestamp()), ZoneId.systemDefault());
        }
        metrics.compute(MetricType.Minute.count.name(), (k, v) -> v += 1);
        if (!other.isSuccess()) {
            metrics.compute(MetricType.Minute.fail_count.name(), (k, v) -> v += 1);
        }
        return this;
    }
}
