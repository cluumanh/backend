package com.lmc.backend.mapper;

public interface BaseMapper<E, R> {
    R toDto(E entity);
    E toEntity(R dto);
}
