package com.neshan.routingreporter.service;

import com.neshan.routingreporter.component.ReportFactory;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.dto.RouteDto;
import com.neshan.routingreporter.mapper.RouteMapper;
import com.neshan.routingreporter.model.Route;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.ReportRepository;
import com.neshan.routingreporter.repository.RouteRepository;
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
    ReportRepository reportRepository;

    public List<ReportDto> getAllReportAroundRoute(RouteDto routeDto) {
        System.out.println(reportRepository.findReportsWithinRouteRadius(routeDto.getRoute()));
        return reportRepository.findReportsWithinRouteRadius(routeDto.getRoute())
                .stream()
                .map(ReportFactory::mapToMapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
