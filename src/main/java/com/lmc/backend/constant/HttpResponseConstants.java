package com.lmc.backend.constant;

public class HttpResponseConstants {
    private HttpResponseConstants() {
        throw new AssertionError("HttpResponseConstants cannot be instantiated");
    }

    public static final String SUCCESS = "SUCCESS";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String CONFLICT = "CONFLICT";
    public static final String ERROR = "ERROR";
}
