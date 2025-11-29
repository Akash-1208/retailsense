package com.retailsense.ai.scheduler;

import com.retailsense.ai.service.AIInsightsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIInsightsScheduler {

    private final AIInsightsService aiInsightsService;

    @Scheduled(cron = "0 0 3 * * *") // Every day at 3 AM
    public void generateDailyInsights() {
        log.info("Starting scheduled AI insights generation");
        aiInsightsService.generateInsightsForAllProducts();
        log.info("Completed scheduled AI insights generation");
    }
}