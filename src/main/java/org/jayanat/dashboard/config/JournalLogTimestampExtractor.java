package org.jayanat.dashboard.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.jayanat.dashboard.entity.JournalLog;

public class JournalLogTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {
        if (record.value() instanceof JournalLog journalLog) {
            return journalLog.externalTime();
        } else {
            return record.timestamp();
        }
    }
}
