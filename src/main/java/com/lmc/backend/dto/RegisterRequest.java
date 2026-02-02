package com.lmc.backend.dto;

import com.lmc.backend.constant.Role;
import com.lmc.backend.constant.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = ValidationConstants.USERNAME_REQUIRED)
    @Size(min = ValidationConstants.MIN_USERNAME_LENGTH,
            message = ValidationConstants.USERNAME_MIN)
    private String username;

    @NotBlank(message = ValidationConstants.PASSWORD_REQUIRED)
    @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH,
            message = ValidationConstants.PASSWORD_MIN)
    private String password;

    @NotBlank(message = ValidationConstants.EMAIL_REQUIRED)
    @Email(message = ValidationConstants.EMAIL_INVALID)
    private String email;

    @NotEmpty(message = ValidationConstants.ROLE_REQUIRED)
    private List<@NotNull Role> roles;
}
