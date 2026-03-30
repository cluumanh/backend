package com.lmc.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest<F> {

    @Min(1)
    @Builder.Default
    private Integer page = 1;

    @Min(1)
    @Max(100)
    @Builder.Default
    private Integer size = 10;

    private String sort;

    @Valid
    private F filters;
}
