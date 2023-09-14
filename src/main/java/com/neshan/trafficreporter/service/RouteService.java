package com.neshan.trafficreporter.service;

import com.neshan.trafficreporter.dto.RouteDto;
import com.neshan.trafficreporter.mapper.RouteMapper;
import com.neshan.trafficreporter.model.Route;
import com.neshan.trafficreporter.model.User;
import com.neshan.trafficreporter.repository.RouteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.LineString;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RouteService {
    RouteRepository routeRepository;

    public RouteDto createRoute(RouteDto routeDto) {
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
