package com.neshan.trafficreporter.repository;

import com.neshan.trafficreporter.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
