package com.lmc.backend.constant;

public class UserPaths {
    private UserPaths() {
        throw new AssertionError("UserPaths cannot be instantiated");
    }

    public static final String ROOT = ApiPaths.API_V1 + "/users";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String ADMIN = "/admin";


}
