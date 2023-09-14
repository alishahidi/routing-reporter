package com.neshan.trafficreporter.repository;

import com.neshan.trafficreporter.enums.ReportType;
import com.neshan.trafficreporter.model.Report;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE ST_DWithin(r.location, ?1, 10 * 0.00001) = true AND r.expiredAt > NOW() AND r.isAccept = true")
    List<Report> findReportsWithinRouteRadius(LineString route);

    @Query("SELECT r FROM Report r WHERE r.expiredAt > NOW()")
    List<Report> findAllByType(ReportType type);

    ReportType findTypeById(Long id);
}
