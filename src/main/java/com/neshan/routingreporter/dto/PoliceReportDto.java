package com.neshan.routingreporter.dto;

import com.neshan.routingreporter.enums.PoliceType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PoliceReportDto extends ReportDto {
    PoliceType policeType;

}
