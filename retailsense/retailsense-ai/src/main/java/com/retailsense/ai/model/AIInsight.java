package com.retailsense.ai.model;

import com.retailsense.common.model.BaseEntity;
import com.retailsense.product.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ai_insights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIInsight extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "product_id", unique = true)
    private Product product;

    @Column(name = "days_until_stockout")
    private Integer daysUntilStockout;

    @Column(name = "recommended_reorder_qty")
    private Integer recommendedReorderQty;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String action;

    @Column(name = "confidence_score", precision = 3, scale = 2)
    private BigDecimal confidenceScore;

    public enum Priority {
        HIGH, MEDIUM, LOW
    }
}