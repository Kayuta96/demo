package com.webshop.demo.controller;

import com.webshop.demo.Cart;
import com.webshop.demo.CartItem;
import com.webshop.demo.Product;
import com.webshop.demo.Order;
import com.webshop.demo.OrderStatus;
import com.webshop.demo.service.CartService;
import com.webshop.demo.service.CartItemService;
import com.webshop.demo.service.ProductService;
import com.webshop.demo.service.OrderService;
import com.webshop.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public String viewCart(Model model) {
        Cart cart = cartService.getOrCreateCart();
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("total", cartService.getTotal());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Model model) {
        Product product = productService.findProductById(productId);
        Cart cart = cartService.getOrCreateCart();
        cartItemService.addOrUpdateCartItem(cart, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartItemQuantity(@RequestParam Long productId, @RequestParam int quantity) {
        Cart cart = cartService.getOrCreateCart();
        cartItemService.updateItemQuantity(cart, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId) {
        Cart cart = cartService.getOrCreateCart();
        cartItemService.removeItemFromCart(cart, productId);
        return "redirect:/cart";
    }

    @GetMapping("/total")
    public String getTotal(Model model) {
        model.addAttribute("total", cartService.getTotal());
        return "cart";
    }

    @PostMapping("/checkout")
    public String checkout(Model model) {
        Cart cart = cartService.getOrCreateCart();
        if (cart.getItems().isEmpty()) {
            model.addAttribute("error", "Your cart is empty. Please add items before checkout.");
            return "cart";
        }

        Order order = new Order();
        order.setUser(cartService.getUser());
        order.setItems(cart.getItems());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        boolean paymentSuccessful = paymentService.processPayment(order);
        if (!paymentSuccessful) {
            model.addAttribute("error", "Payment failed. Please try again.");
            return "cart";
        }

        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentReceived(true);
        orderService.saveOrder(order);
        cartService.clearCart();

        model.addAttribute("order", order);
        return "confirmation";
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
