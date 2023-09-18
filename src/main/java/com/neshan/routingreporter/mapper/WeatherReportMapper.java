package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.WeatherReportDto;
import com.neshan.routingreporter.model.WeatherReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WeatherReportMapper {
    WeatherReportMapper INSTANCE = Mappers.getMapper(WeatherReportMapper.class);

    @Mapping(expression = "java(report.getLocation().toText())", target = "location")
    WeatherReportDto weatherReportToWeatherReportDto(WeatherReport report);

}
