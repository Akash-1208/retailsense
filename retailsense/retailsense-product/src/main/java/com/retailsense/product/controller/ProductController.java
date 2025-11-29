package com.retailsense.product.controller;

import com.retailsense.product.dto.ProductRequest;
import com.retailsense.product.model.Product;
import com.retailsense.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // For development - configure properly for production
public class ProductController {

    private final ProductService productService;

    /**
     * GET /api/products - Get all products
     * Query params: category (optional), lowStock (optional), search (optional)
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean lowStock,
            @RequestParam(required = false) String search
    ) {
        log.info("GET /api/products - category: {}, lowStock: {}, search: {}", category, lowStock, search);

        List<Product> products;

        if (search != null && !search.isEmpty()) {
            products = productService.searchProducts(search);
        } else if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category);
        } else if (lowStock != null && lowStock) {
            products = productService.getLowStockProducts();
        } else {
            products = productService.getAllProducts();
        }

        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id} - Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{}", id);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * POST /api/products - Create new product
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("POST /api/products - Creating product: {}", request.getName());
        Product createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * PUT /api/products/{id} - Update product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        log.info("PUT /api/products/{} - Updating product", id);
        Product updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * DELETE /api/products/{id} - Delete product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{}", id);
        productService.deleteProduct(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        response.put("id", id.toString());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/categories - Get all categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        log.info("GET /api/products/categories");
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/products/stats - Get product statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getProductStats() {
        log.info("GET /api/products/stats");

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productService.getAllProducts().size());
        stats.put("lowStockCount", productService.getLowStockCount());
        stats.put("categories", productService.getAllCategories().size());

        return ResponseEntity.ok(stats);
    }

    /**
     * PATCH /api/products/{id}/stock - Update stock quantity
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request
    ) {
        log.info("PATCH /api/products/{}/stock", id);

        Integer quantity = request.get("quantity");
        String action = request.getOrDefault("action", 0).toString();

        if ("increase".equals(action)) {
            productService.increaseStock(id, quantity);
        } else if ("decrease".equals(action)) {
            productService.reduceStock(id, quantity);
        } else {
            // Set absolute quantity
            Product product = productService.getProductById(id);
            ProductRequest updateRequest = ProductRequest.builder()
                    .name(product.getName())
                    .category(product.getCategory())
                    .purchasePrice(product.getPurchasePrice())
                    .sellingPrice(product.getSellingPrice())
                    .quantity(quantity)
                    .minimumThreshold(product.getMinimumThreshold())
                    .build();
            return ResponseEntity.ok(productService.updateProduct(id, updateRequest));
        }

        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Exception handler for this controller
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        log.error("Error in ProductController: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Validation error: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}