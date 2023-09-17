package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.AccidentReportDto;
import com.neshan.routingreporter.model.AccidentReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccidentReportMapper {
    AccidentReportMapper INSTANCE = Mappers.getMapper(AccidentReportMapper.class);

    @Mapping(expression = "java(report.getLocation().toText())", target = "location")
    AccidentReportDto accidentReportToAccidentReportDto(AccidentReport report);

}
