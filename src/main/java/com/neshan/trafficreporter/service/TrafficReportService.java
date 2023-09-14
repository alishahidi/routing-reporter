package com.neshan.trafficreporter.service;

import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.enums.ReportType;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import com.neshan.trafficreporter.mapper.ReportMapper;
import com.neshan.trafficreporter.model.Report;
import com.neshan.trafficreporter.model.User;
import com.neshan.trafficreporter.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

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
                        .expiredAt(null)
                        .type(reportDto.getType())
                        .isAccept(false)
                        .user(user)
                        .likeCount(0)
                        .location(reportDto.getLocation())
                        .build()
        ));
    }

    @Override
    public List<ReportDto> getAllReport() {
        return reportRepository.findAllByType(ReportType.TRAFFIC)
                .stream()
                .map(ReportMapper.INSTANCE::reportToReportDto)
                .toList();
    }

    @Override
    public ReportDto accept(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setIsAccept(true);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(2));
        reportRepository.save(report);
        return ReportMapper.INSTANCE.reportToReportDto(report);
    }
}
