package com.webshop.demo.service;

import com.webshop.demo.Order;
import com.webshop.demo.OrderStatus;
import com.webshop.demo.User;
import com.webshop.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    // Save a new order or update an existing order
    public Order saveOrder(Order order) {
        order.setStatusUpdatedDate(LocalDateTime.now());  // Update the status date upon saving
        Order savedOrder = orderRepository.save(order);
        logger.info("Order saved with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    // Retrieve orders by a specific user (for user order history)
    public List<Order> getOrdersByUser(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        logger.info("Retrieved {} orders for user ID: {}", orders.size(), user.getId());
        return orders;
    }

    // Retrieve a specific order by ID and user (for order confirmation)
    public Order getOrderByIdAndUser(Long orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> {
                    logger.warn("Order not found for ID: {} and user ID: {}", orderId, user.getId());
                    return new IllegalArgumentException("Order not found");
                });
    }

    // Retrieve all orders (for admin view or management purposes)
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        logger.info("Retrieved {} orders", orders.size());
        return orders;
    }

    // Update the order status and status update date (used by admin or automated processes)
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.warn("Order not found for ID: {}", orderId);
                    return new IllegalArgumentException("Order not found");
                });
        order.setStatus(status);
        order.setStatusUpdatedDate(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        logger.info("Order ID: {} status updated to {}", orderId, status);
        return updatedOrder;
    }

    // Retrieve orders by status (useful for admin or tracking purposes)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        logger.info("Retrieved {} orders with status: {}", orders.size(), status);
        return orders;
    }

    // Retrieve orders updated within a specific time range (useful for recent activity tracking)
    public List<Order> getOrdersUpdatedAfter(LocalDateTime dateTime) {
        List<Order> orders = orderRepository.findByStatusUpdatedDateAfter(dateTime);
        logger.info("Retrieved {} orders updated after: {}", orders.size(), dateTime);
        return orders;
    }
}
