package com.retailsense.sales.repository;

import com.retailsense.sales.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByProductId(Long productId);

    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Sale s WHERE s.saleDate >= :startDate ORDER BY s.saleDate DESC")
    List<Sale> findRecentSales(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(s.totalRevenue) FROM Sale s WHERE s.saleDate >= :startDate")
    BigDecimal calculateRevenueFrom(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate >= :startDate")
    Long countSalesFrom(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(s.quantitySold) FROM Sale s WHERE s.product.id = :productId AND s.saleDate >= :startDate")
    Integer getTotalQuantitySold(@Param("productId") Long productId, @Param("startDate") LocalDateTime startDate);
}
