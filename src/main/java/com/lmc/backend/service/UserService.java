package com.lmc.backend.service;

import com.lmc.backend.dto.RegisterRequest;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.enity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends BaseService<User, Long, UserDto> {
    boolean register(RegisterRequest registerRequest);
}
