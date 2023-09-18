package com.neshan.routingreporter.request;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    ReportType type;
    ReportDto report;
    TrafficType trafficType;
    AccidentType accidentType;
    PoliceType policeType;
    WeatherType weatherType;
    CameraType cameraType;

}
