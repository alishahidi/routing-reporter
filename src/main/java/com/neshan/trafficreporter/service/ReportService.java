package com.neshan.trafficreporter.service;

import com.neshan.trafficreporter.component.ReportFactory;
import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.dto.RouteDto;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import com.neshan.trafficreporter.mapper.ReportMapper;
import com.neshan.trafficreporter.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    ReportFactory reportFactory;
    ReportRepository reportRepository;

    public ReportDto createReport(ReportDto reportDto) {
        ReportInterface report = reportFactory.makeReport(reportDto.getType());

        return report.createReport(reportDto);
    }

    public List<ReportDto> getAllReportAroundRoute(RouteDto routeDto) {
        return reportRepository.findReportsWithinRouteRadius(routeDto.getRoute())
                .stream()
                .map(ReportMapper.INSTANCE::reportToReportDto)
                .toList();
    }

    public List<ReportDto> getAllReport(){
        return reportRepository.findAll()
                .stream()
                .map(ReportMapper.INSTANCE::reportToReportDto)
                .toList();
    }
}
