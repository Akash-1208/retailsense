package com.retailsense.product.service;

import com.retailsense.product.dto.ProductRequest;
import com.retailsense.product.model.Product;
import com.retailsense.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Get all products
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    /**
     * Get product by ID
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Create new product
     */
    public Product createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        // Validate selling price >= purchase price
        if (!request.isSellingPriceValid()) {
            throw new IllegalArgumentException("Selling price must be greater than or equal to purchase price");
        }

        Product product = Product.builder()
                .name(request.getName())
                .category(request.getCategory())
                .purchasePrice(request.getPurchasePrice())
                .sellingPrice(request.getSellingPrice())
                .quantity(request.getQuantity())
                .minimumThreshold(request.getMinimumThreshold())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return savedProduct;
    }

    /**
     * Update existing product
     */
    public Product updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with id: {}", id);

        Product existingProduct = getProductById(id);

        // Validate selling price
        if (!request.isSellingPriceValid()) {
            throw new IllegalArgumentException("Selling price must be greater than or equal to purchase price");
        }

        existingProduct.setName(request.getName());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setPurchasePrice(request.getPurchasePrice());
        existingProduct.setSellingPrice(request.getSellingPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setMinimumThreshold(request.getMinimumThreshold());

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully: {}", updatedProduct.getId());
        return updatedProduct;
    }

    /**
     * Delete product
     */
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
        log.info("Product deleted successfully: {}", id);
    }

    /**
     * Get products by category
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        log.info("Fetching products for category: {}", category);
        return productRepository.findByCategory(category);
    }

    /**
     * Get low stock products
     */
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        log.info("Fetching low stock products");
        return productRepository.findLowStockProducts();
    }

    /**
     * Search products by name
     */
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String searchTerm) {
        log.info("Searching products with term: {}", searchTerm);
        return productRepository.searchByName(searchTerm);
    }

    /**
     * Get all categories
     */
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        log.info("Fetching all categories");
        return productRepository.findAllCategories();
    }

    /**
     * Reduce product stock (called by Sales module)
     */
    public void reduceStock(Long productId, Integer quantity) {
        log.info("Reducing stock for product id: {} by quantity: {}", productId, quantity);

        Product product = getProductById(productId);

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getQuantity() + ", Requested: " + quantity);
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        log.info("Stock reduced successfully. New quantity: {}", product.getQuantity());
    }

    /**
     * Increase product stock (for restocking)
     */
    public void increaseStock(Long productId, Integer quantity) {
        log.info("Increasing stock for product id: {} by quantity: {}", productId, quantity);

        Product product = getProductById(productId);
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);

        log.info("Stock increased successfully. New quantity: {}", product.getQuantity());
    }

    /**
     * Get count of low stock products
     */
    @Transactional(readOnly = true)
    public Long getLowStockCount() {
        return productRepository.countLowStockProducts();
    }
}