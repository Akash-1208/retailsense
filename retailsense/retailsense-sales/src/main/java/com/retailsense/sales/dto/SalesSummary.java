package com.retailsense.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesSummary {
    private String period;
    private BigDecimal totalRevenue;
    private Long totalTransactions;
    private Integer totalUnitsSold;
    private BigDecimal averageTransactionValue;
}