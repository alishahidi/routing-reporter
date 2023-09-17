package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.TrafficReportDto;
import com.neshan.routingreporter.model.TrafficReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrafficReportMapper {
    TrafficReportMapper INSTANCE = Mappers.getMapper(TrafficReportMapper.class);

    @Mapping(expression = "java(report.getLocation().toText())", target = "location")
    TrafficReportDto trafficReportToTrafficReportDto(TrafficReport report);

}
