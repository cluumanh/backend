package com.lmc.backend.service.impl;

import com.amazonaws.services.lambda.model.ResourceNotFoundException;
import com.lmc.backend.mapper.BaseMapper;
import com.lmc.backend.repository.BaseRepository;
import com.lmc.backend.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class BaseServiceImpl<E, ID, R> implements BaseService<E, ID, R> {
    protected final BaseRepository<E, ID> repository;

    protected BaseServiceImpl(BaseRepository<E, ID> repository) {
        this.repository = repository;
    }

    protected abstract R mapToResponse(E entity);
    protected abstract String entityName();

    @Override
    public R findById(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName() + "-id-" + id));
        return mapToResponse(entity);
    }

    @Override
    public R save(E entity) {
        E saveEntity = repository.save(entity);
        return mapToResponse(saveEntity);
    }
}
