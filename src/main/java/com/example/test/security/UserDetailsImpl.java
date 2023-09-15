package com.example.test.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.example.test.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetailsJWT {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private String phone;
    private Collection<? extends GrantedAuthority> authorities; // Роли пользователя

    public UserDetailsImpl(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public UserDetailsImpl(Long id, String username, String email, String password, String phone,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.authorities = authorities;
    }

    public UserDetailsImpl(Long id, String username, String email, String password, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getPhone() {
        return phone;
    }
    
    @Override
    public String getEmail() {
        return email;
    }
}