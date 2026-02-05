package com.lmc.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(200),
    CREATED(201),
    USER_ALREADY_EXISTS(409),
    USER_NOT_FOUND(404),
    INVALID_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    INTERNAL_ERROR(500);

    private final int status;

}
