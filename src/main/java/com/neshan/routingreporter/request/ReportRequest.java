package com.neshan.routingreporter.request;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.AccidentType;
import com.neshan.routingreporter.enums.PoliceType;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.enums.TrafficType;
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

}
