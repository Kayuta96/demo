package com.webshop.demo.controller;

import com.webshop.demo.Order;
import com.webshop.demo.OrderStatus;
import com.webshop.demo.User;
import com.webshop.demo.service.OrderService;
import com.webshop.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller  // Changed to @Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    // Retrieve the authenticated user's order history
    @GetMapping("/user/history")
    public String getUserOrderHistory(Model model) {
        User user = cartService.getUser();  // Get the authenticated user
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);  // Pass orders to the model for the view
        return "user-order-history";  // Renders user-order-history.html
    }

    // Retrieve details of a specific order for the authenticated user
    @GetMapping("/user/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, Model model) {
        User user = cartService.getUser();
        Order order = orderService.getOrderByIdAndUser(orderId, user);
        model.addAttribute("order", order);  // Pass the order to the model
        return "order-details";  // Renders order-details.html
    }

    // Admin endpoint: Retrieve all orders
    @GetMapping("/admin/all")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);  // Pass all orders to the model
        return "admin-all-orders";  // Renders admin-all-orders.html
    }

    // Admin endpoint: Retrieve orders by status
    @GetMapping("/admin/status")
    public String getOrdersByStatus(@RequestParam OrderStatus status, Model model) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        model.addAttribute("orders", orders);  // Pass filtered orders to the model
        model.addAttribute("status", status);   // Include the status filter in the model
        return "admin-orders-by-status";  // Renders admin-orders-by-status.html
    }

    // Admin endpoint: Update the status of an order
    @PostMapping("/admin/update-status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam OrderStatus status, Model model) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        model.addAttribute("order", updatedOrder);
        model.addAttribute("message", "Order status updated successfully.");
        return "redirect:/orders/admin/all";  // Redirect to all orders page after update
    }
}
