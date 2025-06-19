package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    // Order-based queries
    List<OrderItem> findByOrder(Order order);
    
    List<OrderItem> findByOrderId(Long orderId);
    
    Page<OrderItem> findByOrderId(Long orderId, Pageable pageable);
    
    // Product-based queries
    List<OrderItem> findByProductId(String productId);
    
    Page<OrderItem> findByProductId(String productId, Pageable pageable);
    
    List<OrderItem> findByProductSku(String productSku);
    
    // Product and order queries
    Optional<OrderItem> findByOrderIdAndProductId(Long orderId, String productId);
    
    List<OrderItem> findByOrderIdAndProductIdIn(Long orderId, List<String> productIds);
    
    // Search queries
    @Query("SELECT oi FROM OrderItem oi WHERE " +
           "LOWER(oi.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(oi.productSku) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<OrderItem> searchOrderItems(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderId = :orderId AND " +
           "(LOWER(oi.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(oi.productSku) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<OrderItem> searchOrderItemsByOrder(@Param("orderId") Long orderId, @Param("keyword") String keyword);
    
    // Statistics queries
    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.productId = :productId")
    long countByProductId(@Param("productId") String productId);
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.productId = :productId")
    Long sumQuantityByProductId(@Param("productId") String productId);
    
    @Query("SELECT SUM(oi.totalPrice) FROM OrderItem oi WHERE oi.productId = :productId")
    BigDecimal sumTotalPriceByProductId(@Param("productId") String productId);
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.orderId = :orderId")
    Long sumQuantityByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT SUM(oi.totalPrice) FROM OrderItem oi WHERE oi.orderId = :orderId")
    BigDecimal sumTotalPriceByOrderId(@Param("orderId") Long orderId);
    
    // Date-based queries
    @Query("SELECT oi FROM OrderItem oi WHERE oi.createdAt BETWEEN :startDate AND :endDate")
    List<OrderItem> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.createdAt >= :date")
    List<OrderItem> findOrderItemsCreatedAfter(@Param("date") LocalDateTime date);
    
    // Price-based queries
    @Query("SELECT oi FROM OrderItem oi WHERE oi.unitPrice >= :minPrice")
    List<OrderItem> findByMinUnitPrice(@Param("minPrice") BigDecimal minPrice);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<OrderItem> findByUnitPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                        @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.totalPrice >= :minTotal")
    List<OrderItem> findByMinTotalPrice(@Param("minTotal") BigDecimal minTotal);
    
    // Quantity-based queries
    @Query("SELECT oi FROM OrderItem oi WHERE oi.quantity >= :minQuantity")
    List<OrderItem> findByMinQuantity(@Param("minQuantity") Integer minQuantity);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.quantity BETWEEN :minQuantity AND :maxQuantity")
    List<OrderItem> findByQuantityRange(@Param("minQuantity") Integer minQuantity, 
                                       @Param("maxQuantity") Integer maxQuantity);
    
    // Complex queries with joins
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.order WHERE oi.id = :id")
    Optional<OrderItem> findByIdWithOrder(@Param("id") Long id);
    
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.order o WHERE o.userId = :userId")
    List<OrderItem> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.order o WHERE o.userId = :userId AND oi.productId = :productId")
    List<OrderItem> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") String productId);
    
    // Top selling products
    @Query("SELECT oi.productId, oi.productName, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi GROUP BY oi.productId, oi.productName " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);
    
    @Query("SELECT oi.productId, oi.productName, SUM(oi.totalPrice) as totalRevenue " +
           "FROM OrderItem oi GROUP BY oi.productId, oi.productName " +
           "ORDER BY totalRevenue DESC")
    List<Object[]> findTopRevenueProducts(Pageable pageable);
    
    // Recent purchases
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.userId = :userId " +
           "ORDER BY oi.createdAt DESC")
    List<OrderItem> findRecentPurchasesByUserId(@Param("userId") Long userId, Pageable pageable);
    
    // Variant queries
    @Query("SELECT oi FROM OrderItem oi WHERE SIZE(oi.selectedVariants) > 0")
    List<OrderItem> findItemsWithVariants();
    
    @Query("SELECT oi FROM OrderItem oi WHERE SIZE(oi.selectedVariants) = 0")
    List<OrderItem> findItemsWithoutVariants();
}
