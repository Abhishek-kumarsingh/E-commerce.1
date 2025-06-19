package com.ecommerce.repository;

import com.ecommerce.entity.ProductReview;
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
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    
    // Product-based queries
    List<ProductReview> findByProductId(String productId);
    
    Page<ProductReview> findByProductId(String productId, Pageable pageable);
    
    List<ProductReview> findByProductIdAndIsApprovedTrue(String productId);
    
    Page<ProductReview> findByProductIdAndIsApprovedTrue(String productId, Pageable pageable);
    
    List<ProductReview> findByProductIdOrderByCreatedAtDesc(String productId);
    
    Page<ProductReview> findByProductIdAndIsApprovedTrueOrderByCreatedAtDesc(String productId, Pageable pageable);
    
    // User-based queries
    List<ProductReview> findByUser(User user);
    
    List<ProductReview> findByUserId(Long userId);
    
    Page<ProductReview> findByUserId(Long userId, Pageable pageable);
    
    Optional<ProductReview> findByUserIdAndProductId(Long userId, String productId);
    
    List<ProductReview> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Rating-based queries
    List<ProductReview> findByRating(Integer rating);
    
    List<ProductReview> findByProductIdAndRating(String productId, Integer rating);
    
    List<ProductReview> findByRatingGreaterThanEqual(Integer rating);
    
    List<ProductReview> findByRatingLessThanEqual(Integer rating);
    
    List<ProductReview> findByProductIdAndRatingGreaterThanEqual(String productId, Integer rating);
    
    List<ProductReview> findByProductIdAndRatingLessThanEqual(String productId, Integer rating);
    
    // Approval status queries
    List<ProductReview> findByIsApproved(Boolean isApproved);
    
    Page<ProductReview> findByIsApproved(Boolean isApproved, Pageable pageable);
    
    List<ProductReview> findByIsApprovedFalse();
    
    Page<ProductReview> findByIsApprovedFalse(Pageable pageable);
    
    // Verification status queries
    List<ProductReview> findByIsVerified(Boolean isVerified);
    
    List<ProductReview> findByProductIdAndIsVerified(String productId, Boolean isVerified);
    
    List<ProductReview> findByIsVerifiedTrue();
    
    Page<ProductReview> findByProductIdAndIsVerifiedTrue(String productId, Pageable pageable);
    
    // Existence checks
    boolean existsByUserIdAndProductId(Long userId, String productId);
    
    boolean existsByProductId(String productId);
    
    // Count queries
    long countByProductId(String productId);
    
    long countByProductIdAndIsApprovedTrue(String productId);
    
    long countByUserId(Long userId);
    
    long countByRating(Integer rating);
    
    long countByProductIdAndRating(String productId, Integer rating);
    
    long countByIsApprovedFalse();
    
    long countByIsVerifiedTrue();
    
    // Average rating queries
    @Query("SELECT AVG(pr.rating) FROM ProductReview pr WHERE pr.productId = :productId AND pr.isApproved = true")
    BigDecimal getAverageRatingByProductId(@Param("productId") String productId);
    
    @Query("SELECT AVG(pr.rating) FROM ProductReview pr WHERE pr.productId = :productId AND pr.isApproved = true AND pr.isVerified = true")
    BigDecimal getAverageVerifiedRatingByProductId(@Param("productId") String productId);
    
    // Rating distribution queries
    @Query("SELECT pr.rating, COUNT(pr) FROM ProductReview pr WHERE pr.productId = :productId AND pr.isApproved = true GROUP BY pr.rating ORDER BY pr.rating DESC")
    List<Object[]> getRatingDistributionByProductId(@Param("productId") String productId);
    
    // Helpful count queries
    List<ProductReview> findByHelpfulCountGreaterThan(Integer helpfulCount);
    
    List<ProductReview> findByProductIdAndHelpfulCountGreaterThan(String productId, Integer helpfulCount);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.productId = :productId AND pr.isApproved = true ORDER BY pr.helpfulCount DESC")
    List<ProductReview> findMostHelpfulByProductId(@Param("productId") String productId, Pageable pageable);
    
    // Date-based queries
    List<ProductReview> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<ProductReview> findByProductIdAndCreatedAtBetween(String productId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.createdAt >= :date")
    List<ProductReview> findReviewsCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.updatedAt >= :date")
    List<ProductReview> findReviewsUpdatedAfter(@Param("date") LocalDateTime date);
    
    // Search queries
    @Query("SELECT pr FROM ProductReview pr WHERE pr.productId = :productId AND " +
           "(LOWER(pr.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(pr.comment) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ProductReview> searchProductReviews(@Param("productId") String productId, 
                                           @Param("keyword") String keyword, 
                                           Pageable pageable);
    
    @Query("SELECT pr FROM ProductReview pr WHERE " +
           "LOWER(pr.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(pr.comment) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductReview> searchAllReviews(@Param("keyword") String keyword, Pageable pageable);
    
    // Complex queries with joins
    @Query("SELECT pr FROM ProductReview pr JOIN FETCH pr.user WHERE pr.id = :id")
    Optional<ProductReview> findByIdWithUser(@Param("id") Long id);
    
    @Query("SELECT pr FROM ProductReview pr JOIN FETCH pr.user WHERE pr.productId = :productId AND pr.isApproved = true")
    List<ProductReview> findByProductIdWithUser(@Param("productId") String productId);
    
    // Recent reviews
    @Query("SELECT pr FROM ProductReview pr WHERE pr.isApproved = true ORDER BY pr.createdAt DESC")
    List<ProductReview> findRecentApprovedReviews(Pageable pageable);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.productId = :productId AND pr.isApproved = true ORDER BY pr.createdAt DESC")
    List<ProductReview> findRecentReviewsByProductId(@Param("productId") String productId, Pageable pageable);
    
    // Top reviews
    @Query("SELECT pr FROM ProductReview pr WHERE pr.isApproved = true ORDER BY pr.helpfulCount DESC, pr.rating DESC")
    List<ProductReview> findTopReviews(Pageable pageable);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.productId = :productId AND pr.isApproved = true ORDER BY pr.helpfulCount DESC, pr.rating DESC")
    List<ProductReview> findTopReviewsByProductId(@Param("productId") String productId, Pageable pageable);
    
    // Bulk operations
    @Modifying
    @Query("UPDATE ProductReview pr SET pr.isApproved = :isApproved WHERE pr.id IN :ids")
    int updateApprovalStatusByIds(@Param("ids") List<Long> ids, @Param("isApproved") Boolean isApproved);
    
    @Modifying
    @Query("UPDATE ProductReview pr SET pr.isVerified = :isVerified WHERE pr.id IN :ids")
    int updateVerificationStatusByIds(@Param("ids") List<Long> ids, @Param("isVerified") Boolean isVerified);
    
    @Modifying
    @Query("UPDATE ProductReview pr SET pr.helpfulCount = pr.helpfulCount + 1 WHERE pr.id = :id")
    int incrementHelpfulCount(@Param("id") Long id);
    
    @Modifying
    @Query("UPDATE ProductReview pr SET pr.helpfulCount = pr.helpfulCount - 1 WHERE pr.id = :id AND pr.helpfulCount > 0")
    int decrementHelpfulCount(@Param("id") Long id);
    
    @Modifying
    @Query("DELETE FROM ProductReview pr WHERE pr.productId = :productId")
    int deleteByProductId(@Param("productId") String productId);
}
