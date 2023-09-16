package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.AccidentReportDto;
import com.neshan.routingreporter.dto.TrafficReportDto;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.TrafficReport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccidentReportMapper {
    AccidentReportMapper INSTANCE = Mappers.getMapper(AccidentReportMapper.class);

    AccidentReportDto accidentReportToAccidentReportDto(AccidentReport report);
}
