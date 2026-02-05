package com.lmc.backend.exception;

import com.lmc.backend.constant.ErrorCode;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
