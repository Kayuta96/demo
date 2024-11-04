package com.webshop.demo.controller;

import com.webshop.demo.Order;
import com.webshop.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    // Complete the checkout process and redirect to confirmation page
    @PostMapping("/checkout/complete")
    public String completeCheckout(Model model) {
        Order order = cartService.checkout(); // Complete the checkout and retrieve the order
        model.addAttribute("order", order); // Pass the order to the confirmation page
        return "confirmation"; // Render confirmation.html with order details
    }
}
