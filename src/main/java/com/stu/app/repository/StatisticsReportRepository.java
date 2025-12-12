package com.stu.app.repository;

import com.stu.app.model.StatisticsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsReportRepository extends JpaRepository<StatisticsReport, Long> {
}