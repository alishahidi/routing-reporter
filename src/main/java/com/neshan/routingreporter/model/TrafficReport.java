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
@Table(name = "traffic_reports")
public class TrafficReport extends Report {
    Boolean isAccept;

}
