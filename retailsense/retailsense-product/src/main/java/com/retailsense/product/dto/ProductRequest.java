package com.retailsense.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Purchase price must be greater than 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Selling price must be greater than 0")
    private BigDecimal sellingPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 1, message = "Minimum threshold must be at least 1")
    private Integer minimumThreshold = 10;

    // Custom validation
    public boolean isSellingPriceValid() {
        return sellingPrice.compareTo(purchasePrice) >= 0;
    }
}


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ProductResponse {

    private Long id;
    private String name;
    private String category;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Integer quantity;
    private Integer minimumThreshold;
    private BigDecimal profitMargin;
    private String stockStatus;
    private String createdAt;
    private String updatedAt;
}



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ProductSummary {

    private Long id;
    private String name;
    private String category;
    private BigDecimal sellingPrice;
    private Integer quantity;
    private String stockStatus;
    private BigDecimal profitMargin;
}