package com.example.test.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.test.security.AuthEntryPointJwt;
import com.example.test.security.JwtAuthTokenFilter;
import com.example.test.security.jwt.JwtUtils;
import com.example.test.security.jwt.PhoneNumberAuthenticationProvider;
import com.example.test.security.jwt.RefreshTokenAuthenticationProvider;
import com.example.test.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired 
    JwtUtils jwtUtils;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    public DaoAuthenticationProvider authenticationProviderDao() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public PhoneNumberAuthenticationProvider authenticationProviderPhone() {
        return new PhoneNumberAuthenticationProvider().setUserDetailsServiceImpl(userDetailsService);
    }

    public RefreshTokenAuthenticationProvider authenticationProviderRefresh() {
        return new RefreshTokenAuthenticationProvider().setUserDetailsServiceImpl(userDetailsService).setJwtUtils(jwtUtils);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/*").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .anyRequest().authenticated());

        // //http.authenticationProvider(authenticationProviderDao());
        // http.authenticationProvider(authenticationProviderPhone());
        // http.authenticationProvider(authenticationProviderRefresh());

        // http.addFilter(authenticationJwtTokenFilter());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Autowired
    void registerProvider(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProviderPhone())
                .authenticationProvider(authenticationProviderDao())
                .authenticationProvider(authenticationProviderRefresh());
    }
}
