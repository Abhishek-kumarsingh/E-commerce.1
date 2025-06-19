package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.entity.enums.OrderStatus;
import com.ecommerce.entity.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    
    // Basic queries
    Optional<Order> findByOrderNumber(String orderNumber);
    
    boolean existsByOrderNumber(String orderNumber);
    
    // User-based queries
    List<Order> findByUser(User user);
    
    List<Order> findByUserId(Long userId);
    
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // Status-based queries
    List<Order> findByStatus(OrderStatus status);
    
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
    
    Page<Order> findByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);
    
    List<Order> findByStatusAndPaymentStatus(OrderStatus status, PaymentStatus paymentStatus);
    
    // User and status queries
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
    
    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);
    
    List<Order> findByUserIdAndPaymentStatus(Long userId, PaymentStatus paymentStatus);
    
    // Date-based queries
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :date")
    List<Order> findOrdersCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT o FROM Order o WHERE o.updatedAt >= :date")
    List<Order> findOrdersUpdatedAfter(@Param("date") LocalDateTime date);
    
    // Amount-based queries
    @Query("SELECT o FROM Order o WHERE o.total >= :minAmount")
    List<Order> findOrdersWithMinAmount(@Param("minAmount") BigDecimal minAmount);
    
    @Query("SELECT o FROM Order o WHERE o.total BETWEEN :minAmount AND :maxAmount")
    List<Order> findOrdersWithAmountRange(@Param("minAmount") BigDecimal minAmount, 
                                         @Param("maxAmount") BigDecimal maxAmount);
    
    // Search queries
    @Query("SELECT o FROM Order o WHERE " +
           "o.orderNumber LIKE CONCAT('%', :keyword, '%') OR " +
           "o.trackingNumber LIKE CONCAT('%', :keyword, '%')")
    Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND " +
           "(o.orderNumber LIKE CONCAT('%', :keyword, '%') OR " +
           "o.trackingNumber LIKE CONCAT('%', :keyword, '%'))")
    Page<Order> searchUserOrders(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
    
    // Statistics queries
    @Query("SELECT COUNT(o) FROM Order o WHERE o.userId = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@Param("status") OrderStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :paymentStatus")
    long countByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :date")
    long countOrdersCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = :status")
    BigDecimal sumTotalByStatus(@Param("status") OrderStatus status);
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.userId = :userId AND o.status = :status")
    BigDecimal sumTotalByUserIdAndStatus(@Param("userId") Long userId, @Param("status") OrderStatus status);
    
    // Complex queries with joins
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.id = :id")
    Optional<Order> findByIdWithUser(@Param("id") Long id);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.user WHERE o.id = :id")
    Optional<Order> findByIdWithItemsAndUser(@Param("id") Long id);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.orderNumber = :orderNumber")
    Optional<Order> findByOrderNumberWithItems(@Param("orderNumber") String orderNumber);
    
    // Recent orders
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.status IN :statuses ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByStatuses(@Param("statuses") List<OrderStatus> statuses, Pageable pageable);
    
    // Tracking queries
    Optional<Order> findByTrackingNumber(String trackingNumber);
    
    List<Order> findByTrackingNumberIsNotNull();
    
    List<Order> findByTrackingNumberIsNull();
    
    // Payment intent queries
    Optional<Order> findByPaymentIntentId(String paymentIntentId);
    
    List<Order> findByPaymentIntentIdIsNotNull();
    
    // Coupon queries
    List<Order> findByCouponCode(String couponCode);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.couponCode = :couponCode")
    long countByCouponCode(@Param("couponCode") String couponCode);
}
