package com.lmc.backend.config.security;


import com.google.common.base.Strings;
import com.lmc.backend.constant.JwtConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = extractRoles(userDetails);
        claims.put(JwtConstants.ROLES_CLAIM, roles);
        claims.put(JwtConstants.TOKEN_TYPE_TEXT, JwtConstants.ACCESS_TOKEN);
        return createToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.TOKEN_TYPE_TEXT, JwtConstants.REFRESH_TOKEN);
        return createToken(claims, userDetails.getUsername());
    }

    private List<String> extractRoles(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(JwtConstants.TOKEN_TYPE_TEXT, String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtConfig.getAccessExpiration()))
                .issuer(jwtConfig.getIssuer())
                .audience().add(jwtConfig.getAudience()).and()
                .signWith(getSecretKey())
                .compact();
    }

    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims != null ? claims.getSubject() : null;
        } catch (Exception e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .requireIssuer(jwtConfig.getIssuer())
                .requireAudience(jwtConfig.getAudience())
                .build().parseSignedClaims(token).getPayload();
    }

    public boolean isTokenValid(String token) {
        String claimType = extractTokenType(token);
        return validateToken(token) && (claimType.equals(JwtConstants.ACCESS_TOKEN) || claimType.equals(JwtConstants.REFRESH_TOKEN));
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .requireIssuer(jwtConfig.getIssuer())
                    .requireAudience(jwtConfig.getAudience())
                    .build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT expired: {}", e.getMessage());
        } catch (SecurityException e) {
            logger.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported JWT: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT token is null or empty");
        } catch (JwtException e) {
            logger.warn("JWT validation failed: {}", e.getMessage());
        }
        return false;
    }

    public long getExpirationTimeRemaining(String token) {
        try {
            Claims claims = extractAllClaims(token);
            long expirationMillis = claims.getExpiration().getTime() - System.currentTimeMillis();
            return expirationMillis / 1000;
        } catch (Exception e) {
            return -1;
        }
    }

    private SecretKey getSecretKey() {
        String sr = jwtConfig.getSecret();
        if (Strings.isNullOrEmpty(sr)) {
            sr = JwtConstants.DEFAULT_SECRET;
        }
        byte[] keyBytes = Base64.getDecoder().decode(sr);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
