package com.akjae.web3discussion.controller;

import com.akjae.web3discussion.dto.request.LoginRequest;
import com.akjae.web3discussion.dto.request.RegisterRequest;
import com.akjae.web3discussion.dto.response.ApiResponse;
import com.akjae.web3discussion.dto.response.AuthResponse;
import com.akjae.web3discussion.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("회원가입이 완료되었습니다", authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("로그인이 완료되었습니다", authService.login(request)));
    }
}
