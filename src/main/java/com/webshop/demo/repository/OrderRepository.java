package com.webshop.demo.repository;

import com.webshop.demo.Order;
import com.webshop.demo.OrderStatus;
import com.webshop.demo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders based on fulfillment status
    List<Order> findByFulfilled(boolean fulfilled);

    // Find orders based on customer ID
    List<Order> findByUserId(Long userId);

    // Find orders created after a certain date
    List<Order> findByOrderDateAfter(LocalDateTime date);

    // Find orders by User
    List<Order> findByUser(User user);

    // Find unfulfilled orders, sorted by order date
    List<Order> findByFulfilledFalseOrderByOrderDateAsc();

    // Find an order by ID and user (custom query)
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.user = :user")
    Optional<Order> findByIdAndUser(@Param("orderId") Long orderId, @Param("user") User user);

    // Find orders by status
    List<Order> findByStatus(OrderStatus status);

    // Find orders updated after a specific date (for recent activity tracking)
    List<Order> findByStatusUpdatedDateAfter(LocalDateTime dateTime);

    // Find orders by status for a specific user
    List<Order> findByUserAndStatus(User user, OrderStatus status);
}
