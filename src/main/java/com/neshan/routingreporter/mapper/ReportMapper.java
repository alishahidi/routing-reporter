package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    @Mapping(expression = "java(report.getLocation().toText())", target = "location")
    ReportDto toReportDto(Report report);
}
