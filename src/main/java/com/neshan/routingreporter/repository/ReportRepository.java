package com.neshan.routingreporter.repository;

import com.neshan.routingreporter.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
