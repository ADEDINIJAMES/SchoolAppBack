package com.tumtech.schoolApp.service;


import com.tumtech.schoolApp.dto.LoginRequest;
import com.tumtech.schoolApp.model.Users;
import com.tumtech.schoolApp.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService extends UserDetailsService {
    ApiResponse login (LoginRequest loginRequest);
    ApiResponse adminLogin (LoginRequest loginRequest);
    ApiResponse adminRegistration (MultipartFile file, Authentication authentication);
    ApiResponse logout(Authentication authentication, HttpServletRequest request) ;
//    ApiResponse RegisterUser(UserDto userDto, Users user);

    }
