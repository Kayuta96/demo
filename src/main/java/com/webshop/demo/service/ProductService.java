package com.webshop.demo.service;

import com.webshop.demo.Product;
import com.webshop.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Retrieve all products with sorting and pagination
    public Page<Product> getAllProducts(int page, int size, String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
    }

    // Search products by keyword in name or description, with pagination
    public Page<Product> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            return productRepository.findAll(pageable);  // Return all products if no keyword is provided
        }
        return productRepository.searchByKeyword(keyword, pageable);  // Custom search query
    }

    // Filter products by category, price range, and rating with pagination
    public Page<Product> filterProducts(String category, BigDecimal minPrice, BigDecimal maxPrice, double rating, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryNameAndPriceBetweenAndRatingGreaterThanEqual(
                category, minPrice, maxPrice, rating, pageable);
    }

    // Retrieve a single product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Alternative method to retrieve product by ID, returns null if not found
    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    // Reduce stock quantity for a product if available
    public boolean reduceStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStockQuantity() >= quantity) {
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
            return true;
        } else {
            return false; // Not enough stock
        }
    }

    // Save a new or updated product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
