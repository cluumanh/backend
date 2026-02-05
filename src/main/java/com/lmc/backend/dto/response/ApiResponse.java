package com.lmc.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String path;
}
