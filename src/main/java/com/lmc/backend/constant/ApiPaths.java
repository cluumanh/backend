package com.lmc.backend.constant;

public class ApiPaths {
    private ApiPaths() {
        throw new AssertionError("ApiPaths cannot be instantiated");
    }

    public static final String AUTH_BASE = "/api/auth";
    public static final String AUTH_LOGIN = AUTH_BASE + "/user/login";
    public static final String AUTH_REGISTER = AUTH_BASE + "/user/register";
    public static final String AUTH_PROFILE = AUTH_BASE + "/profile";

    public static final String PUBLIC_BASE = "/api/public";
    public static final String PUBLIC_HEALTH = PUBLIC_BASE + "/health";

    public static final String USER_BASE = "/api/user";
    public static final String USER_PROFILE = USER_BASE + "/profile";

    public static final String ADMIN_BASE = "/api/admin";
    public static final String ADMIN_USERS = ADMIN_BASE + "/users";

    public static final String[] PUBLIC_PATHS = {
            PUBLIC_HEALTH, AUTH_LOGIN, AUTH_REGISTER
    };

    public static final String[] USER_PATHS = {USER_PROFILE};
    public static final String[] ADMIN_PATHS = {ADMIN_USERS};
}
