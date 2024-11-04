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

    // Initialize the ROLE_USER role if it doesn't already exist
    @PostConstruct
    public void initRoles() {
        if (roleRepository.findByName("ROLE_USER") == null) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
    }

    // Register a new user and assign the default ROLE_USER
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encrypt the password

        // Assign the default role ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole != null) {
            user.addRole(userRole);
        }

        userRepository.save(user);  // Save the user in the database
    }

    // Save a user (used for managing existing users or roles)
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // Retrieve the currently authenticated user
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            return userRepository.findByUsername(username);
        }
        throw new IllegalStateException("User not authenticated");
    }

    // Update user profile information
    public User updateUserProfile(User updatedUser) {
        User user = getAuthenticatedUser();

        // Update profile fields
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        // Save updated user profile in the database
        return userRepository.save(user);
    }
}
