package com.neshan.routingreporter.repository;

import com.neshan.routingreporter.model.PoliceReport;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PoliceReportRepository extends JpaRepository<PoliceReport, Long> {
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM PoliceReport r " +
            "WHERE ST_Equals(r.location, ?1) = true " +
            "AND r.expiredAt > CURRENT_TIMESTAMP")
    boolean existsPoliceReportByLocationAndExpiredAt(Point point);

    @Query("SELECT r FROM PoliceReport r " +
            "WHERE ST_DWithin(r.location, ?1, 10 * 0.00001) = true " +
            "AND r.expiredAt > NOW() " +
            "AND r.likeCount > -2")
    List<PoliceReport> findReportsWithinRouteRadius(LineString route);

    @Query("SELECT CAST(DATE_PART('hour', r.createdAt) AS INTEGER) AS trafficHour, COUNT(r) AS trafficCount " +
            "FROM PoliceReport r " +
            "GROUP BY trafficHour " +
            "ORDER BY trafficCount DESC " +
            "LIMIT ?1")
    List<Integer> findTopPoliceHours(Integer limit);
}
