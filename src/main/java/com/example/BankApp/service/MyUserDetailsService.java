package com.example.BankApp.service;

import com.example.BankApp.model.Users;
import com.example.BankApp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public MyUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(identifier).orElseGet(() -> usersRepository.findByName(identifier)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + identifier)));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),  // or user.getName() – just match what you use in login
                user.getPassword(),
                Collections.singleton(user.getRole()) // Assuming role implements GrantedAuthority
        );
    }
}
