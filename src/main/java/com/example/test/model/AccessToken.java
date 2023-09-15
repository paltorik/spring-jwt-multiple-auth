package com.example.test.model;

import java.io.Serializable;
import java.util.Date;



import com.example.test.security.UserDetailsJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import jakarta.persistence.*;

@Entity
@Table(name = "access_tokens")
public class AccessToken implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "expires_at")
    private Date expiresAt;

    @Column(name = "careted_at")
    private Date caretedAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "revoke")
    private boolean revoke;

    
    public AccessToken(UserDetailsJWT userDetailsJWT, Date expiresAt) {
        this(userDetailsJWT.getId(), expiresAt);
    }

    public AccessToken(Long userId, Date expiresAt) {
        this.userId = userId;
        this.expiresAt = expiresAt;
    }
    public AccessToken(Long id, Long userId, Date expiresAt) {
        this.id=id;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getCaretedAt() {
        return caretedAt;
    }

    public boolean isRevoke() {
        return revoke;
    }

    public void setRevoke(boolean revoke) {
        this.revoke = revoke;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Claims generateClaims() {
        return Jwts.claims()
                .setExpiration(expiresAt)
                .setId(id.toString())
                .setIssuedAt(new Date())
                .setSubject(userId.toString());
    }

   


    public AccessToken() {
    }

}
