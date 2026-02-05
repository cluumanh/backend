package com.lmc.backend.service;

import com.lmc.backend.dto.RefreshTokenDto;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.enity.RefreshToken;
import com.lmc.backend.enity.User;
import com.lmc.backend.value.ClientInfo;

import java.util.Optional;

public interface RefreshTokenService  extends BaseService<RefreshToken, Long, RefreshTokenDto> {
    Optional<RefreshToken> findValidToken(String token);

    RefreshToken verifyAndUpdateToken(RefreshToken token);

    void revokeToken(String token);

    void revokeAllUserTokens(User user);

    void cleanupExpiredTokens();

    long getActiveSessionCount(User user);

    Optional<RefreshToken> getOldestActiveToken(User user);
}
