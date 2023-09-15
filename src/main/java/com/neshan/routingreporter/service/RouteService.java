package com.neshan.routingreporter.service;

import com.neshan.routingreporter.dto.RouteDto;
import com.neshan.routingreporter.dto.RouteReportsDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.mapper.AccidentReportMapper;
import com.neshan.routingreporter.mapper.PoliceReportMapper;
import com.neshan.routingreporter.mapper.RouteMapper;
import com.neshan.routingreporter.mapper.TrafficReportMapper;
import com.neshan.routingreporter.model.Route;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.AccidentReportRepository;
import com.neshan.routingreporter.repository.PoliceReportRepository;
import com.neshan.routingreporter.repository.RouteRepository;
import com.neshan.routingreporter.repository.TrafficReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.LineString;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RouteService {
    RouteRepository routeRepository;
    TrafficReportRepository trafficReportRepository;
    AccidentReportRepository accidentReportRepository;
    PoliceReportRepository policeReportRepository;

    public RouteReportsDto getAllReportAroundRoute(RouteDto routeDto) {
        return RouteReportsDto.builder()
                .traffic(trafficReportRepository.findReportsWithinRouteRadius(routeDto.getRoute())
                        .stream()
                        .map(TrafficReportMapper.INSTANCE::trafficReportToTrafficReportDto)
                        .toList()
                )
                .accident(accidentReportRepository.findReportsWithinRouteRadius(routeDto.getRoute())
                        .stream()
                        .map(AccidentReportMapper.INSTANCE::accidentReportToAccidentReportDto)
                        .toList()
                )
                .police(policeReportRepository.findReportsWithinRouteRadius(routeDto.getRoute())
                        .stream()
                        .map(PoliceReportMapper.INSTANCE::policeReportToPoliceReportDto)
                        .toList()
                )
                .build();
    }

    public RouteDto create(RouteDto routeDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LineString lineString = routeDto.getRoute();
        lineString.setSRID(3857);
        return RouteMapper.INSTANCE.routeToRouteDto(routeRepository.save(
                Route.builder()
                        .route(routeDto.getRoute())
                        .user(user)
                        .build()
        ));
    }

    public List<RouteDto> getAll() {
        return routeRepository.findAll().stream().map(RouteMapper.INSTANCE::routeToRouteDto).toList();
    }
}
