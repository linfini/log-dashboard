package org.jayanat.dashboard.repository;

import org.jayanat.dashboard.entity.MinuteMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MinuteMetricRepository extends JpaRepository<MinuteMetric, Long> {

    void deleteByMetricTime(LocalDateTime localDateTime);
}
