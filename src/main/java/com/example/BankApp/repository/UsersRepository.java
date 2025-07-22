package com.example.BankApp.repository;

import com.example.BankApp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

//    Users findByName(String name);
//    Users findByEmail(String email);

    Optional<Users> findByEmail(String email);
    Optional<Users> findByName(String name);

    boolean existsByName(String name);
    boolean existsByEmail(String email);

}
