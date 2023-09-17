package com.neshan.routingreporter.repository;

import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.model.Report;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r " +
            "WHERE r.type = ?1")
    List<Report> findAllByType(ReportType reportType);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Report r " +
            "WHERE r.type = ?3 " +
            "AND ST_Equals(r.location, ?1) = true " +
            "AND r.expiredAt > CURRENT_TIMESTAMP " +
            "AND r.user.id = ?2")
    boolean existsReportByLocationAndExpiredAt(Point point, Long userId, ReportType type);

    @Query("SELECT r " +
            "FROM Report r " +
            "WHERE r.id IN (" +
            "  SELECT MIN(sub.id) " +
            "  FROM Report sub " +
            "  WHERE ST_Intersects(sub.location, ST_Buffer(ST_GeomFromText(?1, 3857), 10 * 0.0001)) = true " +
            "  AND sub.expiredAt > NOW() " +
            "  AND sub.likeCount > -2 " +
            "  GROUP BY ST_AsText(sub.location), sub.type" +
            ")")
    List<Report> findReportsWithinRouteRadius(String route);

    @Query("SELECT CAST(DATE_PART('hour', r.createdAt) AS INTEGER) AS trafficHour, COUNT(r) AS trafficCount " +
            "FROM Report r " +
            "WHERE r.type = ?2 " +
            "GROUP BY trafficHour " +
            "ORDER BY trafficCount DESC " +
            "LIMIT ?1")
    List<Integer> findTopHoursReport(Integer limit, ReportType type);

}
