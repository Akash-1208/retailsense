package com.retailsense.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueSummaryResponse {
    private PeriodSummary today;
    private PeriodSummary week;
    private PeriodSummary month;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PeriodSummary {
        private BigDecimal revenue;
        private BigDecimal profit;
        private Long transactions;
    }
}
