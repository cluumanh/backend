package com.lmc.backend.service;

import com.lmc.backend.dto.RefreshTokenDto;
import com.lmc.backend.entity.RefreshToken;
import com.lmc.backend.entity.User;

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
