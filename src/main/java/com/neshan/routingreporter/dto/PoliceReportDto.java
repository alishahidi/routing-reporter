package com.neshan.routingreporter.dto;

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
    String someValue;

}
