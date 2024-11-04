package com.webshop.demo.service;

import com.webshop.demo.Cart;
import com.webshop.demo.CartItem;
import com.webshop.demo.Product;
import com.webshop.demo.repository.CartItemRepository;
import com.webshop.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    // Add or update an item in the cart
    public CartItem addOrUpdateCartItem(Cart cart, Long productId, int quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }

        Product product = productOpt.get();

        // Check if item already exists in the cart
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return cartItemRepository.save(item); // Update existing item
            }
        }

        // If item doesn't exist, create a new one
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        cart.getItems().add(newItem); // Add to cart's items list
        return cartItemRepository.save(newItem); // Save new item
    }

    // Update the quantity of an item in the cart
    public CartItem updateItemQuantity(Cart cart, Long productId, int newQuantity) {
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(newQuantity);
                return cartItemRepository.save(item); // Update the quantity
            }
        }
        throw new IllegalArgumentException("Product not found in cart with ID: " + productId);
    }

    // Remove an item from the cart
    public void removeItemFromCart(Cart cart, Long productId) {
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartItemRepository.flush(); // Synchronize with the database
    }

    // Clear all items from the cart
    public void clearCartItems(Cart cart) {
        cart.getItems().clear();
        cartItemRepository.flush(); // Synchronize with the database
    }
}
