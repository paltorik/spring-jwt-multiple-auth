package com.example.test.model;

import java.io.Serializable;
import java.util.Date;



import com.example.test.security.dto.RefreshTokenDto;

import jakarta.persistence.*;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }


    @Column(name = "access_token_id")
    private Long accessTokenId;

    public Long getAccessTokenId() {
        return accessTokenId;
    }


    @Column(name = "expires_at")
    private Date expiresAt;


    public Date getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
    @Column(name = "revoke")
    private boolean revoke;

    public RefreshToken() {
    }
    public boolean isRevoke() {
        return revoke;
    }
    public void setRevoke(boolean revoke) {
        this.revoke = revoke;
    }
    public RefreshToken(AccessToken accessToken, Date expiresAt) {
        this.accessTokenId =accessToken.getId() ;
        this.expiresAt = expiresAt;
    }

    public RefreshToken(Long accessTokenIUuid, Date expiresAt) {
        this.accessTokenId =accessTokenIUuid;
        this.expiresAt = expiresAt;
    }
    public RefreshToken(RefreshTokenDto refreshTokenDto) {
        this.accessTokenId =refreshTokenDto.getAccessTokenUuid();
        this.expiresAt = refreshTokenDto.getExpiresAt();
        this.id=refreshTokenDto.getRefreshTokenUuid();
    }

}
