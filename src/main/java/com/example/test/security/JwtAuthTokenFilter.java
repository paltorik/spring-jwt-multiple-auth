package com.example.test.security;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.test.model.AccessToken;
import com.example.test.security.jwt.JwtUtils;
import com.example.test.service.UserDetailsServiceImpl;

import org.slf4j.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = obtainBreareToken(request);
            if (token != null && jwtUtils.validateJwtToken(token)) {

                AccessToken accessToken= jwtUtils.getAccessTokenFromJwtToken(token);
                validationAccessToken(accessToken);
                
                UserDetails userDetails = userDetailsService.LoadUserById(accessToken.getUserId());

                JwtTokenAuthenticationToken authentication = new JwtTokenAuthenticationToken(userDetails, true);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String obtainBreareToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    private void validationAccessToken(AccessToken  accessToken){
        if(!jwtUtils.validateJwtToken(accessToken)){
            jwtUtils.revokeAllToken(accessToken);
            throw new BadCredentialsException("Bad credentials");
        }
    }
}