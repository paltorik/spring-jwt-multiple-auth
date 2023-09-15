package com.example.test.request.auth;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

@Service
@Validated
public class LoginValidationService {
    
    @Validated(PasswordProviderValidation.class)
    public void validateForPassword(@Valid LoginRequest input){
    }

    @Validated(SmsProviderValidation.class)
    public void validateForSms(@Valid LoginRequest input){
    }
}
