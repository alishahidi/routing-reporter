package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.*;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.Route;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDTO(User user);

    default ReportDto reportToReportDto(Report report) {
        return Mappers.getMapper(ReportMapper.class).toReportDto(report);
    }

    default RouteDto routeToRouteDto(Route route) {
        return Mappers.getMapper(RouteMapper.class).routeToRouteDto(route);
    }
}

