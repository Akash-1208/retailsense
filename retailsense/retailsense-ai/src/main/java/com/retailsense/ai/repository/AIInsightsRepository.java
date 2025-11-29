package com.retailsense.ai.repository;

import com.retailsense.ai.model.AIInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AIInsightsRepository extends JpaRepository<AIInsight, Long> {
    Optional<AIInsight> findByProductId(Long productId);
    List<AIInsight> findByPriority(AIInsight.Priority priority);
}