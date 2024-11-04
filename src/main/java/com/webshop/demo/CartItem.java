package com.webshop.demo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;

    // Default constructor
    public CartItem() {
    }

    // Parameterized constructor
    public CartItem(Product product, Cart cart, int quantity) {
        this.product = product;
        this.cart = cart;
        this.quantity = quantity;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    // Calculate total price for this CartItem
    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
