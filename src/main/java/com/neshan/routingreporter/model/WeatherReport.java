package com.neshan.routingreporter.model;

import com.neshan.routingreporter.enums.WeatherType;
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
@Table(name = "weather_reports")
public class WeatherReport extends Report {
    @Enumerated(EnumType.STRING)
    WeatherType weatherType;
}
