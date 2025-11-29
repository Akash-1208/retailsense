package com.retailsense.analytics.service;

import com.retailsense.analytics.dto.*;
import com.retailsense.product.repository.ProductRepository;
import com.retailsense.sales.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AnalyticsService {

    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;

    public SalesTrendResponse getSalesTrend(int days) {
        log.info("Calculating sales trend for last {} days", days);

        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        var sales = salesRepository.findRecentSales(startDate);

        Map<LocalDate, SalesTrendResponse.DailyData> dailyMap = new TreeMap<>();

        for (var sale : sales) {
            LocalDate date = sale.getSaleDate().toLocalDate();
            dailyMap.computeIfAbsent(date, k -> new SalesTrendResponse.DailyData(
                    date.toString(), BigDecimal.ZERO, 0
            ));

            SalesTrendResponse.DailyData data = dailyMap.get(date);
            data.setSales(data.getSales().add(sale.getTotalRevenue()));
            data.setTransactions(data.getTransactions() + 1);
        }

        List<SalesTrendResponse.DailyData> dataList = new ArrayList<>(dailyMap.values());

        return SalesTrendResponse.builder()
                .period("last_" + days + "_days")
                .data(dataList)
                .build();
    }

    public TopProductsResponse getTopProducts(int limit, String sortBy) {
        log.info("Fetching top {} products sorted by {}", limit, sortBy);

        var allSales = salesRepository.findAll();
        Map<Long, TopProductsResponse.ProductSales> productSalesMap = new HashMap<>();

        for (var sale : allSales) {
            Long productId = sale.getProduct().getId();
            productSalesMap.computeIfAbsent(productId, k -> TopProductsResponse.ProductSales.builder()
                    .productId(productId)
                    .productName(sale.getProduct().getName())
                    .category(sale.getProduct().getCategory())
                    .totalRevenue(BigDecimal.ZERO)
                    .totalUnitsSold(0)
                    .salesCount(0)
                    .profitMargin(sale.getProduct().getProfitMargin())
                    .build());

            TopProductsResponse.ProductSales ps = productSalesMap.get(productId);
            ps.setTotalRevenue(ps.getTotalRevenue().add(sale.getTotalRevenue()));
            ps.setTotalUnitsSold(ps.getTotalUnitsSold() + sale.getQuantitySold());
            ps.setSalesCount(ps.getSalesCount() + 1);
        }

        Comparator<TopProductsResponse.ProductSales> comparator = switch (sortBy) {
            case "quantity" -> Comparator.comparing(TopProductsResponse.ProductSales::getTotalUnitsSold).reversed();
            case "frequency" -> Comparator.comparing(TopProductsResponse.ProductSales::getSalesCount).reversed();
            default -> Comparator.comparing(TopProductsResponse.ProductSales::getTotalRevenue).reversed();
        };

        List<TopProductsResponse.ProductSales> topProducts = productSalesMap.values().stream()
                .sorted(comparator)
                .limit(limit)
                .collect(Collectors.toList());

        return TopProductsResponse.builder()
                .products(topProducts)
                .build();
    }

    public CategoryDistributionResponse getCategoryDistribution() {
        log.info("Calculating category distribution");

        var allSales = salesRepository.findAll();
        Map<String, BigDecimal> categoryRevenue = new HashMap<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (var sale : allSales) {
            String category = sale.getProduct().getCategory();
            categoryRevenue.merge(category, sale.getTotalRevenue(), BigDecimal::add);
            totalRevenue = totalRevenue.add(sale.getTotalRevenue());
        }

        List<CategoryDistributionResponse.CategoryData> categories = new ArrayList<>();
        BigDecimal finalTotalRevenue = totalRevenue;

        categoryRevenue.forEach((category, revenue) -> {
            long productCount = productRepository.findByCategory(category).size();
            double percentage = finalTotalRevenue.compareTo(BigDecimal.ZERO) > 0
                    ? revenue.divide(finalTotalRevenue, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).doubleValue()
                    : 0.0;

            categories.add(CategoryDistributionResponse.CategoryData.builder()
                    .category(category)
                    .totalRevenue(revenue)
                    .totalProducts(productCount)
                    .percentage(percentage)
                    .build());
        });

        return CategoryDistributionResponse.builder()
                .categories(categories)
                .build();
    }

    public RevenueSummaryResponse getRevenueSummary() {
        log.info("Calculating revenue summary");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime startOfWeek = now.minusDays(7);
        LocalDateTime startOfMonth = now.minusDays(30);

        return RevenueSummaryResponse.builder()
                .today(calculatePeriodSummary(startOfToday))
                .week(calculatePeriodSummary(startOfWeek))
                .month(calculatePeriodSummary(startOfMonth))
                .build();
    }

    private RevenueSummaryResponse.PeriodSummary calculatePeriodSummary(LocalDateTime startDate) {
        var sales = salesRepository.findRecentSales(startDate);

        BigDecimal revenue = sales.stream()
                .map(sale -> sale.getTotalRevenue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profit = sales.stream()
                .map(sale -> {
                    BigDecimal costPerUnit = sale.getProduct().getPurchasePrice();
                    BigDecimal totalCost = costPerUnit.multiply(BigDecimal.valueOf(sale.getQuantitySold()));
                    return sale.getTotalRevenue().subtract(totalCost);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RevenueSummaryResponse.PeriodSummary.builder()
                .revenue(revenue)
                .profit(profit)
                .transactions((long) sales.size())
                .build();
    }
}