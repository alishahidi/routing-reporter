package com.neshan.routingreporter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteReportsDto {
    List<TrafficReportDto> traffic;
    List<AccidentReportDto> accident;
    List<PoliceReportDto> police;

}
