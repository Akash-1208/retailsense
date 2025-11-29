package com.retailsense.ai.service;

import com.retailsense.ai.dto.AIInsightResponse;
import com.retailsense.ai.model.AIInsight;
import com.retailsense.ai.repository.AIInsightsRepository;
import com.retailsense.product.model.Product;
import com.retailsense.product.service.ProductService;
import com.retailsense.sales.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AIInsightsService {

    private final AIInsightsRepository insightsRepository;
    private final ProductService productService;
    private final SalesRepository salesRepository;
    private final OpenAIService openAIService;

    public void generateInsightsForAllProducts() {
        log.info("Starting AI insights generation for all products");

        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            try {
                generateInsightForProduct(product.getId());
                Thread.sleep(500); // Rate limiting
            } catch (Exception e) {
                log.error("Error generating insight for product {}: {}", product.getId(), e.getMessage());
            }
        }

        log.info("Completed AI insights generation");
    }

    public AIInsightResponse generateInsightForProduct(Long productId) {
        log.info("Generating AI insight for product: {}", productId);

        Product product = productService.getProductById(productId);

        // Get sales data for last 14 days
        LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);
        Integer totalSold = salesRepository.getTotalQuantitySold(productId, fourteenDaysAgo);
        if (totalSold == null) totalSold = 0;

        // Calculate daily average
        double dailyAverage = totalSold / 14.0;

        // Calculate days until stockout
        int daysUntilStockout = dailyAverage > 0
                ? (int) Math.ceil(product.getQuantity() / dailyAverage)
                : 999;

        // Determine priority
        AIInsight.Priority priority;
        if (daysUntilStockout <= 3) {
            priority = AIInsight.Priority.HIGH;
        } else if (daysUntilStockout <= 7) {
            priority = AIInsight.Priority.MEDIUM;
        } else {
            priority = AIInsight.Priority.LOW;
        }

        // Calculate recommended reorder quantity
        int recommendedQty = (int) Math.ceil(dailyAverage * 14); // 2 weeks supply

        // Generate AI-powered reasoning
        String prompt = buildPrompt(product, totalSold, dailyAverage, daysUntilStockout);
        String aiReasoning = openAIService.generateInsight(prompt);

        // Create or update insight
        AIInsight insight = insightsRepository.findByProductId(productId)
                .orElse(AIInsight.builder().product(product).build());

        insight.setDaysUntilStockout(daysUntilStockout);
        insight.setRecommendedReorderQty(recommendedQty);
        insight.setPriority(priority);
        insight.setReason(aiReasoning);
        insight.setAction(generateAction(priority, daysUntilStockout, recommendedQty));
        insight.setConfidenceScore(BigDecimal.valueOf(0.85));

        insightsRepository.save(insight);

        return mapToResponse(insight);
    }

    @Transactional(readOnly = true)
    public List<AIInsightResponse> getAllInsights(String priorityFilter) {
        List<AIInsight> insights;

        if (priorityFilter != null && !priorityFilter.isEmpty()) {
            AIInsight.Priority priority = AIInsight.Priority.valueOf(priorityFilter.toUpperCase());
            insights = insightsRepository.findByPriority(priority);
        } else {
            insights = insightsRepository.findAll();
        }

        return insights.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String buildPrompt(Product product, int totalSold, double dailyAverage, int daysUntilStockout) {
        return String.format(
                "Product: %s (Category: %s)\nCurrent Stock: %d\nSales last 14 days: %d units\n" +
                        "Daily average: %.1f units\nDays until stockout: %d\nProfit margin: %.1f%%\n\n" +
                        "Provide a brief insight (max 100 words) about this product's inventory status.",
                product.getName(), product.getCategory(), product.getQuantity(),
                totalSold, dailyAverage, daysUntilStockout, product.getProfitMargin()
        );
    }

    private String generateAction(AIInsight.Priority priority, int days, int qty) {
        return switch (priority) {
            case HIGH -> String.format("URGENT: Reorder %d units immediately to avoid stockout in %d days", qty, days);
            case MEDIUM -> String.format("Reorder %d units within next few days (stockout in %d days)", qty, days);
            case LOW -> String.format("Stock level adequate. Consider reordering %d units for optimal inventory", qty);
        };
    }

    private AIInsightResponse mapToResponse(AIInsight insight) {
        return AIInsightResponse.builder()
                .id(insight.getId())
                .productId(insight.getProduct().getId())
                .productName(insight.getProduct().getName())
                .currentStock(insight.getProduct().getQuantity())
                .daysUntilStockout(insight.getDaysUntilStockout())
                .recommendedReorderQty(insight.getRecommendedReorderQty())
                .priority(insight.getPriority().name())
                .reason(insight.getReason())
                .action(insight.getAction())
                .confidenceScore(insight.getConfidenceScore())
                .generatedAt(insight.getCreatedAt())
                .build();
    }
}