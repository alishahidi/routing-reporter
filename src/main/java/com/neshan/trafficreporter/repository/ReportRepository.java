package com.neshan.trafficreporter.repository;

import com.neshan.trafficreporter.enums.ReportType;
import com.neshan.trafficreporter.model.Report;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE ST_DWithin(r.location, ?1, 10 * 0.00001) = true AND r.expiredAt > NOW() AND r.isAccept = true AND r.likeCount > -2")
    List<Report> findReportsWithinRouteRadius(LineString route);

    @Query("SELECT r FROM Report r WHERE r.expiredAt > NOW()")
    List<Report> findAllByType(ReportType type);

    @Query("SELECT r.type FROM Report r WHERE r.id = ?1")
    ReportType findTypeById(Long id);

    @Query("SELECT r FROM Report r WHERE r.location = ?1 AND r.expiredAt > CURRENT_TIMESTAMP")
    Report findReportByLocationAndExpiredAt(Point point);
}
