package com.neshan.trafficreporter.component;

import com.neshan.trafficreporter.enums.ReportType;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import com.neshan.trafficreporter.service.TrafficReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportFactory {
    TrafficReportService trafficReportService;

    public ReportInterface makeReport(ReportType reportType) {
        return switch (reportType) {
            case TRAFFIC -> trafficReportService;
            case ACCIDENT -> trafficReportService;
            case POLICE -> trafficReportService;
            default -> throw new IllegalArgumentException("Invalid report type");
        };
    }
}
