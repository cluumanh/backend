package com.lmc.backend.mapper;

import com.lmc.backend.dto.UserDto;
import com.lmc.backend.enity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDto> {
    @Override
    UserDto toDto(User entity);

    @Override
    User toEntity(UserDto dto);
}
