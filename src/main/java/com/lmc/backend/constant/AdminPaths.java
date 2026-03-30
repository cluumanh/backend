package com.lmc.backend.constant;

public class AdminPaths {
    private AdminPaths() {
        throw new AssertionError("AdminPaths cannot be instantiated");
    }

    public static final String ROOT = ApiPaths.API_V1 + "/admin";
    public static final String USERS = ROOT + "/users";

}
