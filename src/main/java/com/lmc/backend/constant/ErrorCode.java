package com.lmc.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(200, "Success"),
    CREATED(201, "Created"),
    USER_ALREADY_EXISTS(409, "User already exists"),
    USER_NOT_FOUND(404, "User not found"),
    INVALID_REQUEST(400, "Invalid request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Access denied"),
    INTERNAL_ERROR(500, "Internal server error");

    private final int status;
    private final String message;
}
