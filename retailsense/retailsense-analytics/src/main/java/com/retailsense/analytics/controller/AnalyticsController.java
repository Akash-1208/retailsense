package com.retailsense.analytics.controller;

import com.retailsense.analytics.dto.*;
import com.retailsense.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/sales-trend")
    public ResponseEntity<SalesTrendResponse> getSalesTrend(
            @RequestParam(defaultValue = "7") int days
    ) {
        log.info("GET /api/analytics/sales-trend?days={}", days);
        return ResponseEntity.ok(analyticsService.getSalesTrend(days));
    }

    @GetMapping("/top-products")
    public ResponseEntity<TopProductsResponse> getTopProducts(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "revenue") String sortBy
    ) {
        log.info("GET /api/analytics/top-products?limit={}&sortBy={}", limit, sortBy);
        return ResponseEntity.ok(analyticsService.getTopProducts(limit, sortBy));
    }

    @GetMapping("/category-distribution")
    public ResponseEntity<CategoryDistributionResponse> getCategoryDistribution() {
        log.info("GET /api/analytics/category-distribution");
        return ResponseEntity.ok(analyticsService.getCategoryDistribution());
    }

    @GetMapping("/revenue-summary")
    public ResponseEntity<RevenueSummaryResponse> getRevenueSummary() {
        log.info("GET /api/analytics/revenue-summary");
        return ResponseEntity.ok(analyticsService.getRevenueSummary());
    }
}