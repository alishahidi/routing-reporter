package com.neshan.trafficreporter.mapper;

import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.dto.RouteDto;
import com.neshan.trafficreporter.dto.UserDto;
import com.neshan.trafficreporter.model.Report;
import com.neshan.trafficreporter.model.Route;
import com.neshan.trafficreporter.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDTO(User user);

    default ReportDto reportToReportDto(Report report) {
        return Mappers.getMapper(ReportMapper.class).reportToReportDto(report);
    }

    default RouteDto routeToRouteDto(Route route) {
        return Mappers.getMapper(RouteMapper.class).routeToRouteDto(route);
    }
}

