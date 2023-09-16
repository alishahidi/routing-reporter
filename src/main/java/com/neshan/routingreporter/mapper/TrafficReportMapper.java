package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.TrafficReportDto;
import com.neshan.routingreporter.model.TrafficReport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrafficReportMapper {
    TrafficReportMapper INSTANCE = Mappers.getMapper(TrafficReportMapper.class);

    TrafficReportDto trafficReportToTrafficReportDto(TrafficReport report);
}
