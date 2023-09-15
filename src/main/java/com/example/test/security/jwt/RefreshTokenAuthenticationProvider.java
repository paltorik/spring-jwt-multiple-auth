package com.example.test.security.jwt;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.test.security.JwtTokenAuthenticationToken;
import com.example.test.security.dto.RefreshTokenDto;
import com.example.test.service.UserDetailsServiceImpl;

public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {

    // @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public UserDetailsServiceImpl getUserDetailsServiceImpl() {
        return userDetailsServiceImpl;
    }

    public RefreshTokenAuthenticationProvider setUserDetailsServiceImpl(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        return this;
    }
    private JwtUtils jwtUtils;

    public RefreshTokenAuthenticationProvider setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        return   this;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        if (authentication.isAuthenticated()){
            return authentication;
        }
        
        final JwtTokenAuthenticationToken jwtTokenAuthenticationToken = (JwtTokenAuthenticationToken) authentication;
        try {

            
            String refString=jwtTokenAuthenticationToken.getCredentials().toString();
            RefreshTokenDto refreshToken = jwtUtils.validationRefreshToken(refString).orElseThrow();
            UserDetails user = userDetailsServiceImpl.LoadUserById(refreshToken.getUserId());
            jwtUtils.revokeAllToken(refreshToken);
            return new JwtTokenAuthenticationToken(user, true);
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtTokenAuthenticationToken.class);
    }
}