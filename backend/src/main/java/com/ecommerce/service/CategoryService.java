package com.ecommerce.service;

import com.ecommerce.entity.Category;
import com.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    // Create operations
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Category createCategory(Category category) {
        log.info("Creating new category with slug: {}", category.getSlug());
        
        if (categoryRepository.existsBySlug(category.getSlug())) {
            throw new IllegalArgumentException("Category already exists with slug: " + category.getSlug());
        }
        
        // Validate parent category if specified
        if (category.getParentId() != null) {
            Category parent = getCategoryById(category.getParentId());
            if (parent.getParentId() != null) {
                throw new IllegalArgumentException("Cannot create subcategory under another subcategory");
            }
        }
        
        category.setIsActive(true);
        category.setProductCount(0L);
        
        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());
        return savedCategory;
    }
    
    // Read operations
    @Cacheable(value = "categories", key = "#id")
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }
    
    public Optional<Category> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }
    
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    }
    
    public Category getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with slug: " + slug));
    }
    
    @Cacheable(value = "categories", key = "'all-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    @Cacheable(value = "categories", key = "'active-' + #pageable.pageNumber")
    public Page<Category> getActiveCategories(Pageable pageable) {
        return categoryRepository.findByIsActive(true, pageable);
    }
    
    @Cacheable(value = "categories", key = "'parent-categories'")
    public List<Category> getParentCategories() {
        return categoryRepository.findActiveParentCategories(Sort.by("sortOrder").ascending());
    }
    
    @Cacheable(value = "categories", key = "'subcategories-' + #parentId")
    public List<Category> getSubCategories(String parentId) {
        return categoryRepository.findActiveSubCategories(parentId, Sort.by("sortOrder").ascending());
    }
    
    @Cacheable(value = "categories", key = "'featured'")
    public List<Category> getFeaturedCategories() {
        return categoryRepository.findFeaturedCategories();
    }
    
    public Page<Category> searchCategories(String keyword, Pageable pageable) {
        return categoryRepository.searchActiveCategories(keyword, pageable);
    }
    
    // Update operations
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Category updateCategory(String id, Category categoryDetails) {
        log.info("Updating category with ID: {}", id);
        
        Category category = getCategoryById(id);
        
        // Check if slug is being changed and if it's unique
        if (!category.getSlug().equals(categoryDetails.getSlug())) {
            if (categoryRepository.existsBySlugAndIdNot(categoryDetails.getSlug(), id)) {
                throw new IllegalArgumentException("Category already exists with slug: " + categoryDetails.getSlug());
            }
        }
        
        category.setName(categoryDetails.getName());
        category.setSlug(categoryDetails.getSlug());
        category.setDescription(categoryDetails.getDescription());
        category.setIcon(categoryDetails.getIcon());
        category.setImage(categoryDetails.getImage());
        category.setSortOrder(categoryDetails.getSortOrder());
        category.setTags(categoryDetails.getTags());
        category.setMetaTitle(categoryDetails.getMetaTitle());
        category.setMetaDescription(categoryDetails.getMetaDescription());
        category.setMetaKeywords(categoryDetails.getMetaKeywords());
        
        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with ID: {}", updatedCategory.getId());
        return updatedCategory;
    }
    
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public Category activateCategory(String id) {
        log.info("Activating category with ID: {}", id);
        
        Category category = getCategoryById(id);
        category.activate();
        
        Category activatedCategory = categoryRepository.save(category);
        log.info("Category activated successfully with ID: {}", id);
        return activatedCategory;
    }
    
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public Category deactivateCategory(String id) {
        log.info("Deactivating category with ID: {}", id);
        
        Category category = getCategoryById(id);
        category.deactivate();
        
        Category deactivatedCategory = categoryRepository.save(category);
        log.info("Category deactivated successfully with ID: {}", id);
        return deactivatedCategory;
    }
    
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public Category featureCategory(String id) {
        log.info("Featuring category with ID: {}", id);
        
        Category category = getCategoryById(id);
        category.feature();
        
        Category featuredCategory = categoryRepository.save(category);
        log.info("Category featured successfully with ID: {}", id);
        return featuredCategory;
    }
    
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public Category unfeatureCategory(String id) {
        log.info("Unfeaturing category with ID: {}", id);
        
        Category category = getCategoryById(id);
        category.unfeature();
        
        Category unfeaturedCategory = categoryRepository.save(category);
        log.info("Category unfeatured successfully with ID: {}", id);
        return unfeaturedCategory;
    }
    
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public Category updateProductCount(String id, Long productCount) {
        log.info("Updating product count for category {} to {}", id, productCount);
        
        Category category = getCategoryById(id);
        category.updateProductCount(productCount);
        
        Category updatedCategory = categoryRepository.save(category);
        log.info("Product count updated successfully for category {}", id);
        return updatedCategory;
    }
    
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public Category incrementProductCount(String id) {
        Category category = getCategoryById(id);
        category.incrementProductCount();
        return categoryRepository.save(category);
    }
    
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public Category decrementProductCount(String id) {
        Category category = getCategoryById(id);
        category.decrementProductCount();
        return categoryRepository.save(category);
    }
    
    // Delete operations
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(String id) {
        log.info("Deleting category with ID: {}", id);
        
        Category category = getCategoryById(id);
        
        // Check if category has subcategories
        List<Category> subCategories = categoryRepository.findByParentId(id);
        if (!subCategories.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete category with subcategories. Delete subcategories first.");
        }
        
        // Check if category has products
        if (category.getProductCount() > 0) {
            throw new IllegalArgumentException("Cannot delete category with products. Move or delete products first.");
        }
        
        categoryRepository.delete(category);
        log.info("Category deleted successfully with ID: {}", id);
    }
    
    // Utility methods
    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }
    
    public long getTotalCategoryCount() {
        return categoryRepository.count();
    }
    
    public long getActiveCategoryCount() {
        return categoryRepository.countByIsActive(true);
    }
    
    public long getParentCategoryCount() {
        return categoryRepository.countParentCategories();
    }
    
    public long getSubCategoryCount() {
        return categoryRepository.countSubCategories();
    }
    
    public long getCategoriesWithProductsCount() {
        return categoryRepository.countCategoriesWithProducts();
    }
    
    // Popular and trending categories
    @Cacheable(value = "categories", key = "'popular-' + #pageable.pageNumber")
    public List<Category> getPopularCategories(Pageable pageable) {
        return categoryRepository.findPopularCategories(pageable);
    }
    
    @Cacheable(value = "categories", key = "'popular-parent-' + #pageable.pageNumber")
    public List<Category> getPopularParentCategories(Pageable pageable) {
        return categoryRepository.findPopularParentCategories(pageable);
    }
    
    public List<Category> getRecentCategories(Pageable pageable) {
        return categoryRepository.findRecentCategories(pageable);
    }
    
    // Category hierarchy operations
    public List<Category> getCategoryHierarchy() {
        List<Category> parentCategories = getParentCategories();
        
        for (Category parent : parentCategories) {
            List<Category> children = getSubCategories(parent.getId());
            // Note: In a real implementation, you might want to add a children field to Category
            // or return a different DTO that includes the hierarchy
        }
        
        return parentCategories;
    }
    
    public List<Category> getCategoryPath(String categoryId) {
        Category category = getCategoryById(categoryId);
        List<Category> path = new java.util.ArrayList<>();
        
        path.add(category);
        
        if (category.getParentId() != null) {
            Category parent = getCategoryById(category.getParentId());
            path.add(0, parent);
        }
        
        return path;
    }
    
    // Bulk operations
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void updateCategorySortOrder(List<CategorySortUpdate> updates) {
        log.info("Updating sort order for {} categories", updates.size());
        
        for (CategorySortUpdate update : updates) {
            Category category = getCategoryById(update.getCategoryId());
            category.setSortOrder(update.getSortOrder());
            categoryRepository.save(category);
        }
        
        log.info("Category sort order updated successfully");
    }
    
    // Helper classes
    public static class CategorySortUpdate {
        private String categoryId;
        private Integer sortOrder;
        
        public CategorySortUpdate() {}
        
        public CategorySortUpdate(String categoryId, Integer sortOrder) {
            this.categoryId = categoryId;
            this.sortOrder = sortOrder;
        }
        
        public String getCategoryId() { return categoryId; }
        public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
        
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    }
}
