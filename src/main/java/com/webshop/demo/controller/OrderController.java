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

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    // Retrieve the authenticated user's order history and display it in a Thymeleaf view
    @GetMapping("/user/history")
    public String getUserOrderHistory(Model model) {
        User user = cartService.getUser();
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "order-history";  // Render order-history.html
    }

    // Retrieve a specific order's details for the authenticated user
    @GetMapping("/user/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, Model model) {
        User user = cartService.getUser();
        Order order = orderService.getOrderByIdAndUser(orderId, user);
        model.addAttribute("order", order);
        return "order-details";  // Render order-details.html
    }

    // Admin-only: Retrieve all orders and display them in a Thymeleaf view
    @GetMapping("/admin/all")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin-order-list";  // Render admin-order-list.html
    }

    // Admin-only: Retrieve orders by status and display in a Thymeleaf view
    @GetMapping("/admin/status")
    public String getOrdersByStatus(@RequestParam OrderStatus status, Model model) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        model.addAttribute("orders", orders);
        model.addAttribute("status", status);
        return "admin-order-status";  // Render admin-order-status.html
    }

    // Admin-only: Update the status of an order
    @PostMapping("/admin/update-status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/orders/admin/all";  // Redirect to admin order list after update
    }
}
