package com.ecommerce.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "categories")
public class Category {
    
    @Id
    private String id;
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Category slug is required")
    @Size(min = 2, max = 100, message = "Category slug must be between 2 and 100 characters")
    private String slug;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private String icon;
    
    private String image;
    
    @Field("parent_id")
    private String parentId;
    
    @Field("product_count")
    @Min(value = 0, message = "Product count cannot be negative")
    private Long productCount = 0L;
    
    @Field("sort_order")
    @Min(value = 0, message = "Sort order cannot be negative")
    private Integer sortOrder = 0;
    
    @Field("is_active")
    private Boolean isActive = true;
    
    @Field("is_featured")
    private Boolean isFeatured = false;
    
    private List<String> tags = new ArrayList<>();
    
    @Field("meta_title")
    private String metaTitle;
    
    @Field("meta_description")
    private String metaDescription;
    
    @Field("meta_keywords")
    private List<String> metaKeywords = new ArrayList<>();
    
    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Category() {}
    
    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
    
    public Category(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
    }
    
    // Helper methods
    public boolean isParentCategory() {
        return parentId == null || parentId.isEmpty();
    }
    
    public boolean isSubCategory() {
        return parentId != null && !parentId.isEmpty();
    }
    
    public void incrementProductCount() {
        this.productCount++;
    }
    
    public void decrementProductCount() {
        if (this.productCount > 0) {
            this.productCount--;
        }
    }
    
    public void updateProductCount(Long count) {
        this.productCount = Math.max(0, count);
    }
    
    public void activate() {
        this.isActive = true;
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void feature() {
        this.isFeatured = true;
    }
    
    public void unfeature() {
        this.isFeatured = false;
    }
    
    public void addTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }
    
    public void removeTag(String tag) {
        if (tags != null) {
            tags.remove(tag);
        }
    }
    
    public void addMetaKeyword(String keyword) {
        if (metaKeywords == null) {
            metaKeywords = new ArrayList<>();
        }
        if (!metaKeywords.contains(keyword)) {
            metaKeywords.add(keyword);
        }
    }
    
    public void removeMetaKeyword(String keyword) {
        if (metaKeywords != null) {
            metaKeywords.remove(keyword);
        }
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    
    public Long getProductCount() { return productCount; }
    public void setProductCount(Long productCount) { this.productCount = productCount; }
    
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public String getMetaTitle() { return metaTitle; }
    public void setMetaTitle(String metaTitle) { this.metaTitle = metaTitle; }
    
    public String getMetaDescription() { return metaDescription; }
    public void setMetaDescription(String metaDescription) { this.metaDescription = metaDescription; }
    
    public List<String> getMetaKeywords() { return metaKeywords; }
    public void setMetaKeywords(List<String> metaKeywords) { this.metaKeywords = metaKeywords; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(slug, category.slug);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, slug);
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", parentId='" + parentId + '\'' +
                ", productCount=" + productCount +
                ", isActive=" + isActive +
                ", isFeatured=" + isFeatured +
                '}';
    }
}
