package com.lmc.backend.service;

import com.lmc.backend.dto.UserFilter;
import com.lmc.backend.dto.request.PageRequest;
import com.lmc.backend.dto.request.RegisterRequest;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.dto.response.LoginResponse;
import com.lmc.backend.dto.response.PageResponse;
import com.lmc.backend.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService extends BaseService<User, Long, UserDto>, UserDetailsService {
    boolean register(RegisterRequest registerRequest);
    LoginResponse login(UserDto userDto, HttpServletRequest httpServletRequest);
    Optional<UserDto> findByUserName(String userName);
    PageResponse<UserDto> findUsers(PageRequest<UserFilter> pageRequest);
}
