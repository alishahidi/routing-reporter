package com.neshan.trafficreporter.service;

import com.neshan.trafficreporter.component.ReportFactory;
import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    ReportFactory reportFactory;

    public ReportDto createReport(ReportDto reportDto){
        ReportInterface report = reportFactory.makeReport(reportDto.getType());

        return report.createReport(reportDto);
    }
}
