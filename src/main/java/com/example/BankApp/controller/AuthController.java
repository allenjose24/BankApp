package com.example.BankApp.controller;

import com.example.BankApp.dto.AuthenticationRequest;
import com.example.BankApp.dto.AuthenticationResponse;
import com.example.BankApp.model.Role;
import com.example.BankApp.model.Users;
import com.example.BankApp.repository.UsersRepository;
import com.example.BankApp.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody Users user) {
        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already taken");
        }
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password
        usersRepository.save(user);
        String token = jwtService.generateToken(user.getEmail()); // Use email for consistency
        return new AuthenticationResponse(token);
    }


    @PostMapping("/register-manager")
    public AuthenticationResponse registerManger(@RequestBody Users user) {
        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already taken");
        }
        user.setRole(Role.MANAGER);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password
        usersRepository.save(user);
        String token = jwtService.generateToken(user.getEmail()); // Use email for consistency
        return new AuthenticationResponse(token);
    }


    // 🔑 Login
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authRequest) {
        Authentication auth = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        String token = jwtService.generateToken(authRequest.getEmail());
        return new AuthenticationResponse(token);
    }
}
