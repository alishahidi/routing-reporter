package com.neshan.trafficreporter.interfaces;

import com.neshan.trafficreporter.dto.ReportDto;

public interface ReportInterface {
    ReportDto createReport(ReportDto reportDto);
}
