package com.lmc.backend.controller;


import com.lmc.backend.constant.ApiPaths;
import com.lmc.backend.constant.ErrorCode;
import com.lmc.backend.constant.PublicPaths;
import com.lmc.backend.dto.SuccessResponse;
import com.lmc.backend.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PublicPaths.ROOT)
public class PublicController {
    @GetMapping(PublicPaths.HEALTH)
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new SuccessResponse("API is running", System.currentTimeMillis()));
    }

    @GetMapping("/debug")
    public ResponseEntity<?> debug() {
        throw new BusinessException(ErrorCode.FORBIDDEN);
    }
}
