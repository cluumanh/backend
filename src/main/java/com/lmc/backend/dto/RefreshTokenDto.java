package com.lmc.backend.dto;

import com.lmc.backend.entity.User;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@SuperBuilder
public class RefreshTokenDto extends BaseDto<Long> {
    private String token;
    private User user;
    private Instant expiryDate;
    private boolean revoked;
    private String ipAddress;
    private String userAgent;
}
