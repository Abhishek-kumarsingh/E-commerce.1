package com.ecommerce.service;

import com.ecommerce.entity.*;
import com.ecommerce.entity.enums.OrderStatus;
import com.ecommerce.entity.enums.PaymentMethod;
import com.ecommerce.entity.enums.PaymentStatus;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.OrderItemRepository;
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
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    
    // Create operations
    @Transactional
    public Order createOrder(Long userId, CreateOrderRequest request) {
        log.info("Creating order for user {}", userId);
        
        User user = userService.getUserById(userId);
        
        // Validate cart items
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        
        // Validate stock availability
        cartService.validateCartItems(userId);
        
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddress(new AddressInfo(request.getShippingAddress()));
        order.setBillingAddress(new AddressInfo(request.getBillingAddress()));
        order.setNotes(request.getNotes());
        
        // Calculate totals
        BigDecimal subtotal = calculateSubtotal(cartItems);
        BigDecimal tax = calculateTax(subtotal);
        BigDecimal shipping = calculateShipping(request.getShippingAddress(), cartItems);
        BigDecimal discount = calculateDiscount(subtotal, request.getCouponCode());
        
        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setShipping(shipping);
        order.setDiscount(discount);
        order.setCouponCode(request.getCouponCode());
        order.updateTotal();
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Create order items
        for (CartItem cartItem : cartItems) {
            Product product = productService.getProductById(cartItem.getProductId());
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductSku(product.getSku());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getPrice());
            orderItem.setSelectedVariants(cartItem.getSelectedVariants());
            orderItem.updateTotalPrice();
            
            orderItemRepository.save(orderItem);
            
            // Update product stock and sales count
            productService.updateStock(product.getId(), 
                    product.getStockQuantity() - cartItem.getQuantity());
            productService.incrementSalesCount(product.getId(), cartItem.getQuantity());
        }
        
        // Clear cart
        cartService.clearCart(userId);
        
        log.info("Order created successfully with ID: {} and order number: {}", 
                savedOrder.getId(), savedOrder.getOrderNumber());
        
        return savedOrder;
    }
    
    // Read operations
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }
    
    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with number: " + orderNumber));
    }
    
    public Order getOrderWithItems(Long id) {
        return orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }
    
    public Page<Order> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    public Page<Order> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }
    
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
    public Page<Order> searchOrders(String keyword, Pageable pageable) {
        return orderRepository.searchOrders(keyword, pageable);
    }
    
    // Update operations
    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus newStatus) {
        log.info("Updating order {} status to {}", id, newStatus);
        
        Order order = getOrderById(id);
        OrderStatus oldStatus = order.getStatus();
        
        // Validate status transition
        validateStatusTransition(oldStatus, newStatus);
        
        order.updateStatus(newStatus);
        
        // Handle stock restoration for cancelled orders
        if (newStatus == OrderStatus.CANCELLED && oldStatus != OrderStatus.CANCELLED) {
            restoreStock(order);
        }
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated successfully from {} to {}", oldStatus, newStatus);
        
        return updatedOrder;
    }
    
    @Transactional
    public Order updatePaymentStatus(Long id, PaymentStatus newPaymentStatus) {
        log.info("Updating order {} payment status to {}", id, newPaymentStatus);
        
        Order order = getOrderById(id);
        order.updatePaymentStatus(newPaymentStatus);
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Order payment status updated successfully");
        
        return updatedOrder;
    }
    
    @Transactional
    public Order updateTrackingNumber(Long id, String trackingNumber) {
        log.info("Updating order {} tracking number to {}", id, trackingNumber);
        
        Order order = getOrderById(id);
        order.setTrackingNumber(trackingNumber);
        
        if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.PROCESSING) {
            order.updateStatus(OrderStatus.SHIPPED);
        }
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Order tracking number updated successfully");
        
        return updatedOrder;
    }
    
    @Transactional
    public Order updateEstimatedDelivery(Long id, LocalDateTime estimatedDelivery) {
        log.info("Updating order {} estimated delivery to {}", id, estimatedDelivery);
        
        Order order = getOrderById(id);
        order.setEstimatedDelivery(estimatedDelivery);
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Order estimated delivery updated successfully");
        
        return updatedOrder;
    }
    
    // Cancel and refund operations
    @Transactional
    public Order cancelOrder(Long id, String reason) {
        log.info("Cancelling order {} with reason: {}", id, reason);
        
        Order order = getOrderById(id);
        
        if (!order.canBeCancelled()) {
            throw new IllegalArgumentException("Order cannot be cancelled in current status: " + order.getStatus());
        }
        
        order.updateStatus(OrderStatus.CANCELLED);
        order.setNotes(order.getNotes() != null ? order.getNotes() + "\nCancellation reason: " + reason : "Cancellation reason: " + reason);
        
        // Restore stock
        restoreStock(order);
        
        Order cancelledOrder = orderRepository.save(order);
        log.info("Order cancelled successfully");
        
        return cancelledOrder;
    }
    
    // Utility methods
    public long getTotalOrderCount() {
        return orderRepository.count();
    }
    
    public long getOrderCountByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
    
    public long getUserOrderCount(Long userId) {
        return orderRepository.countByUserId(userId);
    }
    
    public BigDecimal getTotalRevenue() {
        return orderRepository.sumTotalByStatus(OrderStatus.DELIVERED);
    }
    
    public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.sumTotalBetweenDates(startDate, endDate);
    }
    
    public List<Order> getRecentOrders(int limit) {
        return orderRepository.findRecentOrdersByStatuses(
                List.of(OrderStatus.PENDING, OrderStatus.CONFIRMED, OrderStatus.PROCESSING), 
                Pageable.ofSize(limit)
        );
    }
    
    // Private helper methods
    private BigDecimal calculateSubtotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateTax(BigDecimal subtotal) {
        // Simple tax calculation - 10%
        return subtotal.multiply(BigDecimal.valueOf(0.10));
    }
    
    private BigDecimal calculateShipping(Address shippingAddress, List<CartItem> cartItems) {
        // Simple shipping calculation
        BigDecimal baseShipping = BigDecimal.valueOf(10.00);
        
        // Free shipping for orders over $100
        BigDecimal subtotal = calculateSubtotal(cartItems);
        if (subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
            return BigDecimal.ZERO;
        }
        
        return baseShipping;
    }
    
    private BigDecimal calculateDiscount(BigDecimal subtotal, String couponCode) {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        // Simple coupon logic - 10% discount for "SAVE10"
        if ("SAVE10".equals(couponCode)) {
            return subtotal.multiply(BigDecimal.valueOf(0.10));
        }
        
        return BigDecimal.ZERO;
    }
    
    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        // Define valid transitions
        switch (from) {
            case PENDING:
                if (to != OrderStatus.CONFIRMED && to != OrderStatus.CANCELLED) {
                    throw new IllegalArgumentException("Invalid status transition from " + from + " to " + to);
                }
                break;
            case CONFIRMED:
                if (to != OrderStatus.PROCESSING && to != OrderStatus.CANCELLED) {
                    throw new IllegalArgumentException("Invalid status transition from " + from + " to " + to);
                }
                break;
            case PROCESSING:
                if (to != OrderStatus.SHIPPED && to != OrderStatus.CANCELLED) {
                    throw new IllegalArgumentException("Invalid status transition from " + from + " to " + to);
                }
                break;
            case SHIPPED:
                if (to != OrderStatus.OUT_FOR_DELIVERY && to != OrderStatus.DELIVERED) {
                    throw new IllegalArgumentException("Invalid status transition from " + from + " to " + to);
                }
                break;
            case OUT_FOR_DELIVERY:
                if (to != OrderStatus.DELIVERED) {
                    throw new IllegalArgumentException("Invalid status transition from " + from + " to " + to);
                }
                break;
            case DELIVERED:
                if (to != OrderStatus.REFUNDED) {
                    throw new IllegalArgumentException("Invalid status transition from " + from + " to " + to);
                }
                break;
            case CANCELLED:
            case REFUNDED:
                throw new IllegalArgumentException("Cannot change status from " + from);
        }
    }
    
    private void restoreStock(Order order) {
        log.info("Restoring stock for cancelled order {}", order.getId());
        
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        
        for (OrderItem item : orderItems) {
            try {
                Product product = productService.getProductById(item.getProductId());
                int newStock = product.getStockQuantity() + item.getQuantity();
                productService.updateStock(product.getId(), newStock);
                
                // Note: Sales count adjustment would be handled by ProductService if needed
                
            } catch (Exception e) {
                log.error("Error restoring stock for product {}: {}", item.getProductId(), e.getMessage());
            }
        }
        
        log.info("Stock restoration completed for order {}", order.getId());
    }
    
    // Request DTOs
    public static class CreateOrderRequest {
        private PaymentMethod paymentMethod;
        private Address shippingAddress;
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
