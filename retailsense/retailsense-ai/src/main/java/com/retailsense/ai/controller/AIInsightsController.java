package com.retailsense.ai.controller;

import com.retailsense.ai.dto.AIInsightResponse;
import com.retailsense.ai.service.AIInsightsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/insights")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AIInsightsController {

    private final AIInsightsService aiInsightsService;

    @GetMapping
    public ResponseEntity<List<AIInsightResponse>> getAllInsights(
            @RequestParam(required = false) String priority
    ) {
        log.info("GET /api/ai/insights?priority={}", priority);
        return ResponseEntity.ok(aiInsightsService.getAllInsights(priority));
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateInsights() {
        log.info("POST /api/ai/insights/generate - Manual trigger");
        aiInsightsService.generateInsightsForAllProducts();
        return ResponseEntity.ok(Map.of("message", "AI insights generation started"));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<AIInsightResponse> getInsightForProduct(@PathVariable Long productId) {
        log.info("GET /api/ai/insights/product/{}", productId);
        return ResponseEntity.ok(aiInsightsService.generateInsightForProduct(productId));
    }
}