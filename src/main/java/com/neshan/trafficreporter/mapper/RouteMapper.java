package com.neshan.trafficreporter.mapper;

import com.neshan.trafficreporter.dto.RouteDto;
import com.neshan.trafficreporter.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    RouteDto routeToRouteDto(Route route);
}
