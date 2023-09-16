package com.neshan.routingreporter.model;

import com.neshan.routingreporter.enums.TrafficType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "traffic_reports")
public class TrafficReport extends Report {
    @Enumerated(EnumType.STRING)
    TrafficType trafficType;
}
