package com.lmc.backend.service.impl;

import com.lmc.backend.config.security.JwtConfig;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.dto.RefreshTokenDto;
import com.lmc.backend.enity.RefreshToken;
import com.lmc.backend.enity.User;
import com.lmc.backend.exception.BaseException;
import com.lmc.backend.exception.TokenExpiredException;
import com.lmc.backend.mapper.RefreshTokenMapper;
import com.lmc.backend.repository.RefreshTokenRepository;
import com.lmc.backend.service.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class RefreshTokenServerImpl extends BaseServiceImpl<RefreshToken, Long, RefreshTokenDto> implements RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServerImpl.class);

    private final JwtConfig jwtConfig;
    private final RefreshTokenMapper refreshTokenMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    protected RefreshTokenServerImpl(RefreshTokenRepository refreshTokenRepository, JwtConfig jwtConfig, RefreshTokenMapper refreshTokenMapper) {
        super(refreshTokenRepository);
        this.jwtConfig = jwtConfig;
        this.refreshTokenMapper = refreshTokenMapper;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findValidToken(String token) {
        return refreshTokenRepository.findValidToken(token);
    }

    @Override
    public RefreshToken verifyAndUpdateToken(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException(ErrorCode.UNAUTHORIZED, "Token expired");
        }
        return refreshTokenRepository.save(token);
    }

    @Override
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            if (!refreshToken.isRevoked()) {
                refreshToken.revoke();
                refreshTokenRepository.save(refreshToken);
                logger.info("Token revoked for user: {}", refreshToken.getUser().getUsername());
            }
        });
    }

    @Override
    public void revokeAllUserTokens(User user) {
        int revokedCount = refreshTokenRepository.revokeAllUserTokens(user);
        logger.info("Revoked {} tokens for user: {}", revokedCount, user.getUsername());
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupExpiredTokens() {
        int deletedCount = refreshTokenRepository.deleteExpiredTokens(Instant.now());
        if (deletedCount > 0) {
            logger.info("Cleaned up {} expired tokens", deletedCount);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveSessionCount(User user) {
        return refreshTokenRepository.countByUserAndRevokedFalse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> getOldestActiveToken(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findActiveTokensByUser(user);
        return tokens.isEmpty() ? Optional.empty() : Optional.of(tokens.getFirst());
    }

    @Override
    protected RefreshTokenDto mapToResponse(RefreshToken entity) {
        return RefreshTokenDto.builder()
                .token(entity.getToken())
                .user(entity.getUser())
                .expiryDate(entity.getExpiryDate())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .build();
    }

    @Override
    protected String entityName() {
        return "RefreshToken";
    }
}
