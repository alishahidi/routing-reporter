package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.PoliceReportDto;
import com.neshan.routingreporter.model.PoliceReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PoliceReportMapper {
    PoliceReportMapper INSTANCE = Mappers.getMapper(PoliceReportMapper.class);

    @Mapping(expression = "java(report.getLocation().toText())", target = "location")
    PoliceReportDto policeReportToPoliceReportDto(PoliceReport report);
}
