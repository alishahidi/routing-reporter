package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.RouteDto;
import com.neshan.routingreporter.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    @Mapping(expression = "java(route.getRoute().toText())", target = "route")
    RouteDto routeToRouteDto(Route route);

}
