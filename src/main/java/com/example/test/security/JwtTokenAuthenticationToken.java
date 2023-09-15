package com.example.test.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtTokenAuthenticationToken extends AbstractAuthenticationToken {

    public JwtTokenAuthenticationToken(UserDetails userDetails, boolean isAuthenticated) {
		super(userDetails.getAuthorities());
		setAuthenticated(isAuthenticated);
		this.principal = userDetails;
	}
	public JwtTokenAuthenticationToken(String refreshToken) {
		super(null);
		this.credentials = refreshToken;
		this.principal=null;
		setAuthenticated(false);
	}

	private final Object principal;

	private Object credentials;

    
    @Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

    
}