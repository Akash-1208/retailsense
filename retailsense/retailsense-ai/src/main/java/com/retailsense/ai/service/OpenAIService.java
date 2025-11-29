package com.retailsense.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OpenAIService {

    @Value("${openai.api.key:}")
    private String apiKey;

    public String generateInsight(String prompt) {
        // TODO: Implement actual OpenAI API call
        // For now, return a simple generated message
        log.info("Generating AI insight (placeholder)");

        if (prompt.contains("HIGH")) {
            return "High sales velocity detected. Stock levels critically low. Immediate reorder recommended to prevent stockout.";
        } else if (prompt.contains("MEDIUM")) {
            return "Moderate sales pattern observed. Stock approaching minimum threshold. Plan reorder within this week.";
        } else {
            return "Stable inventory levels. Sales velocity is consistent. Current stock adequate for 2+ weeks.";
        }
    }
}