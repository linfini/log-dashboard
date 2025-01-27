package org.jayanat.dashboard.entity;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Getter
@Setter
@ToString
public class JournalDayMetrics {
    private LocalDate date;
    private Map<String, Long> countMetrics;

    public JournalDayMetrics() {
        this.countMetrics = new HashMap<>();
    }

    public static JournalDayMetrics empty() {
        JournalDayMetrics dayMetrics = new JournalDayMetrics();
        dayMetrics.date = LocalDate.now();
        dayMetrics.countMetrics = new HashMap<>();
        return dayMetrics;
    }


    public JournalDayMetrics aggregate(String key, JournalLog other) {
        if (date == null) {
            date = Instant.ofEpochMilli(other.eventTimestamp()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        BiFunction<String, Long, Long> countFunc = (k, v) -> {
            if (v == null) {
                v = 0L;
            }
            v += 1;
            return v;
        };

        countMetrics.compute(MetricType.Day.count.name(), countFunc);
        switch (other.getMsgType()) {
            case 1:
                countMetrics.compute(MetricType.Day.msg_type1_count.name(), countFunc);
                break;
            case 2:
                countMetrics.compute(MetricType.Day.msg_type2_count.name(), countFunc);
                break;
            case 3:
                countMetrics.compute(MetricType.Day.msg_type3_count.name(), countFunc);
                break;
            default:
                break;
        }

        String var0 = MetricType.appendTimePeriod(MetricType.TOTAL_TIME, other.totalTime());
        String var1 = MetricType.appendTimePeriod(MetricType.EXTERNAL_TIME, other.externalTime());
        String var2 = MetricType.appendTimePeriod(MetricType.INTERNAL_TIME, other.internalTime());
        countMetrics.compute(var0, countFunc);
        countMetrics.compute(var1, countFunc);
        countMetrics.compute(var2, countFunc);
        return this;
    }
}
