package com.neshan.trafficreporter.mapper;

import com.neshan.trafficreporter.dto.UserDto;
import com.neshan.trafficreporter.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDTO(User user);
}

