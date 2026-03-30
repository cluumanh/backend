package com.lmc.backend.controller;

import com.lmc.backend.common.ApiResponseFactory;
import com.lmc.backend.constant.AdminPaths;
import com.lmc.backend.constant.AuthPaths;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.constant.MessageConstants;
import com.lmc.backend.dto.UserDto;
import com.lmc.backend.dto.UserFilter;
import com.lmc.backend.dto.request.PageRequest;
import com.lmc.backend.dto.response.PageResponse;
import com.lmc.backend.exception.BusinessException;
import com.lmc.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AdminPaths.ROOT)
@Validated
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @GetMapping(AdminPaths.USERS)
    public ResponseEntity<?> getUsers(@Valid @RequestBody PageRequest<UserFilter> pageRequest) {
        if (pageRequest == null)
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Invalid request");
        PageResponse<UserDto> users = userService.findUsers(pageRequest);
        if (users != null) {
            return ResponseEntity.ok(ApiResponseFactory.success(ErrorCode.SUCCESS, MessageConstants.SUCCESS, users));
        }
        throw new BusinessException(ErrorCode.UNAUTHORIZED, MessageConstants.GET_USERS_FAILED);
    }
}
