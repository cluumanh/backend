package com.lmc.backend.constant;

public class AuthPaths {
    private AuthPaths() {
        throw new AssertionError("UserPaths cannot be instantiated");
    }

    public static final String ROOT = ApiPaths.API_V1 + "/auth";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
}
