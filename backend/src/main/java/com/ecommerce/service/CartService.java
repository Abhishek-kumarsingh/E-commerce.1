package com.ecommerce.service;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductService productService;
    
    // Add to cart operations
    @Transactional
    public CartItem addToCart(Long userId, String productId, Integer quantity, Map<String, String> selectedVariants) {
        log.info("Adding product {} to cart for user {}", productId, userId);
        
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        
        if (!product.getIsActive()) {
            throw new IllegalArgumentException("Product is not active: " + productId);
        }
        
        if (!product.isInStock()) {
            throw new IllegalArgumentException("Product is out of stock: " + productId);
        }
        
        if (quantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }
        
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            if (newQuantity > product.getStockQuantity()) {
                throw new IllegalArgumentException("Total quantity would exceed available stock");
            }
            
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(product.getPrice());
            
            if (selectedVariants != null && !selectedVariants.isEmpty()) {
                cartItem.setSelectedVariants(selectedVariants);
            }
            
            CartItem updatedItem = cartItemRepository.save(cartItem);
            log.info("Updated existing cart item with ID: {}", updatedItem.getId());
            return updatedItem;
        } else {
            CartItem cartItem = new CartItem(productId, quantity, product.getPrice(), user);
            
            if (selectedVariants != null && !selectedVariants.isEmpty()) {
                cartItem.setSelectedVariants(selectedVariants);
            }
            
            CartItem savedItem = cartItemRepository.save(cartItem);
            log.info("Added new cart item with ID: {}", savedItem.getId());
            return savedItem;
        }
    }
    
    // Get cart operations
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public CartItem getCartItem(Long userId, Long itemId) {
        return cartItemRepository.findById(itemId)
                .filter(item -> item.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found or doesn't belong to user"));
    }
    
    public Optional<CartItem> findCartItem(Long userId, String productId) {
        return cartItemRepository.findByUserIdAndProductId(userId, productId);
    }
    
    // Update cart operations
    @Transactional
    public CartItem updateCartItemQuantity(Long userId, Long itemId, Integer newQuantity) {
        log.info("Updating cart item {} quantity to {} for user {}", itemId, newQuantity, userId);
        
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        CartItem cartItem = getCartItem(userId, itemId);
        Product product = productService.getProductById(cartItem.getProductId());
        
        if (newQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }
        
        cartItem.setQuantity(newQuantity);
        cartItem.setPrice(product.getPrice()); // Update price in case it changed
        
        CartItem updatedItem = cartItemRepository.save(cartItem);
        log.info("Cart item quantity updated successfully");
        return updatedItem;
    }
    
    @Transactional
    public CartItem updateCartItemVariants(Long userId, Long itemId, Map<String, String> selectedVariants) {
        log.info("Updating cart item {} variants for user {}", itemId, userId);
        
        CartItem cartItem = getCartItem(userId, itemId);
        cartItem.setSelectedVariants(selectedVariants);
        
        CartItem updatedItem = cartItemRepository.save(cartItem);
        log.info("Cart item variants updated successfully");
        return updatedItem;
    }
    
    // Remove from cart operations
    @Transactional
    public void removeFromCart(Long userId, Long itemId) {
        log.info("Removing cart item {} for user {}", itemId, userId);
        
        CartItem cartItem = getCartItem(userId, itemId);
        cartItemRepository.delete(cartItem);
        
        log.info("Cart item removed successfully");
    }
    
    @Transactional
    public void removeProductFromCart(Long userId, String productId) {
        log.info("Removing product {} from cart for user {}", productId, userId);
        
        int deletedCount = cartItemRepository.deleteByUserIdAndProductId(userId, productId);
        
        if (deletedCount == 0) {
            throw new IllegalArgumentException("Product not found in cart");
        }
        
        log.info("Product removed from cart successfully");
    }
    
    @Transactional
    public void clearCart(Long userId) {
        log.info("Clearing cart for user {}", userId);
        
        int deletedCount = cartItemRepository.deleteByUserId(userId);
        log.info("Cleared {} items from cart for user {}", deletedCount, userId);
    }
    
    // Cart summary and calculations
    public CartSummary getCartSummary(Long userId) {
        List<CartItem> cartItems = getCartItems(userId);
        
        if (cartItems.isEmpty()) {
            return new CartSummary(0, 0, BigDecimal.ZERO);
        }
        
        int totalItems = cartItems.size();
        int totalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new CartSummary(totalItems, totalQuantity, totalAmount);
    }
    
    public int getCartItemCount(Long userId) {
        return (int) cartItemRepository.countByUserId(userId);
    }
    
    public int getCartTotalQuantity(Long userId) {
        Long totalQuantity = cartItemRepository.sumQuantityByUserId(userId);
        return totalQuantity != null ? totalQuantity.intValue() : 0;
    }
    
    public BigDecimal getCartTotalAmount(Long userId) {
        BigDecimal totalAmount = cartItemRepository.sumTotalByUserId(userId);
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }
    
    // Validation methods
    public boolean isProductInCart(Long userId, String productId) {
        return cartItemRepository.existsByUserIdAndProductId(userId, productId);
    }
    
    public boolean hasCartItems(Long userId) {
        return cartItemRepository.existsByUserId(userId);
    }
    
    @Transactional
    public void validateCartItems(Long userId) {
        log.info("Validating cart items for user {}", userId);
        
        List<CartItem> cartItems = getCartItems(userId);
        
        for (CartItem cartItem : cartItems) {
            try {
                Product product = productService.getProductById(cartItem.getProductId());
                
                // Check if product is still active
                if (!product.getIsActive()) {
                    log.warn("Removing inactive product {} from cart", cartItem.getProductId());
                    cartItemRepository.delete(cartItem);
                    continue;
                }
                
                // Check stock availability
                if (cartItem.getQuantity() > product.getStockQuantity()) {
                    if (product.getStockQuantity() > 0) {
                        log.warn("Reducing quantity for product {} from {} to {}", 
                                cartItem.getProductId(), cartItem.getQuantity(), product.getStockQuantity());
                        cartItem.setQuantity(product.getStockQuantity());
                        cartItemRepository.save(cartItem);
                    } else {
                        log.warn("Removing out of stock product {} from cart", cartItem.getProductId());
                        cartItemRepository.delete(cartItem);
                    }
                }
                
                // Update price if it has changed
                if (!cartItem.getPrice().equals(product.getPrice())) {
                    log.info("Updating price for product {} from {} to {}", 
                            cartItem.getProductId(), cartItem.getPrice(), product.getPrice());
                    cartItem.setPrice(product.getPrice());
                    cartItemRepository.save(cartItem);
                }
                
            } catch (Exception e) {
                log.error("Error validating cart item {}: {}", cartItem.getId(), e.getMessage());
                // Remove invalid items
                cartItemRepository.delete(cartItem);
            }
        }
        
        log.info("Cart validation completed for user {}", userId);
    }
    
    // Cleanup operations
    @Transactional
    public void cleanupOldCartItems(int daysOld) {
        log.info("Cleaning up cart items older than {} days", daysOld);
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        int deletedCount = cartItemRepository.deleteOldCartItems(cutoffDate);
        
        log.info("Cleaned up {} old cart items", deletedCount);
    }
    
    // Transfer cart (for user login/merge scenarios)
    @Transactional
    public void transferCart(Long fromUserId, Long toUserId) {
        log.info("Transferring cart from user {} to user {}", fromUserId, toUserId);
        
        List<CartItem> fromUserItems = getCartItems(fromUserId);
        User toUser = userService.getUserById(toUserId);
        
        for (CartItem item : fromUserItems) {
            Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(toUserId, item.getProductId());
            
            if (existingItem.isPresent()) {
                // Merge quantities
                CartItem existing = existingItem.get();
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                cartItemRepository.save(existing);
            } else {
                // Transfer item
                item.setUser(toUser);
                cartItemRepository.save(item);
            }
        }
        
        // Clear original cart
        clearCart(fromUserId);
        
        log.info("Cart transfer completed");
    }
    
    // Inner class for cart summary
    public static class CartSummary {
        private final int itemCount;
        private final int totalQuantity;
        private final BigDecimal totalAmount;
        
        public CartSummary(int itemCount, int totalQuantity, BigDecimal totalAmount) {
            this.itemCount = itemCount;
            this.totalQuantity = totalQuantity;
            this.totalAmount = totalAmount;
        }
        
        public int getItemCount() { return itemCount; }
        public int getTotalQuantity() { return totalQuantity; }
        public BigDecimal getTotalAmount() { return totalAmount; }
    }
}
