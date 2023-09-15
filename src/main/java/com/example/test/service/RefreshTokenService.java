package com.example.test.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.test.model.AccessToken;
import com.example.test.model.RefreshToken;
import com.example.test.repository.RefreshTokenRepository;
import com.example.test.security.dto.RefreshTokenDto;

@Service
public class RefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepostiry;

    public RefreshToken getNewToken(AccessToken accessToken, int jwtExpirationMs) {
        RefreshToken refreshToken = new RefreshToken(accessToken, new Date((new Date()).getTime() + jwtExpirationMs));
        return refreshTokenRepostiry.save(refreshToken);
    }

    public boolean verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().compareTo(new Date()) < 0) {
            return false;
        }
        return true;
    }

    public RefreshToken revoke(RefreshToken refreshToken) {
        refreshToken.setRevoke(true);
        return refreshTokenRepostiry.save(refreshToken);
    }

    public RefreshToken makeByDto(RefreshTokenDto refreshTokenDto) {
        return new RefreshToken(refreshTokenDto);
    }

    public boolean exist(RefreshToken refreshToken) {
        return refreshTokenRepostiry.existsById(refreshToken.getId());
    }

    public boolean validationDto(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken =refreshTokenRepostiry.findById(refreshTokenDto.getRefreshTokenUuid()).orElseThrow();
        return refreshToken.isRevoke() 
            && verifyExpiration(refreshToken)
            && verifyExpiration(makeByDto(refreshTokenDto));
    }

    public RefreshToken findById(Long idLong) {
        return refreshTokenRepostiry.findById(idLong).orElseThrow();
    }

    public void revokeByAccessToken(Long idLong) {
        refreshTokenRepostiry.updateRevokeByAccessToken(idLong);
    }

}
