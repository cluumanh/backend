package com.lmc.backend.exception;

import com.lmc.backend.constant.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

}
