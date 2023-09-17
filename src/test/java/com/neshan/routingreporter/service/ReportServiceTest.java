package com.neshan.routingreporter.service;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.AccidentType;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.enums.TrafficType;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private final List<Report> reports = List.of(
            AccidentReport.builder()
                    .id(1L)
                    .type(ReportType.ACCIDENT)
                    .accidentType(AccidentType.HEAVY)
                    .location(geometryFactory.createPoint(new Coordinate(35.7004, 51.3376)))
                    .build(),
            TrafficReport.builder()
                    .id(2L)
                    .type(ReportType.TRAFFIC)
                    .trafficType(TrafficType.SOFT)
                    .location(geometryFactory.createPoint(new Coordinate(36.7004, 51.3376)))
                    .build()
    );

    @Test
    void getAllReportTest() {
        Mockito.when(reportRepository.findAll()).thenReturn(reports);

        List<ReportDto> result = reportService.getAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getType()).isEqualTo(ReportType.ACCIDENT);
        assertThat(result.get(0)).isInstanceOf(ReportDto.class);
    }

    @Test
    void getByIdTest() {

    }
}