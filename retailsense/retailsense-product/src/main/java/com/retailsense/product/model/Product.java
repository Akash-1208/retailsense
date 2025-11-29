package com.retailsense.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "selling_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "minimum_threshold")
    private Integer minimumThreshold = 10;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Computed field - profit margin percentage
    @Transient
    public BigDecimal getProfitMargin() {
        if (purchasePrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return sellingPrice.subtract(purchasePrice)
                .divide(purchasePrice, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    // Helper method to check stock status
    @Transient
    public StockStatus getStockStatus() {
        if (quantity == 0) {
            return StockStatus.OUT_OF_STOCK;
        } else if (quantity <= minimumThreshold) {
            return StockStatus.LOW_STOCK;
        } else {
            return StockStatus.SUFFICIENT;
        }
    }

    public enum StockStatus {
        OUT_OF_STOCK,
        LOW_STOCK,
        SUFFICIENT
    }
}