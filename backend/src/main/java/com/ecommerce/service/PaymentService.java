package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.Payment;
import com.ecommerce.entity.enums.PaymentMethod;
import com.ecommerce.entity.enums.PaymentStatus;
import com.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    
    // Create operations
    @Transactional
    public Payment createPayment(Long orderId, PaymentMethod paymentMethod, String currency) {
        log.info("Creating payment for order {}", orderId);
        
        Order order = orderService.getOrderById(orderId);
        
        // Check if payment already exists for this order
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment.isPresent()) {
            throw new IllegalArgumentException("Payment already exists for order: " + orderId);
        }
        
        Payment payment = new Payment(order.getTotal(), currency, paymentMethod, order);
        
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created successfully with ID: {} and reference: {}", 
                savedPayment.getId(), savedPayment.getPaymentReference());
        
        return savedPayment;
    }
    
    // Read operations
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }
    
    public Optional<Payment> findByPaymentReference(String paymentReference) {
        return paymentRepository.findByPaymentReference(paymentReference);
    }
    
    public Optional<Payment> findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
    
    public Optional<Payment> findByGatewayTransactionId(String transactionId) {
        return paymentRepository.findByGatewayTransactionId(transactionId);
    }
    
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + id));
    }
    
    public Payment getPaymentByReference(String paymentReference) {
        return paymentRepository.findByPaymentReference(paymentReference)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with reference: " + paymentReference));
    }
    
    public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
    
    public Page<Payment> getPaymentsByStatus(PaymentStatus status, Pageable pageable) {
        return paymentRepository.findByStatus(status, pageable);
    }
    
    public Page<Payment> getPaymentsByMethod(PaymentMethod paymentMethod, Pageable pageable) {
        return paymentRepository.findByPaymentMethod(paymentMethod, pageable);
    }
    
    public List<Payment> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
    
    // Payment processing operations
    @Transactional
    public Payment processPayment(String paymentReference, ProcessPaymentRequest request) {
        log.info("Processing payment with reference: {}", paymentReference);
        
        Payment payment = getPaymentByReference(paymentReference);
        
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Payment is not in pending status: " + payment.getStatus());
        }
        
        try {
            // Mark as processing
            payment.markAsProcessing();
            paymentRepository.save(payment);
            
            // Process payment based on method
            PaymentResult result = processPaymentByMethod(payment, request);
            
            if (result.isSuccess()) {
                payment.markAsCompleted(result.getTransactionId());
                payment.setGatewayResponse(result.getResponse());
                
                // Update order payment status
                orderService.updatePaymentStatus(payment.getOrder().getId(), PaymentStatus.COMPLETED);
                
                log.info("Payment processed successfully: {}", paymentReference);
            } else {
                payment.markAsFailed(result.getFailureReason());
                payment.setGatewayResponse(result.getResponse());
                
                // Update order payment status
                orderService.updatePaymentStatus(payment.getOrder().getId(), PaymentStatus.FAILED);
                
                log.warn("Payment processing failed: {} - {}", paymentReference, result.getFailureReason());
            }
            
            return paymentRepository.save(payment);
            
        } catch (Exception e) {
            log.error("Error processing payment {}: {}", paymentReference, e.getMessage());
            payment.markAsFailed("Processing error: " + e.getMessage());
            paymentRepository.save(payment);
            throw new RuntimeException("Payment processing failed", e);
        }
    }
    
    @Transactional
    public Payment cancelPayment(String paymentReference, String reason) {
        log.info("Cancelling payment with reference: {}", paymentReference);
        
        Payment payment = getPaymentByReference(paymentReference);
        
        if (!payment.isPending()) {
            throw new IllegalArgumentException("Payment cannot be cancelled in current status: " + payment.getStatus());
        }
        
        payment.markAsCancelled();
        payment.setFailureReason(reason);
        
        // Update order payment status
        orderService.updatePaymentStatus(payment.getOrder().getId(), PaymentStatus.CANCELLED);
        
        Payment cancelledPayment = paymentRepository.save(payment);
        log.info("Payment cancelled successfully: {}", paymentReference);
        
        return cancelledPayment;
    }
    
    // Refund operations
    @Transactional
    public Payment processRefund(String paymentReference, BigDecimal refundAmount, String reason) {
        log.info("Processing refund for payment {} - Amount: {}", paymentReference, refundAmount);
        
        Payment payment = getPaymentByReference(paymentReference);
        
        if (!payment.canBeRefunded()) {
            throw new IllegalArgumentException("Payment cannot be refunded in current status: " + payment.getStatus());
        }
        
        if (refundAmount.compareTo(payment.getRefundableAmount()) > 0) {
            throw new IllegalArgumentException("Refund amount exceeds refundable amount");
        }
        
        try {
            // Process refund through payment gateway
            RefundResult result = processRefundByMethod(payment, refundAmount, reason);
            
            if (result.isSuccess()) {
                payment.processRefund(refundAmount);
                payment.setNotes(payment.getNotes() != null ? 
                        payment.getNotes() + "\nRefund: " + reason : "Refund: " + reason);
                
                // Update order payment status if fully refunded
                if (payment.getStatus() == PaymentStatus.REFUNDED) {
                    orderService.updatePaymentStatus(payment.getOrder().getId(), PaymentStatus.REFUNDED);
                }
                
                log.info("Refund processed successfully: {} - Amount: {}", paymentReference, refundAmount);
            } else {
                log.error("Refund processing failed: {} - {}", paymentReference, result.getFailureReason());
                throw new RuntimeException("Refund processing failed: " + result.getFailureReason());
            }
            
            return paymentRepository.save(payment);
            
        } catch (Exception e) {
            log.error("Error processing refund {}: {}", paymentReference, e.getMessage());
            throw new RuntimeException("Refund processing failed", e);
        }
    }
    
    // Statistics and reporting
    public long getTotalPaymentCount() {
        return paymentRepository.count();
    }
    
    public long getPaymentCountByStatus(PaymentStatus status) {
        return paymentRepository.countByStatus(status);
    }
    
    public long getPaymentCountByMethod(PaymentMethod paymentMethod) {
        return paymentRepository.countByPaymentMethod(paymentMethod);
    }
    
    public BigDecimal getTotalAmountByStatus(PaymentStatus status) {
        return paymentRepository.sumAmountByStatus(status);
    }
    
    public BigDecimal getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.sumAmountByStatusAndDateRange(PaymentStatus.COMPLETED, startDate, endDate);
    }
    
    public BigDecimal getTotalRefundedAmount() {
        return paymentRepository.sumTotalRefundedAmount();
    }
    
    public List<Object[]> getPaymentMethodStats() {
        return paymentRepository.getPaymentMethodStatsByStatus(PaymentStatus.COMPLETED);
    }
    
    public List<Object[]> getDailyPaymentStats(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.getDailyPaymentStats(startDate, endDate);
    }
    
    public List<Payment> getRecentPayments(int limit) {
        return paymentRepository.findRecentPayments(Pageable.ofSize(limit));
    }
    
    public List<Payment> getFailedPayments() {
        return paymentRepository.findFailedPaymentsWithReason();
    }
    
    // Private helper methods
    private PaymentResult processPaymentByMethod(Payment payment, ProcessPaymentRequest request) {
        switch (payment.getPaymentMethod()) {
            case CARD:
                return processCardPayment(payment, request);
            case UPI:
                return processUpiPayment(payment, request);
            case WALLET:
                return processWalletPayment(payment, request);
            case BANK_TRANSFER:
                return processBankTransferPayment(payment, request);
            case COD:
                return processCodPayment(payment, request);
            case NET_BANKING:
                return processNetBankingPayment(payment, request);
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + payment.getPaymentMethod());
        }
    }
    
    private PaymentResult processCardPayment(Payment payment, ProcessPaymentRequest request) {
        // Simulate card payment processing
        log.info("Processing card payment for amount: {}", payment.getAmount());
        
        // In real implementation, integrate with Stripe, Razorpay, etc.
        if (request.getCardNumber() != null && request.getCardNumber().startsWith("4")) {
            return new PaymentResult(true, "TXN_" + System.currentTimeMillis(), "Payment successful", null);
        } else {
            return new PaymentResult(false, null, "Payment failed", "Invalid card number");
        }
    }
    
    private PaymentResult processUpiPayment(Payment payment, ProcessPaymentRequest request) {
        // Simulate UPI payment processing
        log.info("Processing UPI payment for amount: {}", payment.getAmount());
        
        if (request.getUpiId() != null && request.getUpiId().contains("@")) {
            return new PaymentResult(true, "UPI_" + System.currentTimeMillis(), "UPI payment successful", null);
        } else {
            return new PaymentResult(false, null, "UPI payment failed", "Invalid UPI ID");
        }
    }
    
    private PaymentResult processWalletPayment(Payment payment, ProcessPaymentRequest request) {
        // Simulate wallet payment processing
        log.info("Processing wallet payment for amount: {}", payment.getAmount());
        return new PaymentResult(true, "WALLET_" + System.currentTimeMillis(), "Wallet payment successful", null);
    }
    
    private PaymentResult processBankTransferPayment(Payment payment, ProcessPaymentRequest request) {
        // Simulate bank transfer payment processing
        log.info("Processing bank transfer payment for amount: {}", payment.getAmount());
        return new PaymentResult(true, "BANK_" + System.currentTimeMillis(), "Bank transfer successful", null);
    }
    
    private PaymentResult processCodPayment(Payment payment, ProcessPaymentRequest request) {
        // COD payments are automatically successful
        log.info("Processing COD payment for amount: {}", payment.getAmount());
        return new PaymentResult(true, "COD_" + System.currentTimeMillis(), "COD payment confirmed", null);
    }
    
    private PaymentResult processNetBankingPayment(Payment payment, ProcessPaymentRequest request) {
        // Simulate net banking payment processing
        log.info("Processing net banking payment for amount: {}", payment.getAmount());
        return new PaymentResult(true, "NB_" + System.currentTimeMillis(), "Net banking payment successful", null);
    }
    
    private RefundResult processRefundByMethod(Payment payment, BigDecimal refundAmount, String reason) {
        // Simulate refund processing
        log.info("Processing refund for payment method: {} - Amount: {}", payment.getPaymentMethod(), refundAmount);
        
        // In real implementation, call appropriate gateway APIs
        return new RefundResult(true, "REF_" + System.currentTimeMillis(), "Refund processed successfully", null);
    }
    
    // Helper classes
    public static class ProcessPaymentRequest {
        private String cardNumber;
        private String expiryMonth;
        private String expiryYear;
        private String cvv;
        private String upiId;
        private String bankCode;
        
        // Getters and setters
        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
        
        public String getExpiryMonth() { return expiryMonth; }
        public void setExpiryMonth(String expiryMonth) { this.expiryMonth = expiryMonth; }
        
        public String getExpiryYear() { return expiryYear; }
        public void setExpiryYear(String expiryYear) { this.expiryYear = expiryYear; }
        
        public String getCvv() { return cvv; }
        public void setCvv(String cvv) { this.cvv = cvv; }
        
        public String getUpiId() { return upiId; }
        public void setUpiId(String upiId) { this.upiId = upiId; }
        
        public String getBankCode() { return bankCode; }
        public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    }
    
    private static class PaymentResult {
        private final boolean success;
        private final String transactionId;
        private final String response;
        private final String failureReason;
        
        public PaymentResult(boolean success, String transactionId, String response, String failureReason) {
            this.success = success;
            this.transactionId = transactionId;
            this.response = response;
            this.failureReason = failureReason;
        }
        
        public boolean isSuccess() { return success; }
        public String getTransactionId() { return transactionId; }
        public String getResponse() { return response; }
        public String getFailureReason() { return failureReason; }
    }
    
    private static class RefundResult {
        private final boolean success;
        private final String refundId;
        private final String response;
        private final String failureReason;
        
        public RefundResult(boolean success, String refundId, String response, String failureReason) {
            this.success = success;
            this.refundId = refundId;
            this.response = response;
            this.failureReason = failureReason;
        }
        
        public boolean isSuccess() { return success; }
        public String getRefundId() { return refundId; }
        public String getResponse() { return response; }
        public String getFailureReason() { return failureReason; }
    }
}
