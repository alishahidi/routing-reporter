package com.neshan.routingreporter.service;

import com.neshan.routingreporter.config.ReportConfig;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.interfaces.ReportInterface;
import com.neshan.routingreporter.model.AccidentReport;
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
public class WeatherReportService implements ReportInterface {
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

        AccidentReport report = AccidentReport.builder()
                .expiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitWeatherTtl()))
                .user(user)
                .likeCount(0)
                .isAccept(true)
                .accidentType(request.getAccidentType())
                .type(ReportType.WEATHER)
                .location(point)
                .build();

        return reportService.create(report);
    }

    @Override
    public List<ReportDto> getAll() {
        return reportService.getAllByType(ReportType.WEATHER);
    }

    @Override
    public ReportDto like(Long id) {
        return reportService.like(id, reportConfig.getLikeWeatherTtl());
    }

    @Override
    public ReportDto disLike(Long id) {
        return reportService.disLike(id, reportConfig.getDisLikeWeatherTtl());
    }

    @Override
    public ReportDto accept(Long id) {
        return reportService.accept(id, reportConfig.getInitWeatherTtl());
    }

    @Override
    public List<Integer> findTopHours(Integer limit) {
        return reportService.findTopHours(limit, ReportType.WEATHER);
    }
}
