package com.example.test.security.dto;

import java.util.Date;


import com.example.test.model.AccessToken;
import com.example.test.model.RefreshToken;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefreshTokenDto {
    @JsonProperty("refreshTokenUuid")
    private Long refreshTokenUuid;

    @JsonProperty("accessTokenUuid")
    private Long accessTokenUuid;

    @JsonProperty("expiresAt")
    private Date expiresAt;

    @JsonProperty("userId")
    private Long userId;

    

    public RefreshTokenDto() {
    }

    public RefreshTokenDto(Long refreshTokenUuid, Long accessTokenUuid, Date expiresAt, Long userId) {
        this.refreshTokenUuid = refreshTokenUuid;
        this.accessTokenUuid = accessTokenUuid;
        this.expiresAt = expiresAt;
        this.userId = userId;
    }

    public RefreshTokenDto(RefreshToken refreshToken, Long userId) {
        this.refreshTokenUuid = refreshToken.getId();
        this.accessTokenUuid = refreshToken.getAccessTokenId();
        this.expiresAt = refreshToken.getExpiresAt();
        this.userId = userId;
    }

    public RefreshTokenDto(RefreshToken refreshToken, AccessToken accessToken) {
        this.refreshTokenUuid = refreshToken.getId();
        this.accessTokenUuid = refreshToken.getAccessTokenId();
        this.expiresAt = refreshToken.getExpiresAt();
        this.userId = accessToken.getUserId();
    }

    public String toJson() throws JsonProcessingException {
        return (new ObjectMapper()).writeValueAsString(this);
    }
    public static RefreshTokenDto makeFromJson(String refreshToken) throws JsonMappingException, JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        
        return objectMapper.readValue(refreshToken, RefreshTokenDto.class);
    }

    public boolean validationExpiresAt(){
        return this.getExpiresAt().compareTo(new Date()) < 0;
    }
     public Long getRefreshTokenUuid() {
        return refreshTokenUuid;
    }

    public void setRefreshTokenUuid(Long refreshTokenUuid) {
        this.refreshTokenUuid = refreshTokenUuid;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
     public Long getAccessTokenUuid() {
        return accessTokenUuid;
    }

    public void setAccessTokenUuid(Long accessTokenUuid) {
        this.accessTokenUuid = accessTokenUuid;
    }
}