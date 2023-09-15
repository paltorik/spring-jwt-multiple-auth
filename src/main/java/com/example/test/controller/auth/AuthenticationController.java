package com.example.test.controller.auth;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.model.User;
import com.example.test.repository.UserRepository;
import com.example.test.request.auth.LoginRequest;
import com.example.test.request.auth.LoginValidationService;
import com.example.test.request.auth.RefreshTokenRequest;
import com.example.test.request.auth.RegistrationRequest;
import com.example.test.security.JwtTokenAuthenticationToken;
import com.example.test.security.PhoneNumberAuthenticationToken;
import com.example.test.security.dto.JwtResponse;
import com.example.test.security.jwt.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        UserRepository userRepository;

        @Autowired
        private LoginValidationService loginValidationService;

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
                        throws JsonProcessingException, InvalidKeyException, NoSuchPaddingException,
                        NoSuchAlgorithmException,
                        InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException,
                        InvalidKeySpecException {

                Authentication authentication;

                if ("password".equals(loginRequest.getProvider())) {
                        loginValidationService.validateForPassword(loginRequest);
                        authentication = authenticationManager
                                        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                        loginRequest.getPassword()));

                } else if ("sms".equals(loginRequest.getProvider())) {
                        loginValidationService.validateForSms(loginRequest);
                        authentication = authenticationManager
                                        .authenticate(new PhoneNumberAuthenticationToken(loginRequest.getPhone(),
                                                        loginRequest.getCode()));
                } else {
                        return ResponseEntity.badRequest().body("Unsupported provider");
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);

                return ResponseEntity
                                .ok(new JwtResponse(jwtUtils.generateAccessToken(authentication),
                                                jwtUtils.generateRefreshToken()));
        }

        @PostMapping("/registration")
        public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest)
                        throws JsonProcessingException, InvalidKeyException, NoSuchPaddingException,
                        NoSuchAlgorithmException,
                        InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException,
                        InvalidKeySpecException {

                if (userRepository.existsByEmail(registrationRequest.getEmail())) {
                        return ResponseEntity
                                        .badRequest()
                                        .body(new JwtResponse());
                }

                User user = new User(registrationRequest.getEmail(),
                                new BCryptPasswordEncoder().encode(registrationRequest.getPassword()));

                userRepository.save(user);

                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(),
                                                registrationRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                return ResponseEntity
                                .ok(new JwtResponse(jwtUtils.generateAccessToken(authentication),
                                                jwtUtils.generateRefreshToken()));
        }

        @PostMapping("/refreshtoken")
        public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenRequest request)
                        throws JsonProcessingException, InvalidKeyException, NoSuchPaddingException,
                        NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException,
                        IllegalBlockSizeException, InvalidKeySpecException {

                Authentication authentication = authenticationManager
                                .authenticate(new JwtTokenAuthenticationToken(request.getRefreshToken()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                return ResponseEntity
                                .ok(new JwtResponse(jwtUtils.generateAccessToken(authentication),
                                                jwtUtils.generateRefreshToken()));
        }
}
