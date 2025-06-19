package com.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
@EntityListeners(AuditingEntityListener.class)
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Product ID is required")
    @Column(name = "product_id", nullable = false)
    private String productId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;
    
    @NotNull(message = "Price is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @ElementCollection
    @CollectionTable(name = "cart_item_variants", joinColumns = @JoinColumn(name = "cart_item_id"))
    @MapKeyColumn(name = "variant_name")
    @Column(name = "variant_value")
    private Map<String, String> selectedVariants = new HashMap<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public CartItem() {}
    
    public CartItem(String productId, Integer quantity, BigDecimal price, User user) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.user = user;
    }
    
    // Helper methods
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
    
    public void incrementQuantity() {
        this.quantity++;
    }
    
    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }
    
    public void updateQuantity(Integer newQuantity) {
        if (newQuantity > 0) {
            this.quantity = newQuantity;
        }
    }
    
    public boolean hasVariants() {
        return selectedVariants != null && !selectedVariants.isEmpty();
    }
    
    public void addVariant(String name, String value) {
        if (selectedVariants == null) {
            selectedVariants = new HashMap<>();
        }
        selectedVariants.put(name, value);
    }
    
    public void removeVariant(String name) {
        if (selectedVariants != null) {
            selectedVariants.remove(name);
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Map<String, String> getSelectedVariants() { return selectedVariants; }
    public void setSelectedVariants(Map<String, String> selectedVariants) { this.selectedVariants = selectedVariants; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id) && 
               Objects.equals(productId, cartItem.productId) && 
               Objects.equals(user, cartItem.user);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, productId, user);
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
}
