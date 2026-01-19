package com.lmc.backend.constant;

public class ValidationConstants {
    private ValidationConstants() {
        throw new AssertionError("ValidationConstants cannot be instantiated");
    }

    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MIN_PASSWORD_LENGTH = 6;

    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String USERNAME_MIN = "Username must be at least " + MIN_USERNAME_LENGTH + " characters";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_MIN = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Email should be valid";
    public static final String ROLE_REQUIRED = "Role is required";
}
