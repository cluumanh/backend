package com.lmc.backend.service.impl;

import com.amazonaws.internal.ExceptionUtils;
import com.lmc.backend.config.security.JwtConfig;
import com.lmc.backend.config.security.JwtUtil;
import com.lmc.backend.dto.TokenPair;
import com.lmc.backend.enity.RefreshToken;
import com.lmc.backend.enity.User;
import com.lmc.backend.mapper.UserMapper;
import com.lmc.backend.service.RefreshTokenService;
import com.lmc.backend.service.TokenManager;
import com.lmc.backend.service.UserService;
import com.lmc.backend.value.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenManagerImpl implements TokenManager {
    private static final Logger logger = LoggerFactory.getLogger(TokenManagerImpl.class);
    private final RefreshTokenService refreshTokenService;
    private final JwtConfig jwtConfig;
    //private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public TokenPair createTokenPair(User user, ClientInfo clientInfo) {
        try {
            logger.info("Creating token pair for user: {}", user.getUsername());

            enforceSessionLimit(user);

            UserDetails userDetails = userMapper.toDto(user);
            if (userDetails == null)
                return null;
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshTokenString = jwtUtil.generateRefreshToken(userDetails);

            RefreshToken refreshToken = buildRefreshToken(user, refreshTokenString, clientInfo);
            refreshTokenService.save(refreshToken);

            return TokenPair.of(
                    accessToken,
                    refreshTokenString,
                    jwtConfig.getAccessExpiration(),
                    jwtConfig.getRefreshExpiration()
            );
        } catch (Exception e) {
            logger.error("createTokenPair Error: {}",
                    ExceptionUtils.exceptionStackTrace(e));
        }
        return null;

    }

    @Override
    @Transactional
    public TokenPair refreshAccessToken(String refreshTokenString) {
        if (!jwtUtil.isTokenValid(refreshTokenString)) {
            throw new IllegalArgumentException("Invalid refresh token format");
        }

        RefreshToken refreshToken = refreshTokenService
                .findValidToken(refreshTokenString)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found or revoked"));

        refreshToken = refreshTokenService.verifyAndUpdateToken(refreshToken);

        User user = refreshToken.getUser();
        UserDetails userDetails = userMapper.toDto(user);

        String newAccessToken = jwtUtil.generateAccessToken(userDetails);

        return TokenPair.of(
                newAccessToken,
                refreshTokenString,
                jwtConfig.getAccessExpiration(),
                jwtConfig.getRefreshExpiration()
        );
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenService.revokeAllUserTokens(user);
    }

    private void enforceSessionLimit(User user) {
        long activeCount = refreshTokenService.getActiveSessionCount(user);
        if (activeCount >= jwtConfig.getMaxActiveSessions()) {
            refreshTokenService.getOldestActiveToken(user).ifPresent(oldest -> {
                oldest.revoke();
                refreshTokenService.save(oldest);
                logger.info("Oldest session revoked for user: {}", user.getUsername());
            });
        }
    }

    private RefreshToken buildRefreshToken(User user, String token, ClientInfo clientInfo) {
        Instant expiryDate = Instant.now().plusMillis(jwtConfig.getRefreshExpiration());

        return RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiryDate)
                .revoked(false)
                .ipAddress(clientInfo.clientId())
                .userAgent(clientInfo.userAgent())
                .build();
    }
}
