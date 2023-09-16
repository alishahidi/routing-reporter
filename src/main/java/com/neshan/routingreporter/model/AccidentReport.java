package com.neshan.routingreporter.model;

import com.neshan.routingreporter.enums.AccidentType;
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
@Table(name = "accident_reports")
public class AccidentReport extends Report {
    @Enumerated(EnumType.STRING)
    AccidentType accidentType;
}
