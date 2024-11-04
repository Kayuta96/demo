package com.webshop.demo.controller;

import com.webshop.demo.Product;
import com.webshop.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // List all products with pagination and optional sorting
    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "name") String sortBy,
                               @RequestParam(defaultValue = "asc") String order,
                               Model model) {
        Page<Product> productPage = productService.getAllProducts(page, size, sortBy, order);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);

        return "product-list";
    }

    // Buy product and reduce stock quantity
    @PostMapping("/buy")
    public String buyProduct(@RequestParam Long productId, @RequestParam int quantity, Model model) {
        boolean success = productService.reduceStock(productId, quantity);

        if (!success) {
            model.addAttribute("error", "Insufficient stock for the requested quantity.");
            return "product-detail"; // Adjust to a relevant view if needed
        }

        return "redirect:/products";
    }

    // Search products by keyword with pagination
    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String keyword,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Page<Product> productPage = productService.searchProducts(keyword, page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        if (keyword == null || keyword.isEmpty()) {
            model.addAttribute("message", "Displaying all products.");
        } else if (productPage.isEmpty()) {
            model.addAttribute("message", "No products found for keyword: " + keyword);
        }

        return "product-list";
    }

    // Filter products by category, price range, and rating with pagination
    @GetMapping("/filter")
    public String filterProducts(@RequestParam String category,
                                 @RequestParam BigDecimal minPrice,
                                 @RequestParam BigDecimal maxPrice,
                                 @RequestParam double rating,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Page<Product> productPage = productService.filterProducts(category, minPrice, maxPrice, rating, page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("category", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("rating", rating);

        if (productPage.isEmpty()) {
            model.addAttribute("message", "No products match the selected filters.");
        }

        return "product-list";
    }
}
