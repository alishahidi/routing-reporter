package com.neshan.routingreporter.model;

import com.neshan.routingreporter.enums.PoliceType;
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
@Table(name = "police_reports")
public class PoliceReport extends Report {
    @Enumerated(EnumType.STRING)
    PoliceType policeType;
}
