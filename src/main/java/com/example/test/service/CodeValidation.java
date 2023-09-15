package com.example.test.service;

public interface CodeValidation {
    
    String generate(String creditals);

    Boolean validation(String creditals, String code);
}