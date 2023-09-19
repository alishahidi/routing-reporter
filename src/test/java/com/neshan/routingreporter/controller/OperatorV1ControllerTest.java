package com.neshan.routingreporter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neshan.routingreporter.component.ReportFactory;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.AccidentType;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.enums.TrafficType;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.service.AccidentReportService;
import com.neshan.routingreporter.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OperatorV1ControllerTest {
    @MockBean
    private ReportService reportService;

    @MockBean
    private AccidentReportService accidentReportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private final List<Report> reports = List.of(
            AccidentReport.builder()
                    .id(1L)
                    .type(ReportType.ACCIDENT)
                    .likeCount(10)
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
    void getAllReportTest() throws Exception {
        List<ReportDto> reportsDto = reports
                .stream()
                .map(ReportFactory::mapToMapper)
                .filter(Objects::nonNull)
                .toList();

        Mockito.when(reportService.getAll(false)).thenReturn(reportsDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/operator/report/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].accidentType").value("HEAVY"));
    }

    @Test
    void getAllReportByTypeTest() throws Exception {
        List<ReportDto> reportsDto = reports
                .stream()
                .map(ReportFactory::mapToMapper)
                .filter(Objects::nonNull)
                .filter(reportDto -> reportDto.getType() == ReportType.ACCIDENT)
                .toList();

        Mockito.when(reportService.getAllByType(ReportType.ACCIDENT, false)).thenReturn(reportsDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/operator/report/get?type=ACCIDENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].accidentType").value("HEAVY"));
    }
}