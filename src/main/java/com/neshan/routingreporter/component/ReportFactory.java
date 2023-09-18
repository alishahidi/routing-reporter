package com.neshan.routingreporter.component;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.interfaces.ReportInterface;
import com.neshan.routingreporter.mapper.*;
import com.neshan.routingreporter.model.*;
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
            case "WeatherReport" -> ReportType.WEATHER;
            case "BumpReport" -> ReportType.BUMP;
            case "CameraReport" -> ReportType.CAMERA;
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
        } else if (report instanceof WeatherReport) {
            return WeatherReportMapper.INSTANCE.weatherReportToWeatherReportDto((WeatherReport) report);
        } else if (report instanceof BumpReport) {
            return BumpReportMapper.INSTANCE.bumpReportToBumpReportDto((BumpReport) report);
        } else if (report instanceof CameraReport) {
            return CameraReportMapper.INSTANCE.cameraReportToCameraReportDto((CameraReport) report);
        }
        return ReportMapper.INSTANCE.toReportDto(report);
    }
}