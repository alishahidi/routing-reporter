package com.neshan.trafficreporter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.LineString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteDto {
    Long id;
    LineString route;
}
