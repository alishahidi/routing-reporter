package com.neshan.routingreporter.dto;

import com.neshan.routingreporter.enums.ReportType;
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
public class TrafficReportDto {
    Long id;
    Point location;
    Boolean isAccept;
    Integer likeCount;
    LocalDateTime expiredAt;
    String someValue;
    Date createdAt;
    Date updatedAt;

}
