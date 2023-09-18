package com.neshan.routingreporter.repository;

import com.neshan.routingreporter.enums.AccidentType;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.enums.Role;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@Sql("/user.sql")
@Sql("/report.sql")
class ReportRepositoryTest {
    @Autowired
    private ReportRepository reportRepository;

    private final User user = User.builder()
            .id(1L)
            .name("Ali Shahidi")
            .role(Role.ADMIN)
            .password("testpass")
            .username("alishahidi")
            .build();

    @Test
    void createReportTest() {
        GeometryFactory geometryFactory = new GeometryFactory();

        AccidentReport accidentReport = AccidentReport.builder()
                .type(ReportType.ACCIDENT)
                .isAccept(true)
                .likeCount(0)
                .location(geometryFactory.createPoint(new Coordinate(35.7004, 51.3376)))
                .user(user)
                .build();

        AccidentReport report = reportRepository.save(accidentReport);

        assertThat(report).isNotNull();
        assertThat(report.getIsAccept()).isEqualTo(true);
        assertThat(report.getType()).isEqualTo(ReportType.ACCIDENT);
    }

    @Test
    void findReportTest() {
        Optional<Report> report = reportRepository.findById(1L);
        assertThat(report).isPresent();
        AccidentReport accidentReport = (AccidentReport) report.get();
        assertThat(accidentReport.getAccidentType()).isEqualTo(AccidentType.HEAVY);
    }

    @Test
    void updateReportTest() {
        Optional<Report> reportOptional = reportRepository.findById(1L);
        assertThat(reportOptional).isPresent();
        AccidentReport accidentReport = (AccidentReport) reportOptional.get();
        accidentReport.setAccidentType(AccidentType.SOFT);
        reportRepository.save(accidentReport);

        Optional<Report> updatedReportOptional = reportRepository.findById(1L);
        assertThat(updatedReportOptional).isPresent();
        AccidentReport updatedAccidentReport = (AccidentReport) updatedReportOptional.get();
        assertThat(updatedAccidentReport.getAccidentType()).isEqualTo(AccidentType.SOFT);
    }

    @Test
    void deleteReportTest() {
        Optional<Report> reportOptional = reportRepository.findById(1L);
        assertThat(reportOptional).isPresent();
        reportOptional.ifPresent(reportRepository::delete);

        Optional<Report> deletedReportOptional = reportRepository.findById(1L);
        assertThat(deletedReportOptional).isNotPresent();
    }
}