package com.neshan.trafficreporter.mapper;

import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.model.Report;
import org.mapstruct.factory.Mappers;

public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    ReportDto reportToReportDto(Report report);
}
