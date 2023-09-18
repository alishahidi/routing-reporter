package com.neshan.routingreporter.service;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.dto.RouteDto;
import com.neshan.routingreporter.enums.AccidentType;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.enums.Role;
import com.neshan.routingreporter.enums.TrafficType;
import com.neshan.routingreporter.model.*;
import com.neshan.routingreporter.repository.ReportRepository;
import com.neshan.routingreporter.repository.RouteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private RouteService routeService;

    @Mock
    private Authentication authentication;

    private final User user = User.builder()
            .id(1L)
            .name("Ali Shahidi")
            .role(Role.ADMIN)
            .password("testpass")
            .username("alishahidi")
            .build();

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
    public void getAllReportAroundRouteTest() {
        Mockito.when(reportRepository.findReportsWithinRouteRadius(ArgumentMatchers.anyString())).thenReturn(reports);

        RouteDto routeDto = RouteDto.builder()
                .route("LINESTRING (59.557710772175795 36.28857021126669, 59.55968013271732 36.292981810238715)")
                .build();

        List<ReportDto> result = routeService.getAllReportAroundRoute(routeDto);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getType()).isEqualTo(ReportType.ACCIDENT);
        assertThat(result.get(0)).isInstanceOf(ReportDto.class);
    }

    @Test
    public void createTest() {
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        RouteDto routeDto = RouteDto.builder()
                .route("LINESTRING (59.557710772175795 36.28857021126669, 59.55968013271732 36.292981810238715)")
                .build();

        WKTReader wktReader = new WKTReader(new GeometryFactory());
        LineString lineString = null;
        try {
            lineString = (LineString) wktReader.read(routeDto.getRoute());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        lineString.setSRID(3857);
        Route route = Route.builder()
                .route(lineString)
                .build();

        Mockito.when(routeRepository.save(ArgumentMatchers.any(Route.class))).thenReturn(route);

        RouteDto result = routeService.create(routeDto);
        assertThat(result).isNotNull();
        assertThat(result.getRoute()).isEqualTo(routeDto.getRoute());
    }
}