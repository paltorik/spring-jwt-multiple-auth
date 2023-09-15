package com.example.test.request.auth;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
