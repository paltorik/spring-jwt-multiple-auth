package com.example.test.security.jwt;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.test.security.PhoneNumberAuthenticationToken;
import com.example.test.service.CodeValidationImpl;
import com.example.test.service.UserDetailsServiceImpl;

public class PhoneNumberAuthenticationProvider implements AuthenticationProvider {

    // @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public UserDetailsServiceImpl getUserDetailsServiceImpl() {
        return userDetailsServiceImpl;
    }

    public PhoneNumberAuthenticationProvider setUserDetailsServiceImpl(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        return this;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final PhoneNumberAuthenticationToken phoneAuthenticationToken = (PhoneNumberAuthenticationToken) authentication;
        
        if (!CodeValidationImpl.validation(phoneAuthenticationToken.getPrincipal().toString(), phoneAuthenticationToken.getCredentials().toString())){
            throw new BadCredentialsException("Bad credentials");
        }
        
        UserDetails user = userDetailsServiceImpl.LoadUserByPhone(phoneAuthenticationToken.getPrincipal().toString());
        
        return new PhoneNumberAuthenticationToken(user,true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PhoneNumberAuthenticationToken.class);
    }
    
}
