package com.lmc.backend.dto;

import com.lmc.backend.constant.JwtConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPair {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long accessTokenExpiresIn;
    private Long refreshTokenExpiresIn;

    public static TokenPair of(
            String accessToken,
            String refreshToken,
            Long accessExpiration,
            Long refreshExpiration) {

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(JwtConstants.BEARER_PREFIX)
                .accessTokenExpiresIn(accessExpiration)
                .refreshTokenExpiresIn(refreshExpiration)
                .build();
    }
}
