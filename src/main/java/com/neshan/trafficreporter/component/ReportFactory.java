package com.neshan.trafficreporter.component;

import com.neshan.trafficreporter.enums.ReportType;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportFactory {
    TrafficReport trafficReport;

    public ReportInterface makeReport(ReportType reportType) {
        return switch (reportType) {
            case TRAFFIC -> trafficReport;
            case ACCIDENT -> trafficReport;
            case POLICE -> trafficReport;
            default -> throw new IllegalArgumentException("Invalid report type");
        };
    }
}
