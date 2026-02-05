package com.lmc.backend.value;

import com.google.common.base.Strings;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.exception.BusinessException;

public record ClientInfo(
        String deviceId,
        String clientId,
        String ipAddress,
        String userAgent) {

    public static ClientInfo of(
            String deviceId,
            String clientId,
            String ipAddress,
            String userAgent
    ) {
        if (Strings.isNullOrEmpty(deviceId)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "deviceId is required");
        }
        return new ClientInfo(deviceId, clientId, ipAddress, userAgent);
    }
}
