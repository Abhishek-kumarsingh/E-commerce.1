package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.entity.Payment;
import com.ecommerce.entity.User;
import com.ecommerce.entity.enums.PaymentMethod;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing endpoints")
public class PaymentController {
    
    private final PaymentService paymentService;
    private final UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all payments", description = "Retrieve paginated list of all payments (Admin only)")
    public ResponseEntity<ApiResponse<Page<Payment>>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Payment> payments = paymentService.getAllPayments(pageable);
            
            return ResponseEntity.ok(ApiResponse.success(payments, "Payments retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve payments: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve payments"));
        }
    }
    
    @GetMapping("/my")
    @Operation(summary = "Get user payments", description = "Retrieve user's payment history")
    public ResponseEntity<ApiResponse<List<Payment>>> getUserPayments(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            List<Payment> payments = paymentService.getUserPayments(user.getId());
            
            return ResponseEntity.ok(ApiResponse.success(payments, "User payments retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve user payments: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve payments"));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieve a specific payment by its ID")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(
            @PathVariable Long id,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Payment payment = paymentService.getPaymentById(id);
            
            // Check if user owns this payment or is admin
            if (!payment.getOrder().getUser().getId().equals(user.getId()) && !user.getRole().isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(payment, "Payment retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve payment {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Payment not found"));
        }
    }
    
    @GetMapping("/reference/{paymentReference}")
    @Operation(summary = "Get payment by reference", description = "Retrieve a payment by its reference number")
    public ResponseEntity<ApiResponse<Payment>> getPaymentByReference(
            @PathVariable String paymentReference,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Payment payment = paymentService.getPaymentByReference(paymentReference);
            
            // Check if user owns this payment or is admin
            if (!payment.getOrder().getUser().getId().equals(user.getId()) && !user.getRole().isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(payment, "Payment retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve payment {}: {}", paymentReference, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Payment not found"));
        }
    }
    
    @PostMapping("/create")
    @Operation(summary = "Create payment", description = "Create a payment for an order")
    public ResponseEntity<ApiResponse<Payment>> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            
            Payment payment = paymentService.createPayment(
                    request.getOrderId(),
                    request.getPaymentMethod(),
                    request.getCurrency()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(payment, "Payment created successfully"));
            
        } catch (Exception e) {
            log.error("Failed to create payment: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create payment: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{paymentReference}/process")
    @Operation(summary = "Process payment", description = "Process a payment using payment gateway")
    public ResponseEntity<ApiResponse<Payment>> processPayment(
            @PathVariable String paymentReference,
            @Valid @RequestBody ProcessPaymentRequest request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Payment payment = paymentService.getPaymentByReference(paymentReference);
            
            // Check if user owns this payment
            if (!payment.getOrder().getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied"));
            }
            
            PaymentService.ProcessPaymentRequest serviceRequest = new PaymentService.ProcessPaymentRequest();
            serviceRequest.setCardNumber(request.getCardNumber());
            serviceRequest.setExpiryMonth(request.getExpiryMonth());
            serviceRequest.setExpiryYear(request.getExpiryYear());
            serviceRequest.setCvv(request.getCvv());
            serviceRequest.setUpiId(request.getUpiId());
            serviceRequest.setBankCode(request.getBankCode());
            
            Payment processedPayment = paymentService.processPayment(paymentReference, serviceRequest);
            
            return ResponseEntity.ok(ApiResponse.success(processedPayment, "Payment processed successfully"));
            
        } catch (Exception e) {
            log.error("Failed to process payment {}: {}", paymentReference, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Payment processing failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{paymentReference}/cancel")
    @Operation(summary = "Cancel payment", description = "Cancel a pending payment")
    public ResponseEntity<ApiResponse<Payment>> cancelPayment(
            @PathVariable String paymentReference,
            @RequestBody(required = false) Map<String, String> request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Payment payment = paymentService.getPaymentByReference(paymentReference);
            
            // Check if user owns this payment or is admin
            if (!payment.getOrder().getUser().getId().equals(user.getId()) && !user.getRole().isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied"));
            }
            
            String reason = request != null ? request.get("reason") : "Cancelled by user";
            Payment cancelledPayment = paymentService.cancelPayment(paymentReference, reason);
            
            return ResponseEntity.ok(ApiResponse.success(cancelledPayment, "Payment cancelled successfully"));
            
        } catch (Exception e) {
            log.error("Failed to cancel payment {}: {}", paymentReference, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to cancel payment: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{paymentReference}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Process refund", description = "Process a refund for a payment (Admin only)")
    public ResponseEntity<ApiResponse<Payment>> processRefund(
            @PathVariable String paymentReference,
            @Valid @RequestBody RefundRequest request) {
        
        try {
            Payment refundedPayment = paymentService.processRefund(
                    paymentReference,
                    request.getRefundAmount(),
                    request.getReason()
            );
            
            return ResponseEntity.ok(ApiResponse.success(refundedPayment, "Refund processed successfully"));
            
        } catch (Exception e) {
            log.error("Failed to process refund for {}: {}", paymentReference, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to process refund: " + e.getMessage()));
        }
    }
    
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get payment statistics", description = "Get payment statistics for admin dashboard (Admin only)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPaymentStats() {
        try {
            Map<String, Object> stats = Map.of(
                    "totalPayments", paymentService.getTotalPaymentCount(),
                    "completedPayments", paymentService.getPaymentCountByStatus(com.ecommerce.entity.enums.PaymentStatus.COMPLETED),
                    "pendingPayments", paymentService.getPaymentCountByStatus(com.ecommerce.entity.enums.PaymentStatus.PENDING),
                    "failedPayments", paymentService.getPaymentCountByStatus(com.ecommerce.entity.enums.PaymentStatus.FAILED),
                    "totalRevenue", paymentService.getTotalAmountByStatus(com.ecommerce.entity.enums.PaymentStatus.COMPLETED),
                    "totalRefunded", paymentService.getTotalRefundedAmount(),
                    "paymentMethodStats", paymentService.getPaymentMethodStats()
            );
            
            return ResponseEntity.ok(ApiResponse.success(stats, "Payment statistics retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve payment statistics: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve payment statistics"));
        }
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get recent payments", description = "Get recent payments for admin dashboard (Admin only)")
    public ResponseEntity<ApiResponse<List<Payment>>> getRecentPayments(
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<Payment> payments = paymentService.getRecentPayments(limit);
            
            return ResponseEntity.ok(ApiResponse.success(payments, "Recent payments retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve recent payments: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve recent payments"));
        }
    }
    
    @GetMapping("/failed")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get failed payments", description = "Get failed payments for admin review (Admin only)")
    public ResponseEntity<ApiResponse<List<Payment>>> getFailedPayments() {
        try {
            List<Payment> payments = paymentService.getFailedPayments();
            
            return ResponseEntity.ok(ApiResponse.success(payments, "Failed payments retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve failed payments: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve failed payments"));
        }
    }
    
    // Request DTOs
    public static class CreatePaymentRequest {
        @NotNull(message = "Order ID is required")
        private Long orderId;
        
        @NotNull(message = "Payment method is required")
        private PaymentMethod paymentMethod;
        
        @NotBlank(message = "Currency is required")
        private String currency = "USD";
        
        // Getters and setters
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        
        public PaymentMethod getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }
    
    public static class ProcessPaymentRequest {
        // Card payment fields
        private String cardNumber;
        private String expiryMonth;
        private String expiryYear;
        private String cvv;
        
        // UPI payment fields
        private String upiId;
        
        // Bank transfer fields
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
    
    public static class RefundRequest {
        @NotNull(message = "Refund amount is required")
        @DecimalMin(value = "0.01", message = "Refund amount must be greater than 0")
        private BigDecimal refundAmount;
        
        @NotBlank(message = "Refund reason is required")
        private String reason;
        
        // Getters and setters
        public BigDecimal getRefundAmount() { return refundAmount; }
        public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}
