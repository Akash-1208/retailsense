package com.retailsense.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopProductsResponse {
    private List<ProductSales> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductSales {
        private Long productId;
        private String productName;
        private String category;
        private BigDecimal totalRevenue;
        private Integer totalUnitsSold;
        private Integer salesCount;
        private BigDecimal profitMargin;
    }
}