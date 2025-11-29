package com.retailsense.sales.dto;

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
public class SaleResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productCategory;
    private Integer quantitySold;
    private BigDecimal salePrice;
    private BigDecimal totalRevenue;
    private LocalDateTime saleDate;
    private Integer remainingStock;
    private String userName;
}