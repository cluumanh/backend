package com.lmc.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BaseDto<ID> {
    private ID id;
    private LocalDateTime created;
    private LocalDateTime updated;
}
