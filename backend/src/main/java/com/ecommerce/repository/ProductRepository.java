package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
    // Basic queries
    Optional<Product> findBySku(String sku);
    
    boolean existsBySku(String sku);
    
    List<Product> findByName(String name);
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Category queries
    List<Product> findByCategory(String category);
    
    Page<Product> findByCategory(String category, Pageable pageable);
    
    List<Product> findBySubcategory(String subcategory);
    
    Page<Product> findBySubcategory(String subcategory, Pageable pageable);
    
    List<Product> findByCategoryAndSubcategory(String category, String subcategory);
    
    Page<Product> findByCategoryAndSubcategory(String category, String subcategory, Pageable pageable);
    
    // Brand queries
    List<Product> findByBrand(String brand);
    
    Page<Product> findByBrand(String brand, Pageable pageable);
    
    List<Product> findByBrandIn(List<String> brands);
    
    // Price queries
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    List<Product> findByPriceGreaterThanEqual(BigDecimal minPrice);
    
    List<Product> findByPriceLessThanEqual(BigDecimal maxPrice);
    
    // Stock queries
    List<Product> findByStockQuantityGreaterThan(Integer quantity);
    
    List<Product> findByStockQuantityLessThanEqual(Integer quantity);
    
    @Query("{'stockQuantity': {$gt: 0}}")
    List<Product> findInStockProducts();
    
    @Query("{'stockQuantity': {$gt: 0}}")
    Page<Product> findInStockProducts(Pageable pageable);
    
    @Query("{'stockQuantity': 0}")
    List<Product> findOutOfStockProducts();
    
    @Query("{'stockQuantity': {$lte: ?0}}")
    List<Product> findLowStockProducts(Integer threshold);
    
    // Status queries
    List<Product> findByIsActive(Boolean isActive);
    
    Page<Product> findByIsActive(Boolean isActive, Pageable pageable);
    
    List<Product> findByIsFeatured(Boolean isFeatured);
    
    Page<Product> findByIsFeatured(Boolean isFeatured, Pageable pageable);
    
    List<Product> findByIsDigital(Boolean isDigital);
    
    List<Product> findByIsActiveAndIsFeatured(Boolean isActive, Boolean isFeatured);
    
    // Rating queries
    List<Product> findByAverageRatingGreaterThanEqual(BigDecimal rating);
    
    Page<Product> findByAverageRatingGreaterThanEqual(BigDecimal rating, Pageable pageable);
    
    List<Product> findByReviewCountGreaterThan(Integer reviewCount);
    
    // Search queries
    @Query("{'$or': [" +
           "{'name': {$regex: ?0, $options: 'i'}}, " +
           "{'description': {$regex: ?0, $options: 'i'}}, " +
           "{'tags': {$regex: ?0, $options: 'i'}}" +
           "]}")
    Page<Product> searchProducts(String keyword, Pageable pageable);
    
    @Query("{'$and': [" +
           "{'isActive': true}, " +
           "{'$or': [" +
           "{'name': {$regex: ?0, $options: 'i'}}, " +
           "{'description': {$regex: ?0, $options: 'i'}}, " +
           "{'tags': {$regex: ?0, $options: 'i'}}" +
           "]}" +
           "]}")
    Page<Product> searchActiveProducts(String keyword, Pageable pageable);
    
    // Tag queries
    @Query("{'tags': {$in: ?0}}")
    List<Product> findByTagsIn(List<String> tags);
    
    @Query("{'tags': ?0}")
    List<Product> findByTag(String tag);
    
    // Complex filter queries
    @Query("{'$and': [" +
           "{'category': ?0}, " +
           "{'price': {$gte: ?1, $lte: ?2}}, " +
           "{'isActive': true}, " +
           "{'stockQuantity': {$gt: 0}}" +
           "]}")
    Page<Product> findByCategoryAndPriceRangeAndInStock(String category, 
                                                       BigDecimal minPrice, 
                                                       BigDecimal maxPrice, 
                                                       Pageable pageable);
    
    @Query("{'$and': [" +
           "{'brand': {$in: ?0}}, " +
           "{'category': ?1}, " +
           "{'price': {$gte: ?2, $lte: ?3}}, " +
           "{'isActive': true}" +
           "]}")
    Page<Product> findByBrandsAndCategoryAndPriceRange(List<String> brands,
                                                      String category,
                                                      BigDecimal minPrice,
                                                      BigDecimal maxPrice,
                                                      Pageable pageable);
    
    // Date queries
    List<Product> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'createdAt': {$gte: ?0}}")
    List<Product> findProductsCreatedAfter(LocalDateTime date);
    
    @Query("{'updatedAt': {$gte: ?0}}")
    List<Product> findProductsUpdatedAfter(LocalDateTime date);
    
    // Statistics queries
    long countByCategory(String category);
    
    long countByBrand(String brand);
    
    long countByIsActive(Boolean isActive);
    
    long countByIsFeatured(Boolean isFeatured);
    
    @Query(value = "{'stockQuantity': {$gt: 0}}", count = true)
    long countInStockProducts();
    
    @Query(value = "{'stockQuantity': 0}", count = true)
    long countOutOfStockProducts();
    
    // Popular products
    @Query(value = "{}", sort = "{'viewCount': -1}")
    List<Product> findMostViewedProducts(Pageable pageable);
    
    @Query(value = "{}", sort = "{'salesCount': -1}")
    List<Product> findBestSellingProducts(Pageable pageable);
    
    @Query(value = "{'isActive': true}", sort = "{'averageRating': -1, 'reviewCount': -1}")
    List<Product> findTopRatedProducts(Pageable pageable);
    
    @Query(value = "{'isFeatured': true, 'isActive': true}", sort = "{'createdAt': -1}")
    List<Product> findFeaturedProducts(Pageable pageable);
    
    // Recent products
    @Query(value = "{'isActive': true}", sort = "{'createdAt': -1}")
    List<Product> findRecentProducts(Pageable pageable);
    
    @Query(value = "{'category': ?0, 'isActive': true}", sort = "{'createdAt': -1}")
    List<Product> findRecentProductsByCategory(String category, Pageable pageable);
    
    // Related products
    @Query("{'$and': [" +
           "{'category': ?0}, " +
           "{'_id': {$ne: ?1}}, " +
           "{'isActive': true}" +
           "]}")
    List<Product> findRelatedProductsByCategory(String category, String excludeId, Pageable pageable);
    
    @Query("{'$and': [" +
           "{'brand': ?0}, " +
           "{'_id': {$ne: ?1}}, " +
           "{'isActive': true}" +
           "]}")
    List<Product> findRelatedProductsByBrand(String brand, String excludeId, Pageable pageable);
    
    @Query("{'$and': [" +
           "{'tags': {$in: ?0}}, " +
           "{'_id': {$ne: ?1}}, " +
           "{'isActive': true}" +
           "]}")
    List<Product> findRelatedProductsByTags(List<String> tags, String excludeId, Pageable pageable);
    
    // Discount queries
    @Query("{'originalPrice': {$exists: true, $ne: null}}")
    List<Product> findProductsWithDiscount();
    
    @Query("{'$and': [" +
           "{'originalPrice': {$exists: true, $ne: null}}, " +
           "{'isActive': true}" +
           "]}")
    Page<Product> findActiveProductsWithDiscount(Pageable pageable);
    
    // Weight and dimension queries
    List<Product> findByWeightBetween(BigDecimal minWeight, BigDecimal maxWeight);
    
    @Query("{'weight': {$exists: true, $ne: null}}")
    List<Product> findProductsWithWeight();
    
    // Aggregation queries for categories
    @Query(value = "{}", fields = "{'category': 1}")
    List<String> findDistinctCategories();
    
    @Query(value = "{}", fields = "{'brand': 1}")
    List<String> findDistinctBrands();
    
    @Query(value = "{'category': ?0}", fields = "{'subcategory': 1}")
    List<String> findDistinctSubcategoriesByCategory(String category);
}
