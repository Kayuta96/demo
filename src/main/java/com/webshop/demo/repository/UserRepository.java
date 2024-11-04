package com.webshop.demo.repository;

import com.webshop.demo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);  // Hämta en användare baserat på användarnamn
}