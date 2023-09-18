package com.neshan.routingreporter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neshan.routingreporter.dto.AccidentReportDto;
import com.neshan.routingreporter.enums.AccidentType;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.enums.Role;
import com.neshan.routingreporter.enums.TrafficType;
import com.neshan.routingreporter.mapper.AccidentReportMapper;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.request.ReportRequest;
import com.neshan.routingreporter.service.AccidentReportService;
import com.neshan.routingreporter.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ReportV1ControllerTest {
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

    private final User user = User.builder()
            .id(1L)
            .name("Ali Shahidi")
            .role(Role.ADMIN)
            .password("testpass")
            .username("alishahidi")
            .build();

    @Test
    void createReportTest() throws Exception {
        AccidentReportDto accidentReportDto = AccidentReportMapper.INSTANCE.accidentReportToAccidentReportDto((AccidentReport) reports.get(0));
        Mockito.when(accidentReportService.create(ArgumentMatchers.any(ReportRequest.class)))
                .thenReturn(accidentReportDto);

        ReportRequest reportRequest = ReportRequest.builder()
                .report(accidentReportDto)
                .type(ReportType.ACCIDENT)
                .accidentType(AccidentType.SOFT)
                .build();

        String requestJson = objectMapper.writeValueAsString(reportRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.accidentType").value("HEAVY"));
    }

    @Test
    void likeReportTest() throws Exception {
        AccidentReportDto accidentReportDto = AccidentReportMapper.INSTANCE.accidentReportToAccidentReportDto((AccidentReport) reports.get(0));
        Mockito.when(reportService.getById(1L)).thenReturn(reports.get(0));
        Mockito.when(accidentReportService.like(1L)).thenReturn(accidentReportDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/report/like/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.accidentType").value("HEAVY"));
    }

    @Test
    void disLikeReportTest() throws Exception {
        AccidentReportDto accidentReportDto = AccidentReportMapper.INSTANCE.accidentReportToAccidentReportDto((AccidentReport) reports.get(0));
        Mockito.when(reportService.getById(1L)).thenReturn(reports.get(0));
        Mockito.when(accidentReportService.disLike(1L)).thenReturn(accidentReportDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/report/dislike/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.accidentType").value("HEAVY"));
    }

    @Test
    void topHourReportTest() throws Exception {
        Mockito.when(reportService.getById(1L)).thenReturn(reports.get(0));
        Mockito.when(accidentReportService.findTopHours(3)).thenReturn(List.of(1, 2, 3));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/report/top/{limit}/{type}", 3, ReportType.ACCIDENT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value(1));
    }
}