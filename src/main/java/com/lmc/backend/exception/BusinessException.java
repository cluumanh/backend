package com.lmc.backend.exception;

import com.lmc.backend.constant.ErrorCode;

public class BusinessException extends BaseException {
    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
