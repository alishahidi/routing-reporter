package com.neshan.trafficreporter.interfaces;

import com.neshan.trafficreporter.dto.ReportDto;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportInterface {
    ReportDto createReport(ReportDto reportDto);

    List<ReportDto> getAllReport();

    default ReportDto accept(Long id) {
        return null;
    }
}
