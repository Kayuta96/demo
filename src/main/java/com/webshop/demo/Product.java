package com.webshop.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Produktnamnet får inte vara tomt")
    private String name;

    private String description;

    @Column(nullable = false)
    private int stockQuantity;

    @NotNull(message = "Priset får inte vara tomt")
    @Min(value = 0, message = "Priset måste vara ett positivt tal")
    private BigDecimal price;

    @Min(value = 0, message = "Lagret måste vara minst 0")
    private int stock;

    // New field: average rating
    @Min(value = 0, message = "Betyget måste vara minst 0")
    @Column(nullable = false)
    private double rating;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
