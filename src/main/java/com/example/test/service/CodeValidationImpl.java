package com.example.test.service;

import org.springframework.stereotype.Component;

@Component
public class CodeValidationImpl  {

    public static String generate(String creditals) {
        return "7777";
    }

    public static Boolean validation(String creditals, String code) {
        return "7777".equals(code);
    }
    
}
