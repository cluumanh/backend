package com.lmc.backend.dto;

import com.lmc.backend.constant.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = ValidationConstants.USERNAME_REQUIRED)
    @Size(min = ValidationConstants.MIN_USERNAME_LENGTH,
            message = ValidationConstants.USERNAME_MIN)
    private String username;

    @NotBlank(message = ValidationConstants.PASSWORD_REQUIRED)
    @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH,
            message = ValidationConstants.PASSWORD_MIN)
    private String password;
}
