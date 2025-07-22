package com.example.BankApp.controller;

import com.example.BankApp.model.Role;
import com.example.BankApp.model.Users;
import com.example.BankApp.service.AccountsService;
import com.example.BankApp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // 1. Register as a CUSTOMER
    @PostMapping("/register")
    public Users registerCustomer(@RequestBody Users user) {
        return usersService.createCustomer(user);
    }

    // 2. Manager creates employee
    @PostMapping("/create-employee")
    public Users createEmployee(@RequestBody Users user) {
        Users manager = getLoggedInUser(); // Replace with real JWT later
        return usersService.createEmployee(user, manager);
    }

    // 3. Get user by ID (self or manager)
    @GetMapping("/get/{id}")
    public Users getUserById(@PathVariable Long id) {
        Users requestingUser = getLoggedInUser(); // Replace with JWT later
        return usersService.getUserById(id, requestingUser);
    }

    // 4. Get all users (Manager only)
    @GetMapping("/all")
    public List<Users> getAllUsers() {
        Users manager = getLoggedInUser(); // Replace with real user later
        return usersService.getAllUsers(manager);
    }

    // 5. Update user (partial)
    @PutMapping("/update/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users updatedUser) {
        Users requestingUser = getLoggedInUser(); // Replace with JWT later
        return usersService.updateUser(id, updatedUser, requestingUser);
    }

    // 6. Delete user (Manager only)
    @DeleteMapping("/del/{id}")
    public String deleteUser(@PathVariable Long id) {
        Users manager = getLoggedInUser(); // Replace with JWT later
        usersService.deleteUser(id, manager);
        return "User deleted successfully.";
    }

    // ----------------------------
    // 🔄 MOCK LOGGED-IN USERS (Temporary)
    private Users getMockManager() {
        Users manager = new Users();
        manager.setId(1L);
        manager.setRole(Role.MANAGER);
        return manager;
    }

    private Users getMockCustomer() {
        Users customer = new Users();
        customer.setId(2L); // use real userId from your DB for testing
        customer.setRole(Role.CUSTOMER);
        return customer;
    }

    private Users getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // or getEmail() if customized
        } else {
            email = principal.toString();
        }

        return usersService.findByEmail(email); // Get from DB
    }
}
