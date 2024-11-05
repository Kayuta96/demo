package com.webshop.demo.service;

import com.webshop.demo.Cart;
import com.webshop.demo.CartItem;
import com.webshop.demo.User;
import com.webshop.demo.Product;
import com.webshop.demo.Order;
import com.webshop.demo.OrderStatus;
import com.webshop.demo.repository.CartRepository;
import com.webshop.demo.repository.CartItemRepository;
import com.webshop.demo.repository.ProductRepository;
import com.webshop.demo.repository.OrderRepository;
import com.webshop.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository; // Corrected: Added missing userRepository

    // Retrieve or create a cart for the authenticated user
    public Cart getOrCreateCart() {
        User user = getAuthenticatedUser();
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // Add or update a product in the cart
    public CartItem addOrUpdateItem(Product product, int quantity) {
        Cart cart = getOrCreateCart();
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem(product, cart, quantity);  // Corrected: Set parameters in the correct order
                    cart.getItems().add(newItem);
                    return newItem;
                });

        // Update quantity if item already exists
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        return cartItemRepository.save(cartItem);
    }

    // Checkout process: creates order, updates stock, clears cart if successful
    public String checkout() {
        Cart cart = getOrCreateCart();
        if (cart.getItems().isEmpty()) {
            return "Your cart is empty. Add items before checkout.";
        }

        // Create and save the order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setItems(cart.getItems());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        // Clear the cart after saving the order
        clearCart();
        return "Checkout successful. Your order has been placed.";
    }

    // Clear the cart for the authenticated user
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // Calculate the total cost of the cart
    public BigDecimal getTotal() {
        Cart cart = getOrCreateCart();
        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Helper method to retrieve the authenticated user
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User)) {
            throw new IllegalStateException("User not authenticated");
        }
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
