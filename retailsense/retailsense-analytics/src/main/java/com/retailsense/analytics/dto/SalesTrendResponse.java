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
public class SalesTrendResponse {
    private String period;
    private List<DailyData> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyData {
        private String date;
        private BigDecimal sales;
        private Integer transactions;
    }
}