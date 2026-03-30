package com.lmc.backend.config;

import org.springframework.data.domain.Sort;

import java.util.Set;

public final class UserQueryConfig {
    private UserQueryConfig() {
    }

    public static final Set<String> USER_ALLOWED = Set.of("id", "createdAt", "name", "email", "status");
    public static final Sort USER_DEFAULT = Sort.by(Sort.Direction.DESC, "createdAt");
}
