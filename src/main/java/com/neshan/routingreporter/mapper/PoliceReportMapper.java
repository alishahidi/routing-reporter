package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.PoliceReportDto;
import com.neshan.routingreporter.model.PoliceReport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PoliceReportMapper {
    PoliceReportMapper INSTANCE = Mappers.getMapper(PoliceReportMapper.class);

    PoliceReportDto policeReportToPoliceReportDto(PoliceReport report);
}
