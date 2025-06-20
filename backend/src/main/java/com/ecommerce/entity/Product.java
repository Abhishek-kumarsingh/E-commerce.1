package com.ecommerce.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "products")
public class Product {
    
    @Id
    private String id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;
    
    @NotBlank(message = "Product description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Original price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Original price format is invalid")
    @Field("original_price")
    private BigDecimal originalPrice;
    
    @NotBlank(message = "Main image is required")
    @Field("main_image")
    private String mainImage;
    
    private List<String> images = new ArrayList<>();
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private String subcategory;
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotBlank(message = "SKU is required")
    @Field("sku")
    private String sku;
    
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Field("stock_quantity")
    private Integer stockQuantity = 0;
    
    @Field("low_stock_threshold")
    private Integer lowStockThreshold = 10;
    
    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    @DecimalMax(value = "1000.0", message = "Weight cannot exceed 1000kg")
    private BigDecimal weight;
    
    private Map<String, String> dimensions = new HashMap<>();
    
    private List<String> tags = new ArrayList<>();
    
    private Map<String, Object> specifications = new HashMap<>();
    
    private List<ProductVariant> variants = new ArrayList<>();
    
    @Field("is_featured")
    private Boolean isFeatured = false;
    
    @Field("is_active")
    private Boolean isActive = true;
    
    @Field("is_digital")
    private Boolean isDigital = false;
    
    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    @Field("average_rating")
    private BigDecimal averageRating = BigDecimal.ZERO;
    
    @Min(value = 0, message = "Review count cannot be negative")
    @Field("review_count")
    private Integer reviewCount = 0;
    
    @Min(value = 0, message = "View count cannot be negative")
    @Field("view_count")
    private Long viewCount = 0L;
    
    @Min(value = 0, message = "Sales count cannot be negative")
    @Field("sales_count")
    private Long salesCount = 0L;
    
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
    public Product() {}
    
    public Product(String name, String description, BigDecimal price, String category, String brand, String sku) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.sku = sku;
    }
    
    // Helper methods
    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }
    
    public boolean isLowStock() {
        return stockQuantity != null && stockQuantity <= lowStockThreshold;
    }
    
    public boolean hasDiscount() {
        return originalPrice != null && originalPrice.compareTo(price) > 0;
    }
    
    public BigDecimal getDiscountPercentage() {
        if (!hasDiscount()) return BigDecimal.ZERO;
        
        BigDecimal discount = originalPrice.subtract(price);
        return discount.divide(originalPrice, 2, RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100));
    }
    
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0L : this.viewCount) + 1;
    }
    
    public void incrementSalesCount(int quantity) {
        this.salesCount = (this.salesCount == null ? 0L : this.salesCount) + quantity;
    }
    
    public void updateRating(BigDecimal newRating, int newReviewCount) {
        this.averageRating = newRating;
        this.reviewCount = newReviewCount;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    
    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }
    
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
    
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    
    public Map<String, String> getDimensions() { return dimensions; }
    public void setDimensions(Map<String, String> dimensions) { this.dimensions = dimensions; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public Map<String, Object> getSpecifications() { return specifications; }
    public void setSpecifications(Map<String, Object> specifications) { this.specifications = specifications; }
    
    public List<ProductVariant> getVariants() { return variants; }
    public void setVariants(List<ProductVariant> variants) { this.variants = variants; }
    
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getIsDigital() { return isDigital; }
    public void setIsDigital(Boolean isDigital) { this.isDigital = isDigital; }
    
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    
    public Long getSalesCount() { return salesCount; }
    public void setSalesCount(Long salesCount) { this.salesCount = salesCount; }
    
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
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(sku, product.sku);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, sku);
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", sku='" + sku + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", isActive=" + isActive +
                '}';
    }
}

class ProductVariant {
    private String name;
    private String value;
    private BigDecimal priceModifier = BigDecimal.ZERO;
    private Integer stockQuantity = 0;
    private String image;
    
    // Constructors
    public ProductVariant() {}
    
    public ProductVariant(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public BigDecimal getPriceModifier() { return priceModifier; }
    public void setPriceModifier(BigDecimal priceModifier) { this.priceModifier = priceModifier; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
