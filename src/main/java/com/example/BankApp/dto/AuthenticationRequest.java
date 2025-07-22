package com.example.BankApp.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;

    // Getters and Setters
}
