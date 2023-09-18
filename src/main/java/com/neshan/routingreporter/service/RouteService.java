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
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
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
        return reportRepository.findReportsWithinRouteRadius(routeDto.getRoute())
                .stream()
                .map(ReportFactory::mapToMapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public RouteDto create(RouteDto routeDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WKTReader wktReader = new WKTReader(new GeometryFactory());
        LineString lineString = null;
        try {
            lineString = (LineString) wktReader.read(routeDto.getRoute());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        lineString.setSRID(3857);
        return RouteMapper.INSTANCE.routeToRouteDto(routeRepository.save(
                Route.builder()
                        .route(lineString)
                        .user(user)
                        .build()
        ));
    }
}
