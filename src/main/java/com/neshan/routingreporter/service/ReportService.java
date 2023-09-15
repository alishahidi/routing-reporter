package com.neshan.routingreporter.service;

import com.neshan.routingreporter.component.ReportFactory;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.dto.RouteDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.interfaces.ReportInterface;
import com.neshan.routingreporter.mapper.ReportMapper;
import com.neshan.routingreporter.repository.ReportRepository;
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

        return report.create(reportDto);
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

    public ReportType getTypeById(Long id){
        return reportRepository.findTypeById(id);
    }
}
