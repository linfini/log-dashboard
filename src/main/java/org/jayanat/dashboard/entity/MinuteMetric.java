package org.jayanat.dashboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "dashboard_metric_minute")
@Getter
@Setter
public class MinuteMetric {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime metricTime;
    private String metricName;
    private String metricValue;


    public static List<MinuteMetric> expand(JournalMinuteMetrics journalMinuteMetrics) {
        List<MinuteMetric> minuteMetricsList = new ArrayList<>();
        LocalDateTime time = journalMinuteMetrics.getTime();

        for (Map.Entry<String, Long> entry : journalMinuteMetrics.getMetrics().entrySet()) {
            MinuteMetric minuteMetric = new MinuteMetric();
            minuteMetric.setMetricTime(time);
            minuteMetric.setMetricName(entry.getKey());
            minuteMetric.setMetricValue(entry.getValue().toString());
            minuteMetricsList.add(minuteMetric);
        }

        return minuteMetricsList;
    }
}
