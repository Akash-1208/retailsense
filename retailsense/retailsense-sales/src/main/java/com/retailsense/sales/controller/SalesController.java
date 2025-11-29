package com.retailsense.sales.controller;

import com.retailsense.sales.dto.SaleRequest;
import com.retailsense.sales.dto.SaleResponse;
import com.retailsense.sales.dto.SalesSummary;
import com.retailsense.sales.service.SalesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SalesController {

    private final SalesService salesService;

    @PostMapping
    public ResponseEntity<SaleResponse> recordSale(@Valid @RequestBody SaleRequest request) {
        log.info("POST /api/sales - Recording sale for product: {}", request.getProductId());
        // TODO: Get userId from SecurityContext when auth is implemented
        Long userId = 1L; // Hardcoded for now
        SaleResponse response = salesService.recordSale(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getSalesHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        log.info("GET /api/sales - from: {}, to: {}", from, to);
        List<SaleResponse> sales = salesService.getSalesHistory(from, to);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/summary")
    public ResponseEntity<SalesSummary> getSalesSummary(
            @RequestParam(defaultValue = "week") String period
    ) {
        log.info("GET /api/sales/summary - period: {}", period);
        SalesSummary summary = salesService.getSalesSummary(period);
        return ResponseEntity.ok(summary);
    }
}