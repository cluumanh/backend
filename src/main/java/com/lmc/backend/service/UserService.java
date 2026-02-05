package com.lmc.backend.service;

import com.lmc.backend.dto.request.LoginRequest;
import com.lmc.backend.dto.request.RegisterRequest;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.dto.response.LoginResponse;
import com.lmc.backend.enity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService extends BaseService<User, Long, UserDto>, UserDetailsService {
    boolean register(RegisterRequest registerRequest);
    LoginResponse login(UserDto userDto, HttpServletRequest httpServletRequest);
    Optional<User> findByUserName(String userName);
}
