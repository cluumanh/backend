package com.lmc.backend.controller;

import com.lmc.backend.common.ApiResponseFactory;
import com.lmc.backend.config.security.JwtUtil;
import com.lmc.backend.constant.ApiPaths;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.constant.HttpResponseConstants;
import com.lmc.backend.constant.UserPaths;
import com.lmc.backend.dto.*;
import com.lmc.backend.exception.BusinessException;
import com.lmc.backend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UserPaths.ROOT)
@Validated
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(UserPaths.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        logger.info("LOGIN ENDPOINT CALLED");
        try {
            UserDetails user = authenticateUser(request);
            String token = jwtUtil.generateAccessToken(user);

            logger.info("Login successful for user{}", request.getUsername());

            return ResponseEntity.ok(buildLoginResponse(user, token));
        } catch (BadCredentialsException e) {
            logger.warn("Login failed for user: {}", request.getUsername());
            return buildUnauthorizedResponse("Invalid username or password");
        }
    }

    @PostMapping(UserPaths.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.register(registerRequest)) {
            return ResponseEntity.ok(ApiResponseFactory.success(ErrorCode.CREATED));
        }
        throw new BusinessException(ErrorCode.INVALID_REQUEST);
    }

    private UserDetails authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        return (UserDetails) authentication.getPrincipal();
    }

    private LoginResponse buildLoginResponse(UserDetails userDetails, String token) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        long expiresIn = jwtUtil.getExpirationTimeRemaining(token);

        return new LoginResponse(token, userDetails.getUsername(), roles, expiresIn);
    }

    private ResponseEntity<?> buildUnauthorizedResponse(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        HttpResponseConstants.UNAUTHORIZED,
                        message,
                        System.currentTimeMillis()
                ));
    }
}
