package com.lmc.backend.service.impl;

import com.google.common.base.Strings;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.constant.Role;
import com.lmc.backend.controller.AuthController;
import com.lmc.backend.dto.RegisterRequest;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.enity.User;
import com.lmc.backend.exception.BusinessException;
import com.lmc.backend.mapper.UserMapper;
import com.lmc.backend.repository.UserRepository;
import com.lmc.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserDto> implements UserService, UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    protected UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    protected UserDto mapToResponse(User entity) {
        return UserDto.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .roles(entity.getRoles())
                .build();
    }

    @Override
    protected String entityName() {
        return "User";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Transactional
    @Override
    public boolean register(RegisterRequest registerRequest) {
        if (registerRequest != null) {
            boolean userExisted = userRepository.existsByUsername(registerRequest.getUsername());
            if (userExisted) {
                throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
            }
            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .email(registerRequest.getEmail())
                    .roles(new HashSet<>(registerRequest.getRoles()))
                    .build();

            return save(user) != null;
        }
        return false;
    }

    @Override
    public UserDto findByUserName(String userName) {
        if (Strings.isNullOrEmpty(userName))
            return null;
        User user = userRepository.findByUsername(userName);
        if (user != null) {
            return userMapper.toDto(user);
        }
        return null;
    }
}
