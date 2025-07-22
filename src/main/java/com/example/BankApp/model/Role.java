package com.example.BankApp.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    MANAGER,
    CLERK,
    LOAN_OFFICER,
    CUSTOMER;

    @Override
    public String getAuthority() {
        return name();
    }
}
