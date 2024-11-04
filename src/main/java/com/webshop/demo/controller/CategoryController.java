package com.webshop.demo.controller;

import com.webshop.demo.Category;
import com.webshop.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    // List all categories with pagination
    @GetMapping
    public String listCategories(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        logger.info("Fetching all categories for page: {}", page);
        Page<Category> categoryPage = categoryService.getAllCategories(PageRequest.of(page, size));
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        return "category-list";
    }

    // Get a single category by ID
    @GetMapping("/{id}")
    public String getCategoryById(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id).orElse(null);
        if (category == null) {
            model.addAttribute("error", "Category not found");
            logger.warn("Category with ID {} not found", id);
            return "category-detail"; // Adjust view as needed
        }
        model.addAttribute("category", category);
        return "category-detail";
    }

    // Create a new category
    @PostMapping("/create")
    public String createCategory(@RequestParam String name, @RequestParam String description, RedirectAttributes redirectAttributes) {
        Category category = new Category(name, description);
        categoryService.saveCategory(category);
        logger.info("Created new category: {}", name);
        redirectAttributes.addFlashAttribute("message", "Category created successfully.");
        return "redirect:/categories"; // Redirect to category list
    }

    // Update an existing category
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @RequestParam String name,
                                 @RequestParam String description,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        Category category = categoryService.getCategoryById(id).orElse(null);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Category not found");
            logger.warn("Attempted to update non-existent category with ID {}", id);
            return "redirect:/categories";
        }

        category.setName(name);
        category.setDescription(description);
        categoryService.saveCategory(category);
        logger.info("Updated category with ID {}: {}", id, name);
        redirectAttributes.addFlashAttribute("message", "Category updated successfully.");
        return "redirect:/categories";
    }

    // Delete a category by ID
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (categoryService.getCategoryById(id).isPresent()) {
            categoryService.deleteCategory(id);
            logger.info("Deleted category with ID {}", id);
            redirectAttributes.addFlashAttribute("message", "Category deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Category not found");
            logger.warn("Attempted to delete non-existent category with ID {}", id);
        }
        return "redirect:/categories";
    }
}
