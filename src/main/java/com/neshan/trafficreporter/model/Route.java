package com.neshan.trafficreporter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    @JsonIgnore
    User user;

    @Column(columnDefinition = "geometry(Point,4326)")
    Point startLocation;

    @Column(columnDefinition = "geometry(Point,4326)")
    Point endLocation;

    @Column(columnDefinition = "geometry(LineString,4326)")
    LineString location;
}
