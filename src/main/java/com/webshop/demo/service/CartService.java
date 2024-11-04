package com.webshop.demo.service;

import com.webshop.demo.*;
import com.webshop.demo.repository.CartRepository;
import com.webshop.demo.repository.CartItemRepository;
import com.webshop.demo.repository.ProductRepository;
import com.webshop.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Retrieve the cart for the current user, or create a new one if it doesn't exist
    public Cart getOrCreateCart() {
        User user = getAuthenticatedUser();
        if (user == null) throw new IllegalStateException("User not authenticated");

        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    // Public method to get the authenticated user, accessible by CartController
    public User getUser() {
        return getAuthenticatedUser();
    }

    // Add an item to the user's cart
    public void addItem(Product product, int quantity) {
        Cart cart = getOrCreateCart();

        // Check if the product is already in the cart
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                cartItemRepository.save(item);
                return;
            }
        }

        // If not, add a new item to the cart
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        cart.getItems().add(newItem);
        cartItemRepository.save(newItem);
    }

    // Checkout: Verify stock and update inventory
    public Order checkout() {
        Cart cart = getOrCreateCart();
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();

            // Check if stock is sufficient for each item
            if (product.getStockQuantity() >= item.getQuantity()) {
                product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                productRepository.save(product);
            } else {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
        }
        clearCart(); // Clear the cart after checkout
        return null;
    }

    // Clear the user's cart
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // Get all items in the user's cart
    public List<CartItem> getCartItems() {
        return getOrCreateCart().getItems();
    }

    // Update the quantity of a specific product in the cart
    public void updateItemQuantity(Long productId, int newQuantity) {
        Cart cart = getOrCreateCart();
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(newQuantity);
                cartItemRepository.save(item);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found in cart");
    }

    // Remove an item from the cart
    public void removeItem(Long productId) {
        Cart cart = getOrCreateCart();
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    // Calculate the total price of the cart
    public BigDecimal getTotal() {
        return getOrCreateCart().getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Helper method to retrieve the authenticated user
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            return userRepository.findByUsername(username);
        }
        return null;
    }
}
