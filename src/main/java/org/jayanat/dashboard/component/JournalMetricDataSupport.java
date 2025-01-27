package org.jayanat.dashboard.component;

import org.jayanat.dashboard.entity.DayMetric;
import org.jayanat.dashboard.entity.JournalDayMetrics;
import org.jayanat.dashboard.entity.JournalMinuteMetrics;
import org.jayanat.dashboard.entity.MinuteMetric;
import org.jayanat.dashboard.repository.DayMetricRepository;
import org.jayanat.dashboard.repository.MinuteMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JournalMetricDataSupport {

    private final DayMetricRepository dayMetricRepository;
    private final MinuteMetricRepository minuteMetricRepository;

    @Autowired
    public JournalMetricDataSupport(DayMetricRepository dayMetricRepository, MinuteMetricRepository minuteMetricRepository) {
        this.dayMetricRepository = dayMetricRepository;
        this.minuteMetricRepository = minuteMetricRepository;
    }

    public void saveDayMetrics(JournalDayMetrics journalDayMetrics) {
        List<DayMetric> dayMetrics = DayMetric.expand(journalDayMetrics);
        dayMetricRepository.deleteByMetricDate(journalDayMetrics.getDate());
        dayMetricRepository.saveAll(dayMetrics);
    }

    public void saveMinuteMetrics(JournalMinuteMetrics journalMinuteMetrics) {
        List<MinuteMetric> minuteMetrics = MinuteMetric.expand(journalMinuteMetrics);
        minuteMetricRepository.deleteByMetricTime(journalMinuteMetrics.getTime());
        minuteMetricRepository.saveAll(minuteMetrics);
    }
}
