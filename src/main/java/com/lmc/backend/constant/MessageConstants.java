package com.lmc.backend.constant;

public class MessageConstants {
    private MessageConstants() {
        throw new  AssertionError("MessageConstants cannot be instantiated");
    }

    public static final String SUCCESS = "success";
    public static final String LOGIN_FAILED = "login failed";
    public static final String GET_USERS_FAILED = "get users failed";
    public static final String USER_NOT_FOUND = "User not found";
}
