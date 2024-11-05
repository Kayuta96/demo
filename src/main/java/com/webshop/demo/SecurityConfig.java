package com.webshop.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin/**").hasRole("ADMIN")           // Restrict admin access
                        .requestMatchers("/register", "/login", "/", "/css/**", "/js/**").permitAll()  // Allow public access
                        .requestMatchers("/products/**").permitAll()            // Public access to products
                        .requestMatchers("/cart/**", "/checkout/**").authenticated()  // Require login for cart and checkout
                        .anyRequest().authenticated()                           // Authenticate all other requests
                )
                .formLogin(form -> form
                        .loginPage("/login")                                    // Custom login page
                        .defaultSuccessUrl("/", true)                           // Redirect to home page on successful login
                        .permitAll()                                            // Allow everyone to access login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")                      // Redirect to login with logout message
                        .permitAll()                                            // Allow everyone to access logout
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/checkout/**", "/api/**")     // Disable CSRF for specific routes if needed
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

