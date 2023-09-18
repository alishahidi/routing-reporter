package com.neshan.routingreporter.component;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.interfaces.ReportInterface;
import com.neshan.routingreporter.mapper.AccidentReportMapper;
import com.neshan.routingreporter.mapper.PoliceReportMapper;
import com.neshan.routingreporter.mapper.ReportMapper;
import com.neshan.routingreporter.mapper.TrafficReportMapper;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.PoliceReport;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportFactory {
    TrafficReportService trafficReportService;
    PoliceReportService policeReportService;
    AccidentReportService accidentReportService;
    CameraReportService cameraReportService;
    BumpReportService bumpReportService;
    WeatherReportService weatherReportService;

    public ReportInterface makeReport(ReportType reportType) {
        return switch (reportType) {
            case TRAFFIC -> trafficReportService;
            case ACCIDENT -> accidentReportService;
            case POLICE -> policeReportService;
            case CAMERA -> cameraReportService;
            case WEATHER -> weatherReportService;
            case BUMP -> bumpReportService;
            default -> throw new IllegalArgumentException("Invalid report type");
        };
    }

    public static ReportType findTypeByClass(Report report) {
        return switch (report.getClass().getSimpleName()) {
            case "TrafficReport" -> ReportType.TRAFFIC;
            case "AccidentReport" -> ReportType.ACCIDENT;
            case "PoliceReport" -> ReportType.POLICE;
            default -> throw new IllegalArgumentException("Invalid report type");
        };
    }

    public static ReportDto mapToMapper(Report report) {
        if (report instanceof TrafficReport) {
            return TrafficReportMapper.INSTANCE.trafficReportToTrafficReportDto((TrafficReport) report);
        } else if (report instanceof AccidentReport) {
            return AccidentReportMapper.INSTANCE.accidentReportToAccidentReportDto((AccidentReport) report);
        } else if (report instanceof PoliceReport) {
            return PoliceReportMapper.INSTANCE.policeReportToPoliceReportDto((PoliceReport) report);
        }
        return ReportMapper.INSTANCE.toReportDto(report);
    }
}