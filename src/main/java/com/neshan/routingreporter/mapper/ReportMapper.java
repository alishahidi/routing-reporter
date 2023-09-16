package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.AccidentReportDto;
import com.neshan.routingreporter.dto.PoliceReportDto;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.dto.TrafficReportDto;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.PoliceReport;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.TrafficReport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    ReportDto toReportDto(Report report);

    default AccidentReportDto accidentReportToAccidentReportDto(AccidentReport report) {
        return Mappers.getMapper(AccidentReportMapper.class).accidentReportToAccidentReportDto(report);
    }

    default TrafficReportDto trafficReportToTrafficReportDto(TrafficReport report) {
        return Mappers.getMapper(TrafficReportMapper.class).trafficReportToTrafficReportDto(report);
    }

    default PoliceReportDto policeReportToPoliceReportDto(PoliceReport report) {
        return Mappers.getMapper(PoliceReportMapper.class).policeReportToPoliceReportDto(report);
    }
}
