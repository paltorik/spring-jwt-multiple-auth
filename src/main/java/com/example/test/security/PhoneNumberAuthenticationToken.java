package com.example.test.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class PhoneNumberAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

	private Object credentials;

    public PhoneNumberAuthenticationToken(String number, String code) {
		super(null);
		this.principal = number;
		this.credentials = code;
		setAuthenticated(false);
	}

    
    
    
    public PhoneNumberAuthenticationToken(UserDetails userDetails, boolean isAuthenticated) {
		super(userDetails.getAuthorities());
		setAuthenticated(isAuthenticated);
		this.principal = userDetails;
	}

    @Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

    
}