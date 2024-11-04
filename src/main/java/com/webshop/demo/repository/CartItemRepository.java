package com.webshop.demo.repository;

import com.webshop.demo.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Additional query methods can be added if needed
}
