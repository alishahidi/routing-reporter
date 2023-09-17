package com.neshan.routingreporter.repository;

import com.neshan.routingreporter.model.TrafficReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficReportRepository extends JpaRepository<TrafficReport, Long> {
}
