package com.tumtech.schoolApp.util;

import com.tumtech.schoolApp.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil utils;
    private final AuthService authService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil utils, @Lazy AuthService authServiceImp) {
        this.utils = utils;
        this.authService = authServiceImp;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        String authorizationHeader = null;
        String username = null;
        UserDetails userDetail = null;

        authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
//            log.debug("Token to be parsed: {}", token);
            token = authorizationHeader.substring(7);
            username = utils.extractUsername.apply(token);

        }


        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            userDetail = authService.loadUserByUsername(username);

            if(userDetail != null && utils.isTokenValid.apply(token, userDetail.getUsername())){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
