package com.lmc.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;


@Getter
@SuperBuilder
public abstract class BaseDto<ID> {
    private ID id;
    private Instant created;
    private Instant updated;
}
