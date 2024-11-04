package com.webshop.demo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime statusUpdatedDate;  // Track last status update

    private boolean paymentReceived;

    private String shippingAddress;

    private String orderNumber;

    private boolean fulfilled;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Link order to User
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    // Constructor
    public Order() {
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;  // Default to PENDING when order is created
        this.statusUpdatedDate = LocalDateTime.now();  // Initialize with current date/time
        this.paymentReceived = false;
        this.orderNumber = generateOrderNumber();
        this.fulfilled = false;  // Default to not fulfilled
    }

    // Generate a unique order number using UUID
    private String generateOrderNumber() {
        return UUID.randomUUID().toString();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    // Set order status and automatically update the statusUpdatedDate
    public void setStatus(OrderStatus status) {
        this.status = status;
        this.statusUpdatedDate = LocalDateTime.now();  // Update statusUpdatedDate each time status changes
    }

    public LocalDateTime getStatusUpdatedDate() {
        return statusUpdatedDate;
    }

    // Add this setter method to allow setting the statusUpdatedDate externally
    public void setStatusUpdatedDate(LocalDateTime statusUpdatedDate) {
        this.statusUpdatedDate = statusUpdatedDate;
    }

    public boolean isPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        for (CartItem item : items) {
            item.setOrder(this);  // Ensure each CartItem has a reference to this Order
        }
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    // Calculate the total price of all items in the order
    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
