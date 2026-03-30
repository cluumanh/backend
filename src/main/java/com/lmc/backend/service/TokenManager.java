package com.lmc.backend.service;

import com.lmc.backend.dto.TokenPair;
import com.lmc.backend.entity.User;
import com.lmc.backend.value.ClientInfo;

public interface TokenManager {
    TokenPair createTokenPair(User user, ClientInfo clientInfo);

    TokenPair refreshAccessToken(String refreshToken);

    void revokeRefreshToken(String refreshToken);

    void revokeAllUserTokens(User user);
}
