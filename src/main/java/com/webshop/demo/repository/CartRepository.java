package com.webshop.demo.repository;

import com.webshop.demo.Cart;
import com.webshop.demo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Custom query to find a cart by user
    Optional<Cart> findByUser(User user);
}
