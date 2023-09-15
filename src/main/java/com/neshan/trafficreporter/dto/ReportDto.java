package com.neshan.trafficreporter.dto;

import com.neshan.trafficreporter.enums.ReportType;
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
public class ReportDto {
    Long id;
    ReportType type;
    Boolean isAccept;
    Point location;
    Integer likeCount;
    LocalDateTime expiredAt;
    Date createdAt;
    Date updatedAt;

}
