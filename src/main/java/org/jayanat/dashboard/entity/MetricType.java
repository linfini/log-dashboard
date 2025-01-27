package org.jayanat.dashboard.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ALL")
public class MetricType {
    enum Day {
        count,
        msg_type1_count,
        msg_type2_count,
        msg_type3_count
    }

    enum Minute {
        count,
        fail_count
    }

    static final Logger log = LoggerFactory.getLogger(MetricType.class);
    public static String TOTAL_TIME = "total_time";
    public static String EXTERNAL_TIME = "external_time";
    public static String INTERNAL_TIME = "internal_time";

    static Set<TimePeriod> timePeriodSet = new HashSet<>();

    static {
        timePeriodSet.add(TimePeriod.of("<100ms", Integer.MIN_VALUE, 100));
        timePeriodSet.add(TimePeriod.of("100ms~1s", 100, 1000));
        timePeriodSet.add(TimePeriod.of("1s~2s", 1000, 2000));
        timePeriodSet.add(TimePeriod.of("2s~3s", 2000, 3000));
        timePeriodSet.add(TimePeriod.of("3s~4s", 3000, 4000));
        timePeriodSet.add(TimePeriod.of("4s~5s", 4000, 5000));
        timePeriodSet.add(TimePeriod.of("5s~6s", 5000, 6000));
        timePeriodSet.add(TimePeriod.of("6s~7s", 6000, 7000));
        timePeriodSet.add(TimePeriod.of("7s~8s", 7000, 8000));
        timePeriodSet.add(TimePeriod.of("8s~9s", 8000, 9000));
        timePeriodSet.add(TimePeriod.of("9s~10s", 9000, 10000));
        timePeriodSet.add(TimePeriod.of(">10s", 10000, Integer.MAX_VALUE));
    }

    public static String appendTimePeriod(String prefix, long time) {
        return prefix + " " + timePeriodSuffix(time);
    }

    private static String timePeriodSuffix(long time) {
        for (TimePeriod period : timePeriodSet) {
            if (period.isMatch(time)) {
                return period.name;
            }
        }
        throw new IllegalStateException("time period not found for time:" + time);
    }

    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode
    private static class TimePeriod {
        private String name;
        private long start;
        private long end;

        public boolean isMatch(long var0) {
            return start <= var0 && var0 < end;
        }

    }
}
