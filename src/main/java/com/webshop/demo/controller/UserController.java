package com.webshop.demo.controller;

import com.webshop.demo.User;
import com.webshop.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller  // Changed to @Controller for MVC
@RequestMapping("/user")  // No need for /api prefix in a monolithic setup
public class UserController {

    @Autowired
    private UserService userService;

    // View login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // Renders login.html for the login page
    }

    // View registration page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());  // Sends an empty User object to the registration view
        return "register";  // Renders register.html for the registration page
    }

    // Handle POST request to register a new user
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);  // Register user with encryption and role assignment
        return "redirect:/user/login";  // Redirect to login page after successful registration
    }

    // --- Profile Management Endpoints ---

    // View profile of the authenticated user
    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User authenticatedUser = userService.getAuthenticatedUser();
        model.addAttribute("user", authenticatedUser);  // Add user to model for profile view
        return "profile";  // Renders profile.html for profile view
    }

    // Update the profile of the authenticated user
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User updatedUser, Model model) {
        User updated = userService.updateUserProfile(updatedUser);
        model.addAttribute("user", updated);  // Add updated user to model
        model.addAttribute("message", "Profile updated successfully.");
        return "profile";  // Re-render profile page with updated info
    }
}
