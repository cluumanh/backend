package com.lmc.backend.constant;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Role fromAuthority(String authority) {
        if (authority == null) {
            throw new IllegalArgumentException("Authority is null");
        }

        return Role.valueOf(
                authority.replace("ROLE_", "").toUpperCase()
        );
    }
}

