package com.lmc.backend.config.security;


import com.google.common.base.Strings;
import com.lmc.backend.constant.JwtConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = extractRoles(userDetails);
        claims.put(JwtConstants.ROLES_CLAIM, roles);
        return createToken(claims, userDetails.getUsername());
    }

    private List<String> extractRoles(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationTime)
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
                .build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims empty: {}", e.getMessage());
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
        String sr = secret;
        if (Strings.isNullOrEmpty(sr)) {
            sr = JwtConstants.DEFAULT_SECRET;
        }
        byte[] keyBytes = Base64.getDecoder().decode(sr);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
