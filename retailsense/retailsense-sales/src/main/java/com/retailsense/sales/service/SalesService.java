package com.retailsense.sales.service;

import com.retailsense.common.exception.InsufficientStockException;
import com.retailsense.common.model.User;
import com.retailsense.product.model.Product;
import com.retailsense.product.service.ProductService;
import com.retailsense.sales.dto.SaleRequest;
import com.retailsense.sales.dto.SaleResponse;
import com.retailsense.sales.dto.SalesSummary;
import com.retailsense.sales.model.Sale;
import com.retailsense.sales.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalesService {

    private final SalesRepository salesRepository;
    private final ProductService productService;

    public SaleResponse recordSale(SaleRequest request, Long userId) {
        log.info("Recording sale for product: {}, quantity: {}", request.getProductId(), request.getQuantitySold());

        // Get product
        Product product = productService.getProductById(request.getProductId());

        // Check stock
        if (product.getQuantity() < request.getQuantitySold()) {
            throw new InsufficientStockException(product.getQuantity(), request.getQuantitySold());
        }

        // Create sale
        Sale sale = Sale.builder()
                .product(product)
                .user(User.builder().build())
                .quantitySold(request.getQuantitySold())
                .salePrice(product.getSellingPrice())
                .saleDate(LocalDateTime.now())
                .build();

        // Calculate total revenue
        sale.setTotalRevenue(product.getSellingPrice().multiply(BigDecimal.valueOf(request.getQuantitySold())));

        Sale savedSale = salesRepository.save(sale);

        // Reduce product stock
        productService.reduceStock(product.getId(), request.getQuantitySold());

        log.info("Sale recorded successfully: {}", savedSale.getId());

        // Get updated product
        Product updatedProduct = productService.getProductById(product.getId());

        return SaleResponse.builder()
                .id(savedSale.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productCategory(product.getCategory())
                .quantitySold(savedSale.getQuantitySold())
                .salePrice(savedSale.getSalePrice())
                .totalRevenue(savedSale.getTotalRevenue())
                .saleDate(savedSale.getSaleDate())
                .remainingStock(updatedProduct.getQuantity())
                .userName("User")
                .build();
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesHistory(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching sales history from {} to {}", startDate, endDate);

        List<Sale> sales;
        if (startDate != null && endDate != null) {
            sales = salesRepository.findBySaleDateBetween(startDate, endDate);
        } else if (startDate != null) {
            sales = salesRepository.findRecentSales(startDate);
        } else {
            sales = salesRepository.findAll();
        }

        return sales.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SalesSummary getSalesSummary(String period) {
        log.info("Calculating sales summary for period: {}", period);

        LocalDateTime startDate = getStartDateForPeriod(period);

        BigDecimal totalRevenue = salesRepository.calculateRevenueFrom(startDate);
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        Long totalTransactions = salesRepository.countSalesFrom(startDate);

        List<Sale> sales = salesRepository.findRecentSales(startDate);
        Integer totalUnitsSold = sales.stream()
                .mapToInt(Sale::getQuantitySold)
                .sum();

        BigDecimal avgTransactionValue = totalTransactions > 0
                ? totalRevenue.divide(BigDecimal.valueOf(totalTransactions), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return SalesSummary.builder()
                .period(period)
                .totalRevenue(totalRevenue)
                .totalTransactions(totalTransactions)
                .totalUnitsSold(totalUnitsSold)
                .averageTransactionValue(avgTransactionValue)
                .build();
    }

    private LocalDateTime getStartDateForPeriod(String period) {
        return switch (period.toLowerCase()) {
            case "today" -> LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            case "week" -> LocalDateTime.now().minusDays(7);
            case "month" -> LocalDateTime.now().minusDays(30);
            default -> LocalDateTime.now().minusDays(7);
        };
    }

    private SaleResponse mapToResponse(Sale sale) {
        return SaleResponse.builder()
                .id(sale.getId())
                .productId(sale.getProduct().getId())
                .productName(sale.getProduct().getName())
                .productCategory(sale.getProduct().getCategory())
                .quantitySold(sale.getQuantitySold())
                .salePrice(sale.getSalePrice())
                .totalRevenue(sale.getTotalRevenue())
                .saleDate(sale.getSaleDate())
                .userName("User")
                .build();
    }
}
