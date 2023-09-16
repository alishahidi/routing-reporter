package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.RouteDto;
import com.neshan.routingreporter.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    RouteDto routeToRouteDto(Route route);
}
