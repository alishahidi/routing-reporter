package com.neshan.routingreporter.mapper;

import com.neshan.routingreporter.dto.AuthenticationDto;
import com.neshan.routingreporter.dto.UserDto;
import com.neshan.routingreporter.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface AuthenticationMapper {
    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    AuthenticationDto authenticationToAuthenticationDto(User user, String token);

    default UserDto userToUserDto(User user) {
        return Mappers.getMapper(UserMapper.class).userToUserDTO(user);
    }
}
