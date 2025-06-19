package com.ecommerce.repository;

import com.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    
    // Basic queries
    Optional<Category> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    List<Category> findByName(String name);
    
    List<Category> findByNameContainingIgnoreCase(String name);
    
    // Parent-child relationship queries
    List<Category> findByParentId(String parentId);
    
    List<Category> findByParentIdOrderBySortOrder(String parentId);
    
    List<Category> findByParentIdAndIsActive(String parentId, Boolean isActive);
    
    @Query("{'parentId': {$exists: false}}")
    List<Category> findParentCategories();
    
    @Query("{'parentId': {$exists: false}}")
    List<Category> findParentCategories(Sort sort);
    
    @Query("{'parentId': {$exists: true, $ne: null}}")
    List<Category> findSubCategories();
    
    @Query("{'$and': [{'parentId': {$exists: false}}, {'isActive': true}]}")
    List<Category> findActiveParentCategories();
    
    @Query("{'$and': [{'parentId': {$exists: false}}, {'isActive': true}]}")
    List<Category> findActiveParentCategories(Sort sort);
    
    // Status queries
    List<Category> findByIsActive(Boolean isActive);
    
    Page<Category> findByIsActive(Boolean isActive, Pageable pageable);
    
    List<Category> findByIsFeatured(Boolean isFeatured);
    
    Page<Category> findByIsFeatured(Boolean isFeatured, Pageable pageable);
    
    List<Category> findByIsActiveAndIsFeatured(Boolean isActive, Boolean isFeatured);
    
    // Product count queries
    List<Category> findByProductCountGreaterThan(Long productCount);
    
    @Query("{'productCount': {$gt: 0}}")
    List<Category> findCategoriesWithProducts();
    
    @Query("{'productCount': 0}")
    List<Category> findEmptyCategories();
    
    @Query("{'$and': [{'productCount': {$gt: 0}}, {'isActive': true}]}")
    List<Category> findActiveCategoriesWithProducts();
    
    // Search queries
    @Query("{'$or': [" +
           "{'name': {$regex: ?0, $options: 'i'}}, " +
           "{'description': {$regex: ?0, $options: 'i'}}, " +
           "{'tags': {$regex: ?0, $options: 'i'}}" +
           "]}")
    Page<Category> searchCategories(String keyword, Pageable pageable);
    
    @Query("{'$and': [" +
           "{'isActive': true}, " +
           "{'$or': [" +
           "{'name': {$regex: ?0, $options: 'i'}}, " +
           "{'description': {$regex: ?0, $options: 'i'}}, " +
           "{'tags': {$regex: ?0, $options: 'i'}}" +
           "]}" +
           "]}")
    Page<Category> searchActiveCategories(String keyword, Pageable pageable);
    
    // Tag queries
    @Query("{'tags': {$in: ?0}}")
    List<Category> findByTagsIn(List<String> tags);
    
    @Query("{'tags': ?0}")
    List<Category> findByTag(String tag);
    
    // Sort order queries
    List<Category> findByParentIdOrderBySortOrderAsc(String parentId);
    
    List<Category> findByParentIdAndIsActiveOrderBySortOrderAsc(String parentId, Boolean isActive);
    
    @Query("{'parentId': {$exists: false}}")
    List<Category> findParentCategoriesOrderBySortOrder(Sort sort);
    
    // Date queries
    List<Category> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'createdAt': {$gte: ?0}}")
    List<Category> findCategoriesCreatedAfter(LocalDateTime date);
    
    @Query("{'updatedAt': {$gte: ?0}}")
    List<Category> findCategoriesUpdatedAfter(LocalDateTime date);
    
    // Statistics queries
    long countByIsActive(Boolean isActive);
    
    long countByIsFeatured(Boolean isFeatured);
    
    long countByParentId(String parentId);
    
    @Query(value = "{'parentId': {$exists: false}}", count = true)
    long countParentCategories();
    
    @Query(value = "{'parentId': {$exists: true, $ne: null}}", count = true)
    long countSubCategories();
    
    @Query(value = "{'productCount': {$gt: 0}}", count = true)
    long countCategoriesWithProducts();
    
    // Featured categories
    @Query(value = "{'isFeatured': true, 'isActive': true}", sort = "{'sortOrder': 1}")
    List<Category> findFeaturedCategories();
    
    @Query(value = "{'isFeatured': true, 'isActive': true}", sort = "{'sortOrder': 1}")
    List<Category> findFeaturedCategories(Pageable pageable);
    
    // Popular categories (by product count)
    @Query(value = "{'isActive': true}", sort = "{'productCount': -1}")
    List<Category> findPopularCategories(Pageable pageable);
    
    @Query(value = "{'$and': [{'parentId': {$exists: false}}, {'isActive': true}]}", sort = "{'productCount': -1}")
    List<Category> findPopularParentCategories(Pageable pageable);
    
    // Recent categories
    @Query(value = "{'isActive': true}", sort = "{'createdAt': -1}")
    List<Category> findRecentCategories(Pageable pageable);
    
    // Category hierarchy queries
    @Query("{'$and': [{'parentId': ?0}, {'isActive': true}]}")
    List<Category> findActiveSubCategories(String parentId);
    
    @Query("{'$and': [{'parentId': ?0}, {'isActive': true}]}")
    List<Category> findActiveSubCategories(String parentId, Sort sort);
    
    // Meta information queries
    @Query("{'metaTitle': {$exists: true, $ne: null}}")
    List<Category> findCategoriesWithMetaTitle();
    
    @Query("{'metaDescription': {$exists: true, $ne: null}}")
    List<Category> findCategoriesWithMetaDescription();
    
    // Image queries
    @Query("{'image': {$exists: true, $ne: null}}")
    List<Category> findCategoriesWithImage();
    
    @Query("{'icon': {$exists: true, $ne: null}}")
    List<Category> findCategoriesWithIcon();
    
    // Bulk operations support queries
    List<Category> findByIdIn(List<String> ids);
    
    List<Category> findBySlugIn(List<String> slugs);
    
    // Category tree structure queries
    @Query("{'$and': [{'parentId': {$exists: false}}, {'isActive': true}]}")
    List<Category> findActiveCategoryTree(Sort sort);
    
    // Validation queries
    boolean existsByNameAndParentId(String name, String parentId);
    
    boolean existsBySlugAndIdNot(String slug, String id);
    
    // Custom aggregation queries for category statistics
    @Query(value = "{}", fields = "{'name': 1, 'productCount': 1}")
    List<Category> findCategoryProductCounts();
    
    // Find categories that need product count update
    @Query("{'updatedAt': {$lt: ?0}}")
    List<Category> findCategoriesNeedingUpdate(LocalDateTime date);
}
