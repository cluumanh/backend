package com.lmc.backend.exception;

import com.amazonaws.internal.ExceptionUtils;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BaseException ex,
            HttpServletRequest request) {

        logger.error("Error [{}] at {}",
                ex.getErrorCode(),
                ExceptionUtils.exceptionStackTrace(ex));

        ErrorCode code = ex.getErrorCode();
        String message = ex.getMessage();

        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .status(code.getStatus())
                        .code(code.name())
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception ex,
            HttpServletRequest request) {

        logger.error("Exception {}",
                ExceptionUtils.exceptionStackTrace(ex));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .status(500)
                        .code(ErrorCode.INTERNAL_ERROR.name())
                        .message("Something went wrong")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build());
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(
            Exception ex,
            HttpServletRequest request) {

        logger.error("UsernameNotFoundException {}",
                ExceptionUtils.exceptionStackTrace(ex));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .status(ErrorCode.USER_NOT_FOUND.getStatus())
                        .code(ErrorCode.USER_NOT_FOUND.name())
                        .message("User not found")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(
            Exception ex,
            HttpServletRequest request) {

        logger.error("BadCredentialsException {}",
                ExceptionUtils.exceptionStackTrace(ex));

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .status(ErrorCode.UNAUTHORIZED.getStatus())
                        .code(ErrorCode.UNAUTHORIZED.name())
                        .message("invalid username or password")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        logger.error(ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .status(400)
                        .code(ErrorCode.INVALID_REQUEST.name())
                        .message("Validation failed")
                        .data(errors)
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build());
    }

}
