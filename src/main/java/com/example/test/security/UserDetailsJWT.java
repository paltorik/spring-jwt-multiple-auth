package com.example.test.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsJWT extends UserDetails {
    public String getPhone();
    public Long getId();
    public String getEmail();
}
