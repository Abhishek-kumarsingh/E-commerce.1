package com.ecommerce.repository;

import com.ecommerce.entity.User;
import com.ecommerce.entity.WishlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    
    // User-based queries
    List<WishlistItem> findByUser(User user);
    
    List<WishlistItem> findByUserId(Long userId);
    
    Page<WishlistItem> findByUserId(Long userId, Pageable pageable);
    
    List<WishlistItem> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Product-based queries
    List<WishlistItem> findByProductId(String productId);
    
    Optional<WishlistItem> findByUserIdAndProductId(Long userId, String productId);
    
    List<WishlistItem> findByUserIdAndProductIdIn(Long userId, List<String> productIds);
    
    // Existence checks
    boolean existsByUserIdAndProductId(Long userId, String productId);
    
    boolean existsByUserId(Long userId);
    
    // Count queries
    long countByUserId(Long userId);
    
    @Query("SELECT COUNT(wi) FROM WishlistItem wi WHERE wi.productId = :productId")
    long countByProductId(@Param("productId") String productId);
    
    // Date-based queries
    List<WishlistItem> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT wi FROM WishlistItem wi WHERE wi.createdAt >= :date")
    List<WishlistItem> findWishlistItemsCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT wi FROM WishlistItem wi WHERE wi.createdAt < :date")
    List<WishlistItem> findOldWishlistItems(@Param("date") LocalDateTime date);
    
    @Query("SELECT wi FROM WishlistItem wi WHERE wi.userId = :userId AND wi.createdAt < :date")
    List<WishlistItem> findOldWishlistItemsByUserId(@Param("userId") Long userId, @Param("date") LocalDateTime date);
    
    // Complex queries with joins
    @Query("SELECT wi FROM WishlistItem wi JOIN FETCH wi.user WHERE wi.id = :id")
    Optional<WishlistItem> findByIdWithUser(@Param("id") Long id);
    
    @Query("SELECT wi FROM WishlistItem wi JOIN FETCH wi.user WHERE wi.userId = :userId")
    List<WishlistItem> findByUserIdWithUser(@Param("userId") Long userId);
    
    // Popular products in wishlists
    @Query("SELECT wi.productId, COUNT(wi) as wishlistCount FROM WishlistItem wi " +
           "GROUP BY wi.productId ORDER BY wishlistCount DESC")
    List<Object[]> findMostWishedProducts(Pageable pageable);
    
    @Query("SELECT wi.productId, COUNT(wi) as wishlistCount FROM WishlistItem wi " +
           "WHERE wi.createdAt >= :date GROUP BY wi.productId ORDER BY wishlistCount DESC")
    List<Object[]> findMostWishedProductsSince(@Param("date") LocalDateTime date, Pageable pageable);
    
    // User wishlist statistics
    @Query("SELECT COUNT(wi) FROM WishlistItem wi WHERE wi.userId = :userId")
    long getWishlistSizeByUserId(@Param("userId") Long userId);
    
    @Query("SELECT wi.productId FROM WishlistItem wi WHERE wi.userId = :userId")
    List<String> getWishlistProductIdsByUserId(@Param("userId") Long userId);
    
    // Recent wishlist additions
    @Query("SELECT wi FROM WishlistItem wi WHERE wi.userId = :userId ORDER BY wi.createdAt DESC")
    List<WishlistItem> findRecentWishlistItemsByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT wi FROM WishlistItem wi ORDER BY wi.createdAt DESC")
    List<WishlistItem> findRecentWishlistItems(Pageable pageable);
    
    // Bulk operations
    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.userId = :userId")
    int deleteByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.userId = :userId AND wi.productId = :productId")
    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") String productId);
    
    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.userId = :userId AND wi.productId IN :productIds")
    int deleteByUserIdAndProductIdIn(@Param("userId") Long userId, @Param("productIds") List<String> productIds);
    
    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.createdAt < :date")
    int deleteOldWishlistItems(@Param("date") LocalDateTime date);
    
    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.productId = :productId")
    int deleteByProductId(@Param("productId") String productId);
    
    // Wishlist comparison queries
    @Query("SELECT wi.productId FROM WishlistItem wi WHERE wi.userId = :userId1 " +
           "AND wi.productId IN (SELECT wi2.productId FROM WishlistItem wi2 WHERE wi2.userId = :userId2)")
    List<String> findCommonWishlistItems(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    @Query("SELECT COUNT(wi) FROM WishlistItem wi WHERE wi.userId = :userId1 " +
           "AND wi.productId IN (SELECT wi2.productId FROM WishlistItem wi2 WHERE wi2.userId = :userId2)")
    long countCommonWishlistItems(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
