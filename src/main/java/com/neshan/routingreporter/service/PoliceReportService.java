package com.neshan.routingreporter.service;

import com.neshan.routingreporter.config.ReportConfig;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.interfaces.ReportInterface;
import com.neshan.routingreporter.model.PoliceReport;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.PoliceReportRepository;
import com.neshan.routingreporter.request.ReportRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PoliceReportService implements ReportInterface {
    ReportConfig reportConfig;
    PoliceReportRepository policeReportRepository;
    ReportService reportService;

    @Override
    public ReportDto create(ReportRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportDto reportDto = request.getReport();
        Point point = reportDto.getLocation();
        point.setSRID(3857);

        PoliceReport report = PoliceReport.builder()
                .expiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitPoliceTtl()))
                .user(user)
                .type(ReportType.POLICE)
                .isAccept(true)
                .policeType(request.getPoliceType())
                .likeCount(0)
                .location(reportDto.getLocation())
                .build();

        return reportService.create(report);
    }

    @Override
    public List<ReportDto> getAll() {
        return reportService.getAllByType(ReportType.POLICE);
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
    public List<Integer> findTopHours(Integer limit) {
        return reportService.findTopHours(limit, ReportType.POLICE);
    }
}
