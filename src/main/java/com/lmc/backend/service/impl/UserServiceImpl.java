package com.lmc.backend.service.impl;

import com.google.common.base.Strings;
import com.lmc.backend.common.PageableMapper;
import com.lmc.backend.config.UserQueryConfig;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.constant.MessageConstants;
import com.lmc.backend.dto.TokenPair;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.dto.UserFilter;
import com.lmc.backend.dto.request.PageRequest;
import com.lmc.backend.dto.request.RegisterRequest;
import com.lmc.backend.dto.response.LoginResponse;
import com.lmc.backend.dto.response.PageResponse;
import com.lmc.backend.entity.User;
import com.lmc.backend.exception.BusinessException;
import com.lmc.backend.filter.UserSpecification;
import com.lmc.backend.mapper.UserMapper;
import com.lmc.backend.repository.UserRepository;
import com.lmc.backend.service.TokenManager;
import com.lmc.backend.service.UserService;
import com.lmc.backend.value.ClientInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserDto> implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenManager tokenManager;

    protected UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, TokenManager tokenManager) {
        super(userRepository);
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
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
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(userMapper::toDto).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + username)
        );
    }

    @Transactional
    @Override
    public boolean register(RegisterRequest registerRequest) {
        if (registerRequest != null) {
            boolean userExisted = userRepository.existsByUsername(registerRequest.getUsername());
            if (userExisted) {
                throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS, "User already exists");
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
    public LoginResponse login(UserDto userDto, HttpServletRequest httpServletRequest) {
        if (userDto == null)
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, MessageConstants.USER_NOT_FOUND);

        ClientInfo clientInfo = extractClientInfo(httpServletRequest);

        Optional<UserDto> user = Optional.of(findByUserName(userDto.getUsername()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, MessageConstants.USER_NOT_FOUND)));

        TokenPair tokenPair = tokenManager.createTokenPair(userMapper.toEntity(user.get()), clientInfo);

        if (tokenPair != null) {
            return LoginResponse.builder()
                    .accessToken(tokenPair.getAccessToken())
                    .refreshToken(tokenPair.getRefreshToken())
                    .expiresIn(tokenPair.getAccessTokenExpiresIn())
                    .username(user.get().getUsername())
                    .roles(new ArrayList<>(user.get().getRoles()))
                    .build();
        }
        return null;
    }

    private ClientInfo extractClientInfo(HttpServletRequest httpServletRequest) {
        String deviceId = httpServletRequest.getHeader("X-Device-Id");
        String clientId = httpServletRequest.getHeader("X-Client-Id");
        String userAgent = httpServletRequest.getHeader("User-Agent");
        String ipAddress = extractIp(httpServletRequest);

        return ClientInfo.of(deviceId, clientId, ipAddress, userAgent);
    }

    private static String extractIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (!Strings.isNullOrEmpty(xff)) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByUserName(String userName) {
        if (Strings.isNullOrEmpty(userName))
            return Optional.empty();
        Optional<User> user = userRepository.findByUsername(userName);
        return user.map(userMapper::toDto);
    }

    @Override
    public PageResponse<UserDto> findUsers(PageRequest<UserFilter> pageRequest) {
        Pageable pageable = PageableMapper.toPageable(
                pageRequest.getPage(),
                pageRequest.getSize(),
                pageRequest.getSort(),
                UserQueryConfig.USER_ALLOWED,
                UserQueryConfig.USER_DEFAULT,
                100
        );

        Page<User> page = userRepository.findAll(
                new UserSpecification(pageRequest.getFilters()),
                pageable
        );

        Page<UserDto> dtoPage = page.map(userMapper::toDto);
        return PageResponse.from(dtoPage);
    }
}
