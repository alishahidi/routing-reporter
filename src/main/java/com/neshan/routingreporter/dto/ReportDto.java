package com.neshan.routingreporter.dto;

import com.neshan.routingreporter.enums.ReportType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportDto {
    Long id;
    Point location;
    ReportType type;
    Integer likeCount;
    LocalDateTime expiredAt;
    Date createdAt;
    Date updatedAt;

}
