package com.ecommerce.repository;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    // User-based queries
    List<CartItem> findByUser(User user);
    
    List<CartItem> findByUserId(Long userId);
    
    Page<CartItem> findByUserId(Long userId, Pageable pageable);
    
    List<CartItem> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Product-based queries
    List<CartItem> findByProductId(String productId);
    
    Optional<CartItem> findByUserIdAndProductId(Long userId, String productId);
    
    List<CartItem> findByUserIdAndProductIdIn(Long userId, List<String> productIds);
    
    // Existence checks
    boolean existsByUserIdAndProductId(Long userId, String productId);
    
    boolean existsByUserId(Long userId);
    
    // Count queries
    long countByUserId(Long userId);
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.productId = :productId")
    long countByProductId(@Param("productId") String productId);
    
    // Sum queries
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.userId = :userId")
    Long sumQuantityByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(ci.price * ci.quantity) FROM CartItem ci WHERE ci.userId = :userId")
    BigDecimal sumTotalByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.productId = :productId")
    Long sumQuantityByProductId(@Param("productId") String productId);
    
    // Date-based queries
    List<CartItem> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.createdAt >= :date")
    List<CartItem> findCartItemsCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.updatedAt >= :date")
    List<CartItem> findCartItemsUpdatedAfter(@Param("date") LocalDateTime date);
    
    // Old cart items (for cleanup)
    @Query("SELECT ci FROM CartItem ci WHERE ci.updatedAt < :date")
    List<CartItem> findOldCartItems(@Param("date") LocalDateTime date);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.userId = :userId AND ci.updatedAt < :date")
    List<CartItem> findOldCartItemsByUserId(@Param("userId") Long userId, @Param("date") LocalDateTime date);
    
    // Price-based queries
    @Query("SELECT ci FROM CartItem ci WHERE ci.price >= :minPrice")
    List<CartItem> findByMinPrice(@Param("minPrice") BigDecimal minPrice);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.price BETWEEN :minPrice AND :maxPrice")
    List<CartItem> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice);
    
    // Quantity-based queries
    @Query("SELECT ci FROM CartItem ci WHERE ci.quantity >= :minQuantity")
    List<CartItem> findByMinQuantity(@Param("minQuantity") Integer minQuantity);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.quantity BETWEEN :minQuantity AND :maxQuantity")
    List<CartItem> findByQuantityRange(@Param("minQuantity") Integer minQuantity, 
                                      @Param("maxQuantity") Integer maxQuantity);
    
    // Complex queries with joins
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.user WHERE ci.id = :id")
    Optional<CartItem> findByIdWithUser(@Param("id") Long id);
    
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.user WHERE ci.userId = :userId")
    List<CartItem> findByUserIdWithUser(@Param("userId") Long userId);
    
    // Cart summary queries
    @Query("SELECT new map(COUNT(ci) as itemCount, SUM(ci.quantity) as totalQuantity, " +
           "SUM(ci.price * ci.quantity) as totalAmount) FROM CartItem ci WHERE ci.userId = :userId")
    List<Object> getCartSummaryByUserId(@Param("userId") Long userId);
    
    // Variant queries
    @Query("SELECT ci FROM CartItem ci WHERE SIZE(ci.selectedVariants) > 0")
    List<CartItem> findItemsWithVariants();
    
    @Query("SELECT ci FROM CartItem ci WHERE SIZE(ci.selectedVariants) = 0")
    List<CartItem> findItemsWithoutVariants();
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.userId = :userId AND SIZE(ci.selectedVariants) > 0")
    List<CartItem> findUserItemsWithVariants(@Param("userId") Long userId);
    
    // Bulk operations
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.userId = :userId")
    int deleteByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.userId = :userId AND ci.productId = :productId")
    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") String productId);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.userId = :userId AND ci.id IN :itemIds")
    int deleteByUserIdAndIdIn(@Param("userId") Long userId, @Param("itemIds") List<Long> itemIds);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.updatedAt < :date")
    int deleteOldCartItems(@Param("date") LocalDateTime date);
    
    @Modifying
    @Query("UPDATE CartItem ci SET ci.quantity = :quantity, ci.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE ci.userId = :userId AND ci.productId = :productId")
    int updateQuantityByUserIdAndProductId(@Param("userId") Long userId, 
                                          @Param("productId") String productId, 
                                          @Param("quantity") Integer quantity);
    
    @Modifying
    @Query("UPDATE CartItem ci SET ci.price = :price, ci.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE ci.userId = :userId AND ci.productId = :productId")
    int updatePriceByUserIdAndProductId(@Param("userId") Long userId, 
                                       @Param("productId") String productId, 
                                       @Param("price") BigDecimal price);
}
