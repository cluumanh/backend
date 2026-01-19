package com.lmc.backend.filter;

import com.google.common.base.Strings;
import com.lmc.backend.config.security.JwtUtil;
import com.lmc.backend.constant.ApiPaths;
import com.lmc.backend.constant.JwtConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        boolean isPublic = Arrays.stream(ApiPaths.PUBLIC_PATHS)
                .anyMatch(publicPath -> {
                    if (publicPath.endsWith("/**")) {
                        return path.startsWith(publicPath.replace("/**", "/"));
                    }
                    return path.equals(publicPath);
                });

        if (isPublic) {
            logger.debug("Skipping JWT filter for public path: {}", path);
        }

        return isPublic;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);

            if (isValidToken(token)) {
                authenticateUser(token, request);
            }
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(JwtConstants.BEARER_PREFIX)) {
            return authHeader.substring(JwtConstants.BEARER_PREFIX_LENGTH);
        }
        return null;
    }

    private boolean isValidToken(String token) {
        return !Strings.isNullOrEmpty(token) && jwtUtil.validateToken(token);
    }

    private void authenticateUser(String token, HttpServletRequest request) {
        String username = jwtUtil.extractUsername(token);

        if (username != null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("User authenticated: {}", username);
        }
    }
}
