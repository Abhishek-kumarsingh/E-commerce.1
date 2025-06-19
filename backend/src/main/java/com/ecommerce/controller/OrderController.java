package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.entity.enums.OrderStatus;
import com.ecommerce.entity.enums.PaymentMethod;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {
    
    private final OrderService orderService;
    private final UserService userService;
    
    @GetMapping
    @Operation(summary = "Get user orders", description = "Retrieve paginated list of user's orders")
    public ResponseEntity<ApiResponse<Page<Order>>> getUserOrders(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Order> orders = orderService.getUserOrders(user.getId(), pageable);
            
            return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve user orders: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve orders"));
        }
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders", description = "Retrieve paginated list of all orders (Admin only)")
    public ResponseEntity<ApiResponse<Page<Order>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String search) {
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Order> orders;
            
            if (search != null && !search.trim().isEmpty()) {
                orders = orderService.searchOrders(search, pageable);
            } else if (status != null) {
                orders = orderService.getOrdersByStatus(status, pageable);
            } else {
                orders = orderService.getAllOrders(pageable);
            }
            
            return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve all orders: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve orders"));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    public ResponseEntity<ApiResponse<Order>> getOrderById(
            @PathVariable Long id,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Order order = orderService.getOrderWithItems(id);
            
            // Check if user owns this order or is admin
            if (!order.getUser().getId().equals(user.getId()) && !user.getRole().isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(order, "Order retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve order {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Order not found"));
        }
    }
    
    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by number", description = "Retrieve a specific order by its order number")
    public ResponseEntity<ApiResponse<Order>> getOrderByNumber(
            @PathVariable String orderNumber,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Order order = orderService.getOrderByNumber(orderNumber);
            
            // Check if user owns this order or is admin
            if (!order.getUser().getId().equals(user.getId()) && !user.getRole().isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(order, "Order retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve order {}: {}", orderNumber, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Order not found"));
        }
    }
    
    @PostMapping
    @Operation(summary = "Create order", description = "Create a new order from cart items")
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            
            OrderService.CreateOrderRequest orderRequest = new OrderService.CreateOrderRequest();
            orderRequest.setPaymentMethod(request.getPaymentMethod());
            orderRequest.setShippingAddress(request.getShippingAddress());
            orderRequest.setBillingAddress(request.getBillingAddress());
            orderRequest.setNotes(request.getNotes());
            orderRequest.setCouponCode(request.getCouponCode());
            
            Order order = orderService.createOrder(user.getId(), orderRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(order, "Order created successfully"));
            
        } catch (Exception e) {
            log.error("Failed to create order: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create order: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Update the status of an order (Admin only)")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, OrderStatus> request) {
        
        try {
            OrderStatus newStatus = request.get("status");
            if (newStatus == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Status is required"));
            }
            
            Order order = orderService.updateOrderStatus(id, newStatus);
            
            return ResponseEntity.ok(ApiResponse.success(order, "Order status updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update order status for {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update order status: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/tracking")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update tracking number", description = "Update the tracking number of an order (Admin only)")
    public ResponseEntity<ApiResponse<Order>> updateTrackingNumber(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        
        try {
            String trackingNumber = request.get("trackingNumber");
            if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Tracking number is required"));
            }
            
            Order order = orderService.updateTrackingNumber(id, trackingNumber);
            
            return ResponseEntity.ok(ApiResponse.success(order, "Tracking number updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update tracking number for {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update tracking number: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/delivery")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update estimated delivery", description = "Update the estimated delivery date (Admin only)")
    public ResponseEntity<ApiResponse<Order>> updateEstimatedDelivery(
            @PathVariable Long id,
            @RequestBody Map<String, LocalDateTime> request) {
        
        try {
            LocalDateTime estimatedDelivery = request.get("estimatedDelivery");
            if (estimatedDelivery == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Estimated delivery date is required"));
            }
            
            Order order = orderService.updateEstimatedDelivery(id, estimatedDelivery);
            
            return ResponseEntity.ok(ApiResponse.success(order, "Estimated delivery updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update estimated delivery for {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update estimated delivery: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an order")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Order order = orderService.getOrderById(id);
            
            // Check if user owns this order or is admin
            if (!order.getUser().getId().equals(user.getId()) && !user.getRole().isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied"));
            }
            
            String reason = request != null ? request.get("reason") : "Cancelled by user";
            Order cancelledOrder = orderService.cancelOrder(id, reason);
            
            return ResponseEntity.ok(ApiResponse.success(cancelledOrder, "Order cancelled successfully"));
            
        } catch (Exception e) {
            log.error("Failed to cancel order {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to cancel order: " + e.getMessage()));
        }
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get recent orders", description = "Get recent orders for admin dashboard (Admin only)")
    public ResponseEntity<ApiResponse<List<Order>>> getRecentOrders(
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<Order> orders = orderService.getRecentOrders(limit);
            
            return ResponseEntity.ok(ApiResponse.success(orders, "Recent orders retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve recent orders: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve recent orders"));
        }
    }
    
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get order statistics", description = "Get order statistics for admin dashboard (Admin only)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOrderStats() {
        try {
            Map<String, Object> stats = Map.of(
                    "totalOrders", orderService.getTotalOrderCount(),
                    "pendingOrders", orderService.getOrderCountByStatus(OrderStatus.PENDING),
                    "confirmedOrders", orderService.getOrderCountByStatus(OrderStatus.CONFIRMED),
                    "shippedOrders", orderService.getOrderCountByStatus(OrderStatus.SHIPPED),
                    "deliveredOrders", orderService.getOrderCountByStatus(OrderStatus.DELIVERED),
                    "cancelledOrders", orderService.getOrderCountByStatus(OrderStatus.CANCELLED),
                    "totalRevenue", orderService.getTotalRevenue()
            );
            
            return ResponseEntity.ok(ApiResponse.success(stats, "Order statistics retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve order statistics: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve order statistics"));
        }
    }
    
    // Request DTOs
    public static class CreateOrderRequest {
        @NotNull(message = "Payment method is required")
        private PaymentMethod paymentMethod;
        
        @NotNull(message = "Shipping address is required")
        private Address shippingAddress;
        
        @NotNull(message = "Billing address is required")
        private Address billingAddress;
        
        private String notes;
        private String couponCode;
        
        // Getters and setters
        public PaymentMethod getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public Address getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }
        
        public Address getBillingAddress() { return billingAddress; }
        public void setBillingAddress(Address billingAddress) { this.billingAddress = billingAddress; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        
        public String getCouponCode() { return couponCode; }
        public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    }
}
