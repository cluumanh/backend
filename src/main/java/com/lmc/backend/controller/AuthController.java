package com.lmc.backend.controller;

import com.lmc.backend.config.security.JwtUtil;
import com.lmc.backend.constant.ApiPaths;
import com.lmc.backend.constant.HttpResponseConstants;
import com.lmc.backend.dto.*;
import com.lmc.backend.enity.User;
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
//@RequestMapping(ApiPaths.AUTH_BASE)
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

    @PostMapping(ApiPaths.AUTH_LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        logger.info("LOGIN ENDPOINT CALLED");
        try {
            UserDetails user = authenticateUser(request);
            String token = jwtUtil.generateToken(user);

            logger.info("Login successful for user{}", request.getUsername());

            return ResponseEntity.ok(buildLoginResponse(user, token));
        } catch (BadCredentialsException e) {
            logger.warn("Login failed for user: {}", request.getUsername());
            return buildUnauthorizedResponse("Invalid username or password");
        }
    }

    @PostMapping(ApiPaths.AUTH_REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            boolean isRegisterUser = userService.register(registerRequest);
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("");
        }
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


    @GetMapping(ApiPaths.PUBLIC_HEALTH)
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new SuccessResponse("API is running", System.currentTimeMillis()));
    }
}
