package com.lmc.backend.mapper;

import com.lmc.backend.dto.RefreshTokenDto;
import com.lmc.backend.entity.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper extends BaseMapper<RefreshToken, RefreshTokenDto> {
    RefreshTokenDto toDto(RefreshToken entity);

    RefreshToken toEntity(RefreshTokenDto dto);
}
