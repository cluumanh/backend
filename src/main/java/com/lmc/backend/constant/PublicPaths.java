package com.lmc.backend.constant;

public class PublicPaths {
    private PublicPaths() {
        throw new  AssertionError("PublicPaths cannot be instantiated");
    }

    public static final String ROOT = ApiPaths.API_V1 + "/public";
    public static final String HEALTH = "/health";

}
