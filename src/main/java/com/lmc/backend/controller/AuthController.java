package com.lmc.backend.controller;

import com.lmc.backend.common.ApiResponseFactory;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.constant.MessageConstants;
import com.lmc.backend.constant.UserPaths;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.dto.request.LoginRequest;
import com.lmc.backend.dto.request.RegisterRequest;
import com.lmc.backend.dto.response.LoginResponse;
import com.lmc.backend.exception.BusinessException;
import com.lmc.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(UserPaths.ROOT)
@Validated
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @PostMapping(UserPaths.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        logger.info("LOGIN ENDPOINT CALLED");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDto userDto = (UserDto) authentication.getPrincipal();

        LoginResponse loginResponse = userService.login(userDto, httpRequest);

        if (loginResponse != null) {
            logger.info("Login successful for user{}", request.getUsername());
            return ResponseEntity.ok(ApiResponseFactory.success(ErrorCode.SUCCESS, MessageConstants.SUCCESS, loginResponse));
        }
        throw new BusinessException(ErrorCode.UNAUTHORIZED, MessageConstants.LOGIN_FAILED);
    }

    @PostMapping(UserPaths.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.register(registerRequest)) {
            return ResponseEntity.ok(ApiResponseFactory.success(ErrorCode.CREATED, MessageConstants.SUCCESS));
        }
        throw new BusinessException(ErrorCode.INVALID_REQUEST, "Invalid request");
    }
}
