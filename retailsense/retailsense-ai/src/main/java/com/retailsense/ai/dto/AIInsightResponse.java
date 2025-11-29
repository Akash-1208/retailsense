package com.retailsense.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIInsightResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Integer currentStock;
    private Integer daysUntilStockout;
    private Integer recommendedReorderQty;
    private String priority;
    private String reason;
    private String action;
    private BigDecimal confidenceScore;
    private LocalDateTime generatedAt;
}