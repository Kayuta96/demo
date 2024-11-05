package com.webshop.demo.service;

import com.webshop.demo.User;
import com.webshop.demo.Role;
import com.webshop.demo.repository.UserRepository;
import com.webshop.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Initialize default roles
    @PostConstruct
    public void initRoles() {
        roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role userRole = new Role("ROLE_USER");
            return roleRepository.save(userRole);
        });
    }

    // Register a new user with default ROLE_USER
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        roleRepository.findByName("ROLE_USER").ifPresent(user::addRole);
        userRepository.save(user);
    }

    // Save or update a user with encrypted password
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // Retrieve authenticated user from security context
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
        }
        throw new IllegalStateException("User not authenticated");
    }

    // Update profile information of the authenticated user
    public User updateUserProfile(User updatedUser) {
        User user = getAuthenticatedUser();
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        return userRepository.save(user);
    }
}
