package com.webshop.demo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    // Add product to cart
    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        CartItem newItem = new CartItem();
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        newItem.setCart(this); // Set the relationship back to this cart
        items.add(newItem);
    }

    // Remove product from cart
    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    // Retrieve all items in the cart
    public List<CartItem> getItems() {
        return items;
    }

    // Calculate total price
    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Clear the cart
    public void clear() {
        items.clear();
    }

    // Update quantity for a specific product
    public void updateItemQuantity(Long productId, int newQuantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(newQuantity);
                break;
            }
        }
    }

    // Getters and Setters for ID and User
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
