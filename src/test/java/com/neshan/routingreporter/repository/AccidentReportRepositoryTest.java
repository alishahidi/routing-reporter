package com.neshan.routingreporter.repository;

import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.enums.Role;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.User;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AccidentReportRepositoryTest {
    @Autowired
    private AccidentReportRepository accidentReportRepository;

    private final User user = User.builder()
            .id(1L)
            .name("Ali Shahidi")
            .role(Role.ADMIN)
            .password("testpass")
            .username("alishahidi")
            .build();

    @Test
    @WithMockUser(username = "alishahidi")
    @Sql("/user.sql")
    void createReport() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(35.7004, 51.3376);

        AccidentReport accidentReport = AccidentReport.builder()
                .type(ReportType.ACCIDENT)
                .isAccept(true)
                .likeCount(0)
                .location(geometryFactory.createPoint(coordinate))
                .user(user)
                .build();

        AccidentReport report = accidentReportRepository.save(accidentReport);

        assertThat(report)
                .isNotNull()
                .extracting(AccidentReport::getIsAccept, AccidentReport::getType)
                .containsExactly(true, ReportType.ACCIDENT);
    }
}