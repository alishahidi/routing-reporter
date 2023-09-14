package com.neshan.trafficreporter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteDto {
    Long id;
    UserDto user;
    Point startLocation;
    Point endLocation;
    LineString route;

}
