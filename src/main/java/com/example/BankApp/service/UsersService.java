package com.example.BankApp.service;

import com.example.BankApp.model.AccountType;
import com.example.BankApp.model.Role;
import com.example.BankApp.model.Users;
import com.example.BankApp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Create new user (anyone can register as CUSTOMER)
    public Users createCustomer(Users user) {
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password
        accountsService.createAccountForUser(user, "SAVINGS", BigDecimal.ZERO);
        return usersRepository.save(user);

    }

    // Create employee (Manager only)
    public Users createEmployee(Users employee, Users requestingUser) {
        if (requestingUser.getRole() != Role.MANAGER) {
            throw new SecurityException("Only manager can create employees.");
        }
        return usersRepository.save(employee);
    }

    // Get user by ID (self or Manager)
    public Users getUserById(Long id, Users requestingUser) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!requestingUser.getId().equals(id) && requestingUser.getRole() != Role.MANAGER) {
            throw new SecurityException("Access denied.");
        }
        return user;
    }

    // Get all users (Manager only)
    public List<Users> getAllUsers(Users requestingUser) {
        if (requestingUser.getRole() != Role.MANAGER) {
            throw new SecurityException("Only manager can view all users.");
        }
        return usersRepository.findAll();
    }

    // Update user (self or Manager)
    public Users updateUser(Long id, Users updatedUser, Users requestingUser) {
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!requestingUser.getId().equals(id) && requestingUser.getRole() != Role.MANAGER) {
            throw new SecurityException("Access denied: You can only update your own account.");
        }

        // Partial update (only non-null fields)
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPhone() != null) {
            existingUser.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getAddress() != null) {
            existingUser.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        // Role change only allowed by manager
        if (updatedUser.getRole() != null && requestingUser.getRole() == Role.MANAGER) {
            existingUser.setRole(updatedUser.getRole());
        }

        return usersRepository.save(existingUser);
    }

    // Delete user (Manager only)
    public void deleteUser(Long id, Users requestingUser) {
        if (requestingUser.getRole() != Role.MANAGER) {
            throw new SecurityException("Only manager can delete users.");
        }

        Users userToDelete = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        usersRepository.delete(userToDelete);
    }

    // Optional: Find by email
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    // Optional: Check existence
    public boolean existsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    public boolean existsByName(String name) {
        return usersRepository.existsByName(name);
    }
}
