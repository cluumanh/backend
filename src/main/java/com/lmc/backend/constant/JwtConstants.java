package com.lmc.backend.constant;

public class JwtConstants {
    private JwtConstants() {
        throw new AssertionError("JwtConstants cannot be instantiated");
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = 7;
    public static final String ROLES_CLAIM = "roles";
    public static final String ALGORITHM = "HS512";
    public static final String DEFAULT_SECRET = "default-secret";
}
