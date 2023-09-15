package com.neshan.routingreporter.component;

import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.interfaces.ReportInterface;
import com.neshan.routingreporter.service.AccidentReportService;
import com.neshan.routingreporter.service.PoliceReportService;
import com.neshan.routingreporter.service.TrafficReportService;
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

    public ReportInterface makeReport(ReportType reportType) {
        return switch (reportType) {
            case TRAFFIC -> trafficReportService;
            case ACCIDENT -> accidentReportService;
            case POLICE -> policeReportService;
            default -> throw new IllegalArgumentException("Invalid report type");
        };
    }
}
