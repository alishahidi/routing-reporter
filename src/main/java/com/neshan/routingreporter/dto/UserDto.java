package com.neshan.routingreporter.dto;

import com.neshan.routingreporter.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String name;
    String username;
    Role role;
    List<TrafficReportDto> trafficReports;
    List<AccidentReportDto> accidentReports;
    List<PoliceReportDto> policeReports;
    List<RouteDto> routes;
    Date createdAt;
    Date updatedAt;

}
