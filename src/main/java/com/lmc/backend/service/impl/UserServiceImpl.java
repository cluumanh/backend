package com.lmc.backend.service.impl;

import com.google.common.base.Strings;
import com.lmc.backend.constant.Role;
import com.lmc.backend.dto.RegisterRequest;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.enity.User;
import com.lmc.backend.mapper.UserMapper;
import com.lmc.backend.repository.BaseRepository;
import com.lmc.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserDto> implements UserService, UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    protected UserServiceImpl(BaseRepository<User, Long> repository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        super(repository);
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected UserDto mapToResponse(User entity) {
        return null;
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
            User user = new User();
            user.setUserName(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setEmail(registerRequest.getEmail());
            user.setRoles(Arrays.stream(registerRequest.getEmail().split(","))
                        .filter(e -> !e.isEmpty())
                        .map(String::toUpperCase)
                        .map(Role::valueOf).collect(Collectors.toCollection(HashSet::new)));

            return save(user) != null;
        }
        return false;
    }
}
