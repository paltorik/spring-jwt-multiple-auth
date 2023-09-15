package com.example.test.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.test.model.AccessToken;
//import com.example.test.model.RefreshToken;
import com.example.test.repository.AccessTokenRepository;
import com.example.test.security.UserDetailsJWT;

@Service
public class AccessTokenService {

    @Autowired(required = true)
    private AccessTokenRepository accessTokenRepository;

    public AccessToken getNewToken(UserDetailsJWT userPrincipa, int jwtExpirationMs) {
        return new AccessToken(userPrincipa, new Date((new Date()).getTime() + jwtExpirationMs));
    }

    public AccessToken save(AccessToken accessToken) {
        return accessTokenRepository.save(accessToken);
    }

    public AccessToken revoke(AccessToken accessToken) {
        accessToken.setRevoke(true);
        return accessTokenRepository.save(accessToken);
    }

    public boolean isRevoke(AccessToken accessToken) {
        return accessTokenRepository.findById(accessToken.getId()).orElseThrow().isRevoke();
    }

    public boolean verifyExpiration(AccessToken token) {
        if (token.getExpiresAt().compareTo(new Date()) < 0) {
            return false;
        }
        return true;
    }

    public boolean verifiactionToken(AccessToken accessToken) {
        AccessToken originalAccessToken = accessTokenRepository.findById(accessToken.getId()).orElseThrow();
        if (originalAccessToken.isRevoke()
                || accessToken.getUserId() != originalAccessToken.getUserId()
                || !verifyExpiration(originalAccessToken)) {
            return false;
        }
        return true;
    }

    public AccessToken findById(Long idLong){
            return accessTokenRepository.findById(idLong).orElseThrow();
    }
}