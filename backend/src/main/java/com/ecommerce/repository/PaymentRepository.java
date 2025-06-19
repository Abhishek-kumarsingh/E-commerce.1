package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.Payment;
import com.ecommerce.entity.enums.PaymentMethod;
import com.ecommerce.entity.enums.PaymentStatus;
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
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // Basic queries
    Optional<Payment> findByPaymentReference(String paymentReference);
    
    boolean existsByPaymentReference(String paymentReference);
    
    // Order-based queries
    Optional<Payment> findByOrder(Order order);
    
    Optional<Payment> findByOrderId(Long orderId);
    
    List<Payment> findByOrderIn(List<Order> orders);
    
    // Status-based queries
    List<Payment> findByStatus(PaymentStatus status);
    
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
    
    List<Payment> findByStatusIn(List<PaymentStatus> statuses);
    
    Page<Payment> findByStatusIn(List<PaymentStatus> statuses, Pageable pageable);
    
    // Payment method queries
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    
    Page<Payment> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);
    
    List<Payment> findByPaymentMethodIn(List<PaymentMethod> paymentMethods);
    
    // Gateway queries
    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);
    
    List<Payment> findByGatewayTransactionIdIsNotNull();
    
    List<Payment> findByGatewayTransactionIdIsNull();
    
    // Amount-based queries
    @Query("SELECT p FROM Payment p WHERE p.amount >= :minAmount")
    List<Payment> findByMinAmount(@Param("minAmount") BigDecimal minAmount);
    
    @Query("SELECT p FROM Payment p WHERE p.amount BETWEEN :minAmount AND :maxAmount")
    List<Payment> findByAmountRange(@Param("minAmount") BigDecimal minAmount, 
                                   @Param("maxAmount") BigDecimal maxAmount);
    
    @Query("SELECT p FROM Payment p WHERE p.netAmount >= :minNetAmount")
    List<Payment> findByMinNetAmount(@Param("minNetAmount") BigDecimal minNetAmount);
    
    // Currency queries
    List<Payment> findByCurrency(String currency);
    
    Page<Payment> findByCurrency(String currency, Pageable pageable);
    
    // Date-based queries
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<Payment> findByProcessedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT p FROM Payment p WHERE p.createdAt >= :date")
    List<Payment> findPaymentsCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT p FROM Payment p WHERE p.processedAt >= :date")
    List<Payment> findPaymentsProcessedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT p FROM Payment p WHERE p.processedAt IS NOT NULL")
    List<Payment> findProcessedPayments();
    
    @Query("SELECT p FROM Payment p WHERE p.processedAt IS NULL AND p.status = :status")
    List<Payment> findUnprocessedPaymentsByStatus(@Param("status") PaymentStatus status);
    
    // Refund queries
    List<Payment> findByRefundedAmountGreaterThan(BigDecimal amount);
    
    @Query("SELECT p FROM Payment p WHERE p.refundedAmount > 0")
    List<Payment> findRefundedPayments();
    
    @Query("SELECT p FROM Payment p WHERE p.refundedAt IS NOT NULL")
    List<Payment> findPaymentsWithRefunds();
    
    @Query("SELECT p FROM Payment p WHERE p.refundedAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsRefundedBetween(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    // Count queries
    long countByStatus(PaymentStatus status);
    
    long countByPaymentMethod(PaymentMethod paymentMethod);
    
    long countByCurrency(String currency);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.createdAt >= :date")
    long countPaymentsCreatedAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.processedAt >= :date")
    long countPaymentsProcessedAfter(@Param("date") LocalDateTime date);
    
    // Sum queries
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") PaymentStatus status);
    
    @Query("SELECT SUM(p.netAmount) FROM Payment p WHERE p.status = :status")
    BigDecimal sumNetAmountByStatus(@Param("status") PaymentStatus status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByStatusAndDateRange(@Param("status") PaymentStatus status,
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.fee) FROM Payment p WHERE p.status = :status")
    BigDecimal sumFeeByStatus(@Param("status") PaymentStatus status);
    
    @Query("SELECT SUM(p.refundedAmount) FROM Payment p WHERE p.refundedAmount > 0")
    BigDecimal sumTotalRefundedAmount();
    
    // Statistics queries
    @Query("SELECT p.paymentMethod, COUNT(p), SUM(p.amount) FROM Payment p WHERE p.status = :status GROUP BY p.paymentMethod")
    List<Object[]> getPaymentMethodStatsByStatus(@Param("status") PaymentStatus status);
    
    @Query("SELECT p.currency, COUNT(p), SUM(p.amount) FROM Payment p WHERE p.status = :status GROUP BY p.currency")
    List<Object[]> getCurrencyStatsByStatus(@Param("status") PaymentStatus status);
    
    @Query("SELECT DATE(p.createdAt), COUNT(p), SUM(p.amount) FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate GROUP BY DATE(p.createdAt) ORDER BY DATE(p.createdAt)")
    List<Object[]> getDailyPaymentStats(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    // Complex queries with joins
    @Query("SELECT p FROM Payment p JOIN FETCH p.order WHERE p.id = :id")
    Optional<Payment> findByIdWithOrder(@Param("id") Long id);
    
    @Query("SELECT p FROM Payment p JOIN FETCH p.order o WHERE o.userId = :userId")
    List<Payment> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Payment p JOIN FETCH p.order o WHERE o.userId = :userId AND p.status = :status")
    List<Payment> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") PaymentStatus status);
    
    // Failed payments
    @Query("SELECT p FROM Payment p WHERE p.status = 'FAILED' AND p.failureReason IS NOT NULL")
    List<Payment> findFailedPaymentsWithReason();
    
    @Query("SELECT p.failureReason, COUNT(p) FROM Payment p WHERE p.status = 'FAILED' AND p.failureReason IS NOT NULL GROUP BY p.failureReason")
    List<Object[]> getFailureReasonStats();
    
    // Recent payments
    @Query("SELECT p FROM Payment p ORDER BY p.createdAt DESC")
    List<Payment> findRecentPayments(Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.status = :status ORDER BY p.createdAt DESC")
    List<Payment> findRecentPaymentsByStatus(@Param("status") PaymentStatus status, Pageable pageable);
}
