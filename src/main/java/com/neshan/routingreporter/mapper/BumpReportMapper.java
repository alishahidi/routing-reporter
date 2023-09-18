package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.BumpReportDto;
import com.neshan.routingreporter.model.BumpReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BumpReportMapper {
    BumpReportMapper INSTANCE = Mappers.getMapper(BumpReportMapper.class);

    @Mapping(expression = "java(report.getLocation().toText())", target = "location")
    BumpReportDto bumpReportToBumpReportDto(BumpReport report);

}
