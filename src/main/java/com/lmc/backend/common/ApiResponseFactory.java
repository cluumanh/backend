package com.lmc.backend.common;

import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.dto.response.ApiResponse;

import java.time.LocalDateTime;

public final class ApiResponseFactory {
    private ApiResponseFactory() {
    }

    public static <T> ApiResponse<T> success(ErrorCode code, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(code.getStatus())
                .code(code.name())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Void> success(ErrorCode code, String message) {
        return success(code, message, null);
    }

    public static ApiResponse<Void> fail(ErrorCode code, String message) {
        return ApiResponse.<Void>builder()
                .success(false)
                .status(code.getStatus())
                .code(code.name())
                .message(message)
                .timestamp(LocalDateTime.now()).build();
    }

}
