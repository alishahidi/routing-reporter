package com.neshan.routingreporter.model;

import jakarta.persistence.*;
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
    String someValue;
}
