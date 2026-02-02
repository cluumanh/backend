package com.lmc.backend.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private String secret;
    private long accessExpiration;
    private long refreshExpiration;
    private String issuer;
    private String audience;
}
