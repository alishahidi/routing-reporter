package com.neshan.trafficreporter.service;

import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import com.neshan.trafficreporter.mapper.ReportMapper;
import com.neshan.trafficreporter.model.Report;
import com.neshan.trafficreporter.model.User;
import com.neshan.trafficreporter.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrafficReportService implements ReportInterface {
    ReportRepository reportRepository;

    @Override
    public ReportDto createReport(ReportDto reportDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Point point = reportDto.getLocation();
        point.setSRID(3857);
        return ReportMapper.INSTANCE.reportToReportDto(reportRepository.save(
                Report.builder()
                        .expiredAt(LocalDateTime.now().plusMinutes(2))
                        .type(reportDto.getType())
                        .isAccept(false)
                        .user(user)
                        .likeCount(0)
                        .location(reportDto.getLocation())
                        .build()
        ));
    }
}
