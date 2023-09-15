package com.neshan.routingreporter.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PoliceReportDto {
    Long id;
    Point location;
    Integer likeCount;
    LocalDateTime expiredAt;
    String someValue;
    Date createdAt;
    Date updatedAt;

}
