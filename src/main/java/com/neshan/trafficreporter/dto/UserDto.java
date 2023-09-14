package com.neshan.trafficreporter.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neshan.trafficreporter.enums.Role;
import com.neshan.trafficreporter.model.Report;
import com.neshan.trafficreporter.model.Route;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    List<ReportDto> reports;
    List<RouteDto> routes;
    Date createdAt;
    Date updatedAt;

}
