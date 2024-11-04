package com.webshop.demo.service;

import com.webshop.demo.Order;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    // Mock payment processing method
    public boolean processPayment(Order order) {
        // Simulate a successful payment for testing purposes
        return true;  // Return true for a successful mock payment
    }
}