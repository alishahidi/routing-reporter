package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.CameraReportDto;
import com.neshan.routingreporter.model.CameraReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CameraReportMapper {
    CameraReportMapper INSTANCE = Mappers.getMapper(CameraReportMapper.class);

    @Mapping(expression = "java(report.getLocation().toText())", target = "location")
    CameraReportDto cameraReportToCameraReportDto(CameraReport report);

}
