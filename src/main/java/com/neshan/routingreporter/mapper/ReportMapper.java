package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    ReportDto reportToReportDto(Report report);
}