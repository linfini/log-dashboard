package org.jayanat.dashboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "dashboard_metric_day")
@Getter
@Setter
public class DayMetric {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate metricDate;
    private String metricName;
    private String metricValue;


    public static List<DayMetric> expand(JournalDayMetrics journalDayMetrics) {
        List<DayMetric> dayMetricsList = new ArrayList<>();
        LocalDate date = journalDayMetrics.getDate();
        for (Map.Entry<String, Long> entry : journalDayMetrics.getCountMetrics().entrySet()) {
            DayMetric dayMetric = new DayMetric();
            dayMetric.setMetricDate(date);
            dayMetric.setMetricName(entry.getKey());
            dayMetric.setMetricValue(entry.getValue().toString());
            dayMetricsList.add(dayMetric);
        }
        return dayMetricsList;
    }

}
