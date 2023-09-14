package com.neshan.trafficreporter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neshan.trafficreporter.enums.ReportType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    ReportType type;

    Boolean isAccept;

    LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    @JsonIgnore
    User user;

    @Column(columnDefinition = "geometry(Point,3857)")
    Point location;

    Integer likeCount;

    @CreationTimestamp
    Date createdAt;

    @UpdateTimestamp
    Date updatedAt;
}
