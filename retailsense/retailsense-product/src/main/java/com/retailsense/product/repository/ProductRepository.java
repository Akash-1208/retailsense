package com.retailsense.product.repository;

import com.retailsense.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by category
    List<Product> findByCategory(String category);

    // Find products with low stock
    @Query("SELECT p FROM Product p WHERE p.quantity <= p.minimumThreshold")
    List<Product> findLowStockProducts();

    // Find products with no stock
    @Query("SELECT p FROM Product p WHERE p.quantity = 0")
    List<Product> findOutOfStockProducts();

    // Search products by name (case-insensitive)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByName(@Param("searchTerm") String searchTerm);

    // Find products by category with stock filter
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.quantity > 0")
    List<Product> findByCategoryWithStock(@Param("category") String category);

    // Get all distinct categories
    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
    List<String> findAllCategories();

    // Count products by stock status
    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity <= p.minimumThreshold")
    Long countLowStockProducts();
}