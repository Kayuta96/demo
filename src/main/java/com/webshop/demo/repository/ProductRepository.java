package com.webshop.demo.repository;

import com.webshop.demo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search by keyword in name or description with pagination
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Filter by category, price range, and rating with pagination
    @Query("SELECT p FROM Product p WHERE p.category.name = :category " +
            "AND p.price BETWEEN :minPrice AND :maxPrice " +
            "AND p.rating >= :rating")
    Page<Product> findByCategoryNameAndPriceBetweenAndRatingGreaterThanEqual(
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("rating") double rating,
            Pageable pageable);

    // Basic search by keyword in name with pagination
    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    // Retrieve products within a specific price range
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Retrieve products with more than a specified amount in stock
    List<Product> findByStockGreaterThan(int stockThreshold);

    // Search by name and sort by price in ascending order
    List<Product> findByNameContainingOrderByPriceAsc(String keyword);
}