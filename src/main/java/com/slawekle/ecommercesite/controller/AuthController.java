package com.slawekle.ecommercesite.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slawekle.ecommercesite.request.LoginRequest;
import com.slawekle.ecommercesite.security.jwt.JwtUtils;
import com.slawekle.ecommercesite.security.user.ShopUserDetailsService;
import com.slawekle.ecommercesite.utils.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final ShopUserDetailsService shopUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @Value("${auth.token.refreshExpirationInMils}")
    private Long refreshTokenExpirationTime;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String accessToken = jwtUtils.generateAccessTokenForUser(authentication);
        String refreshToken = jwtUtils.generateRefreshTokenForUser(loginRequest.getEmail());
        cookieUtils.addRefreshTokenCookie(response, refreshToken, refreshTokenExpirationTime);
        Map<String, String> token = new HashMap<>();
        token.put("accessToken", accessToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        cookieUtils.logCookies(request);
        String refreshToken = cookieUtils.getRefreshTokenFromCookies(request);
        if (refreshToken != null) {
            boolean isValid = jwtUtils.validateToken(refreshToken);
            if (isValid) {
                String usernameFromToken = jwtUtils.getUserNameFromToken(refreshToken);
                UserDetails userDetails = shopUserDetailsService.loadUserByUsername(usernameFromToken);
                String newAccessToken = jwtUtils.generateAccessTokenForUser(new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()));

                if (newAccessToken != null) {
                    Map<String, String> token = new HashMap<>();
                    token.put("accessToken", newAccessToken);
                    return ResponseEntity.ok(token);
                } else {
                    return ResponseEntity.status(500).body("Failed to generate new access token");
                }
            } 
        }        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or missing refresh token");
    }
}
