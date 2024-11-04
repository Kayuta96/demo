package com.webshop.demo.repository;

import com.webshop.demo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);  // Hämta en roll baserat på dess namn (t.ex. ROLE_USER)
}