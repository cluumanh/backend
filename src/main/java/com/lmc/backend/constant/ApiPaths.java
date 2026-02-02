package com.lmc.backend.constant;

public class ApiPaths {
    private ApiPaths() {
        throw new AssertionError("ApiPaths cannot be instantiated");
    }

    public static final String API = "/api";
    public static final String V1 = "/v1";

    public static final String API_V1 = API + V1;

    public static final String[] PUBLIC_PATHS = {
            UserPaths.ROOT + UserPaths.REGISTER,
            UserPaths.ROOT + UserPaths.LOGIN,
            PublicPaths.ROOT + PublicPaths.HEALTH,
            PublicPaths.ROOT + "/debug"
    };
}
