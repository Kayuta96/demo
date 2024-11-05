package com.webshop.demo.repository;

import com.webshop.demo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  // Ensure this returns Optional<User>
}