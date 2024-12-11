package com.tumtech.schoolApp.controller;

import com.tumtech.schoolApp.dto.LoginRequest;
import com.tumtech.schoolApp.response.ApiResponse;
import com.tumtech.schoolApp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/v1/auth")
@CrossOrigin (allowCredentials = "true", origins = "http://localhost:5173")
public class AuthController {
    private final AuthService authService;

@Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> adminLogin (@Valid  @RequestBody LoginRequest loginRequest){
    ApiResponse response = authService.login(loginRequest);
    return ResponseEntity.status(response.getStatusCode()).body(response);
    }
@PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout (Authentication authentication, HttpServletRequest request){
    ApiResponse response = authService.logout(authentication, request);
    return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
