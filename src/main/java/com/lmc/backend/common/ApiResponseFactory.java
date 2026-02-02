package com.lmc.backend.common;

import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.dto.ApiResponse;

import java.time.LocalDateTime;

public final class ApiResponseFactory {
    private ApiResponseFactory() {}

    public static <T> ApiResponse<T> success(ErrorCode code, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(code.getStatus())
                .code(code.name())
                .message(code.getMessage())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Void> success(ErrorCode code) {
        return success(code, null);
    }

    public static ApiResponse<Void> fail(ErrorCode code) {
        return  ApiResponse.<Void>builder()
                .success(false)
                .status(code.getStatus())
                .code(code.name())
                .message(code.getMessage())
                .timestamp(LocalDateTime.now()).build();
    }

}
