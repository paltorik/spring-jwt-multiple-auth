package com.example.test.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest implements PasswordProviderValidation, SmsProviderValidation{

    @NotBlank
    private String provider;

    @NotBlank(groups = PasswordProviderValidation.class)
    private String email;    

    @NotBlank(groups = PasswordProviderValidation.class)
    @Size(min = 8, max = 255)
    private String password;

    @NotBlank(groups = SmsProviderValidation.class)
    private String code;
    
    @NotBlank(groups = SmsProviderValidation.class)
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}