package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;
    
    // Create operations
    @Transactional
    @CacheEvict(value = {"products", "categories"}, allEntries = true)
    public Product createProduct(Product product) {
        log.info("Creating new product with SKU: {}", product.getSku());
        
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("Product already exists with SKU: " + product.getSku());
        }
        
        product.setIsActive(true);
        product.setViewCount(0L);
        product.setSalesCount(0L);
        product.setAverageRating(BigDecimal.ZERO);
        product.setReviewCount(0);
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return savedProduct;
    }
    
    // Read operations
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }
    
    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }
    
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }
    
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with SKU: " + sku));
    }
    
    @Cacheable(value = "products", key = "'all-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Cacheable(value = "products", key = "'active-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Product> getActiveProducts(Pageable pageable) {
        return productRepository.findByIsActive(true, pageable);
    }
    
    @Cacheable(value = "products", key = "'category-' + #category + '-' + #pageable.pageNumber")
    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }
    
    public Page<Product> getProductsByCategoryAndSubcategory(String category, String subcategory, Pageable pageable) {
        return productRepository.findByCategoryAndSubcategory(category, subcategory, pageable);
    }
    
    public Page<Product> getProductsByBrand(String brand, Pageable pageable) {
        return productRepository.findByBrand(brand, pageable);
    }
    
    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }
    
    public Page<Product> getInStockProducts(Pageable pageable) {
        return productRepository.findInStockProducts(pageable);
    }
    
    @Cacheable(value = "products", key = "'featured-' + #pageable.pageNumber")
    public Page<Product> getFeaturedProducts(Pageable pageable) {
        return productRepository.findByIsFeatured(true, pageable);
    }
    
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchActiveProducts(keyword, pageable);
    }
    
    // Update operations
    @Transactional
    @CacheEvict(value = {"products", "categories"}, allEntries = true)
    public Product updateProduct(String id, Product productDetails) {
        log.info("Updating product with ID: {}", id);
        
        Product product = getProductById(id);
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setOriginalPrice(productDetails.getOriginalPrice());
        product.setMainImage(productDetails.getMainImage());
        product.setImages(productDetails.getImages());
        product.setCategory(productDetails.getCategory());
        product.setSubcategory(productDetails.getSubcategory());
        product.setBrand(productDetails.getBrand());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setLowStockThreshold(productDetails.getLowStockThreshold());
        product.setWeight(productDetails.getWeight());
        product.setDimensions(productDetails.getDimensions());
        product.setTags(productDetails.getTags());
        product.setSpecifications(productDetails.getSpecifications());
        product.setVariants(productDetails.getVariants());
        product.setMetaTitle(productDetails.getMetaTitle());
        product.setMetaDescription(productDetails.getMetaDescription());
        product.setMetaKeywords(productDetails.getMetaKeywords());
        
        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        return updatedProduct;
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product updateStock(String id, Integer newStock) {
        log.info("Updating stock for product ID: {} to: {}", id, newStock);
        
        Product product = getProductById(id);
        product.setStockQuantity(newStock);
        
        Product updatedProduct = productRepository.save(product);
        log.info("Stock updated successfully for product ID: {}", id);
        return updatedProduct;
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product updatePrice(String id, BigDecimal newPrice) {
        log.info("Updating price for product ID: {} to: {}", id, newPrice);
        
        Product product = getProductById(id);
        product.setPrice(newPrice);
        
        Product updatedProduct = productRepository.save(product);
        log.info("Price updated successfully for product ID: {}", id);
        return updatedProduct;
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product activateProduct(String id) {
        log.info("Activating product with ID: {}", id);
        
        Product product = getProductById(id);
        product.setIsActive(true);
        
        Product activatedProduct = productRepository.save(product);
        log.info("Product activated successfully with ID: {}", id);
        return activatedProduct;
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product deactivateProduct(String id) {
        log.info("Deactivating product with ID: {}", id);
        
        Product product = getProductById(id);
        product.setIsActive(false);
        
        Product deactivatedProduct = productRepository.save(product);
        log.info("Product deactivated successfully with ID: {}", id);
        return deactivatedProduct;
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product featureProduct(String id) {
        log.info("Featuring product with ID: {}", id);
        
        Product product = getProductById(id);
        product.setIsFeatured(true);
        
        Product featuredProduct = productRepository.save(product);
        log.info("Product featured successfully with ID: {}", id);
        return featuredProduct;
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product unfeatureProduct(String id) {
        log.info("Unfeaturing product with ID: {}", id);
        
        Product product = getProductById(id);
        product.setIsFeatured(false);
        
        Product unfeaturedProduct = productRepository.save(product);
        log.info("Product unfeatured successfully with ID: {}", id);
        return unfeaturedProduct;
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product incrementViewCount(String id) {
        Product product = getProductById(id);
        product.incrementViewCount();
        return productRepository.save(product);
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product incrementSalesCount(String id, int quantity) {
        Product product = getProductById(id);
        product.incrementSalesCount(quantity);
        return productRepository.save(product);
    }
    
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product updateRating(String id, BigDecimal averageRating, int reviewCount) {
        Product product = getProductById(id);
        product.updateRating(averageRating, reviewCount);
        return productRepository.save(product);
    }
    
    // Delete operations
    @Transactional
    @CacheEvict(value = {"products", "categories"}, allEntries = true)
    public void deleteProduct(String id) {
        log.info("Deleting product with ID: {}", id);
        
        Product product = getProductById(id);
        productRepository.delete(product);
        
        log.info("Product deleted successfully with ID: {}", id);
    }
    
    // Utility methods
    public boolean existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }
    
    public long getTotalProductCount() {
        return productRepository.count();
    }
    
    public long getActiveProductCount() {
        return productRepository.countByIsActive(true);
    }
    
    public long getInStockProductCount() {
        return productRepository.countInStockProducts();
    }
    
    public long getOutOfStockProductCount() {
        return productRepository.countOutOfStockProducts();
    }
    
    public long getProductCountByCategory(String category) {
        return productRepository.countByCategory(category);
    }
    
    public long getProductCountByBrand(String brand) {
        return productRepository.countByBrand(brand);
    }
    
    // Popular and trending products
    @Cacheable(value = "products", key = "'most-viewed-' + #pageable.pageNumber")
    public List<Product> getMostViewedProducts(Pageable pageable) {
        return productRepository.findMostViewedProducts(pageable);
    }
    
    @Cacheable(value = "products", key = "'best-selling-' + #pageable.pageNumber")
    public List<Product> getBestSellingProducts(Pageable pageable) {
        return productRepository.findBestSellingProducts(pageable);
    }
    
    @Cacheable(value = "products", key = "'top-rated-' + #pageable.pageNumber")
    public List<Product> getTopRatedProducts(Pageable pageable) {
        return productRepository.findTopRatedProducts(pageable);
    }
    
    @Cacheable(value = "products", key = "'recent-' + #pageable.pageNumber")
    public List<Product> getRecentProducts(Pageable pageable) {
        return productRepository.findRecentProducts(pageable);
    }
    
    public List<Product> getRelatedProducts(String productId, Pageable pageable) {
        Product product = getProductById(productId);
        return productRepository.findRelatedProductsByCategory(product.getCategory(), productId, pageable);
    }
    
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }
    
    public List<Product> getProductsWithDiscount() {
        return productRepository.findProductsWithDiscount();
    }
    
    public List<String> getDistinctCategories() {
        return productRepository.findDistinctCategories();
    }
    
    public List<String> getDistinctBrands() {
        return productRepository.findDistinctBrands();
    }
    
    public List<String> getDistinctSubcategoriesByCategory(String category) {
        return productRepository.findDistinctSubcategoriesByCategory(category);
    }
}
