package com.example.test.security.jwt;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtConfig {
    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${bezkoder.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    @Value("${bezkoder.app.appKey}")
    private String appKey;


    public String getAppKey() {
        return appKey;
    }

    public Key getSecret() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public char[] getSecretChar(){
        return jwtSecret.toCharArray();
    }
    public int getAccessTokenExpirationMs() {
        return jwtExpirationMs;
    }

    public int getRefreshTokenExpirationMs() {
        return jwtRefreshExpirationMs;
    }
}
