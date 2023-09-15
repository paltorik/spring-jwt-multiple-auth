package com.example.test.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.example.test.model.AccessToken;
import com.example.test.model.RefreshToken;
import com.example.test.security.UserDetailsImpl;
import com.example.test.security.dto.RefreshTokenDto;
import com.example.test.service.AccessTokenService;
import com.example.test.service.RefreshTokenService;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  private AccessToken accessToken;

  @Autowired
  private AccessTokenService accessTokenService;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired(required = true)
  private JwtConfig jwtConfig;

  public String generateAccessToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    accessToken = accessTokenService
        .save(accessTokenService.getNewToken(userPrincipal, jwtConfig.getAccessTokenExpirationMs()));

    return Jwts.builder()
        .setClaims(accessToken.generateClaims())
        .signWith(jwtConfig.getSecret(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken()
      throws JsonProcessingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
      InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

    RefreshTokenDto esTtoken = new RefreshTokenDto(
        refreshTokenService.getNewToken(accessToken, jwtConfig.getRefreshTokenExpirationMs()), accessToken);

    SecretKey secretKey = AESUtil.getKeySecretKey(jwtConfig.getSecretChar(), jwtConfig.getAppKey());

    return AESUtil.encrypt(esTtoken.toJson(), AESUtil.getIv(jwtConfig.getAppKey()), secretKey);
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(jwtConfig.getSecret()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
  public boolean validateJwtToken(AccessToken authToken){
      return accessTokenService.verifiactionToken(authToken);
  }

  public Optional<RefreshTokenDto> validationRefreshToken(String refreshTokenString) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonMappingException, JsonProcessingException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException  {

    SecretKey secretKey = AESUtil.getKeySecretKey(jwtConfig.getSecretChar(), jwtConfig.getAppKey());
    RefreshTokenDto refreshTokenDto = RefreshTokenDto.makeFromJson(AESUtil.decrypt(refreshTokenString,
        AESUtil.getIv(jwtConfig.getAppKey()), secretKey));

    if (!refreshTokenService.validationDto(refreshTokenDto)) {
      return Optional.empty();
    }

    return Optional.of(refreshTokenDto);
  }

  public void revokeAllToken(RefreshToken refreshToken) {
    accessTokenService.revoke(accessTokenService.findById(refreshToken.getAccessTokenId()));
    refreshTokenService.revoke(refreshToken);
  }
  public void revokeAllToken(AccessToken accessToken) {
    accessTokenService.revoke(accessToken);
    refreshTokenService.revokeByAccessToken(accessToken.getId());
  }


  public void revokeAllToken(RefreshTokenDto refreshTokenDto) {
    revokeAllToken(refreshTokenService.makeByDto(refreshTokenDto));
  }

  public String getUserIdFromJwtToken(String accessToken) {
    return Jwts.parserBuilder().setSigningKey(jwtConfig.getSecret()).build()
        .parseClaimsJws(accessToken).getBody().getSubject();
  }

  public AccessToken getAccessTokenFromJwtToken(String accessTokenJwt) {
    Claims claims = Jwts.parserBuilder().setSigningKey(jwtConfig.getSecret()).build().parseClaimsJws(accessTokenJwt)
        .getBody();

    AccessToken accessToken = new AccessToken(Long.parseLong(claims.getId()), Long.parseLong(claims.getSubject()),
        claims.getExpiration());

    return accessToken;

  }

}
