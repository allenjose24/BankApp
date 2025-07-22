package com.example.BankApp.controller;

import com.example.BankApp.model.AccountStatus;
import com.example.BankApp.model.Accounts;
import com.example.BankApp.model.Users;
import com.example.BankApp.service.AccountsService;
import com.example.BankApp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private UsersService usersService;

    // ✅ Create account for authenticated user
    @PostMapping("/create")
    public Accounts createAccount(@RequestParam String type,
                                  @RequestParam BigDecimal balance,
                                  Principal principal) {
        Users user = usersService.findByEmail(principal.getName()); // use JWT's email
        return accountsService.createAccountForUser(user, type, balance);
    }

    // ✅ Get all accounts for logged-in user
    @GetMapping("/my")
    public List<Accounts> getMyAccounts(Principal principal) {
        Users user = usersService.findByEmail(principal.getName());
        return accountsService.getAccountsByUser(user);
    }

    // ✅ Get account by ID (manager or owner)
    @GetMapping("/{id}")
    public Accounts getAccountById(@PathVariable Long id, Principal principal) {
        Users user = usersService.findByEmail(principal.getName());

        return accountsService.getAccountIfAuthorized(id, user);
    }

    // ✅ Update account status (Manager only)
    @PutMapping("/{id}/status")
    public Accounts updateAccountStatus(@PathVariable Long id,
                                        @RequestParam AccountStatus status,
                                        Principal principal) {
        Users manager = usersService.findByEmail(principal.getName());
        return accountsService.updateAccountStatusAsManager(id, status, manager);
    }

    // ✅ Delete account (owner or manager)
    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id, Principal principal) {
        Users user = usersService.findByEmail(principal.getName());
        accountsService.deleteAccount(id, user);
        return "Account deleted successfully.";
    }
}

