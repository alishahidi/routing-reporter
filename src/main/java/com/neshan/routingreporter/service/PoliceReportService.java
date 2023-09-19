package com.neshan.routingreporter.service;

import com.neshan.routingreporter.config.ReportConfig;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.interfaces.ReportInterface;
import com.neshan.routingreporter.model.PoliceReport;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.request.ReportRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PoliceReportService implements ReportInterface {
    ReportConfig reportConfig;
    ReportService reportService;

    @Override
    public ReportDto create(ReportRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportDto reportDto = request.getReport();
        WKTReader wktReader = new WKTReader(new GeometryFactory());
        Point point = null;
        try {
            point = (Point) wktReader.read(reportDto.getLocation());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        point.setSRID(3857);

        PoliceReport report = PoliceReport.builder()
                .expiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitPoliceTtl()))
                .user(user)
                .type(ReportType.POLICE)
                .isAccept(true)
                .policeType(request.getPoliceType())
                .likeCount(0)
                .location(point)
                .build();

        return reportService.create(report);
    }

    @Override
    public ReportDto like(Long id) {
        return reportService.like(id, reportConfig.getLikePoliceTtl());
    }

    @Override
    public ReportDto disLike(Long id) {
        return reportService.disLike(id, reportConfig.getDisLikePoliceTtl());
    }

    @Override
    public ReportDto accept(Long id) {
        return reportService.accept(id, reportConfig.getInitPoliceTtl());
    }

    @Override
    public List<Integer> findTopHours(Integer limit) {
        return reportService.findTopHours(limit, ReportType.POLICE);
    }
}
