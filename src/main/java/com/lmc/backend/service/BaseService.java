package com.lmc.backend.service;

public interface BaseService<E, ID, R> {
    R findById(ID id);
    R save(E entity);
}
