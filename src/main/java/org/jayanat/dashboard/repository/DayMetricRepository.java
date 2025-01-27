package org.jayanat.dashboard.repository;

import org.jayanat.dashboard.entity.DayMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DayMetricRepository extends JpaRepository<DayMetric, Long> {

    void deleteByMetricDate(LocalDate localDate);
}
