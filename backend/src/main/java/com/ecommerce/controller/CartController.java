package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Shopping cart management endpoints")
public class CartController {
    
    private final CartService cartService;
    private final UserService userService;
    
    @GetMapping
    @Operation(summary = "Get cart items", description = "Retrieve all items in user's cart")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartItems(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            List<CartItem> cartItems = cartService.getCartItems(user.getId());
            
            return ResponseEntity.ok(ApiResponse.success(cartItems, "Cart items retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve cart items: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve cart items"));
        }
    }
    
    @GetMapping("/summary")
    @Operation(summary = "Get cart summary", description = "Retrieve cart summary with totals")
    public ResponseEntity<ApiResponse<CartService.CartSummary>> getCartSummary(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            CartService.CartSummary summary = cartService.getCartSummary(user.getId());
            
            return ResponseEntity.ok(ApiResponse.success(summary, "Cart summary retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to retrieve cart summary: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve cart summary"));
        }
    }
    
    @PostMapping("/add")
    @Operation(summary = "Add item to cart", description = "Add a product to the shopping cart")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            
            CartItem cartItem = cartService.addToCart(
                    user.getId(),
                    request.getProductId(),
                    request.getQuantity(),
                    request.getSelectedVariants()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(cartItem, "Item added to cart successfully"));
            
        } catch (Exception e) {
            log.error("Failed to add item to cart: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to add item to cart: " + e.getMessage()));
        }
    }
    
    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update cart item", description = "Update quantity or variants of a cart item")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItem(
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            
            CartItem cartItem;
            if (request.getQuantity() != null) {
                cartItem = cartService.updateCartItemQuantity(user.getId(), itemId, request.getQuantity());
            } else if (request.getSelectedVariants() != null) {
                cartItem = cartService.updateCartItemVariants(user.getId(), itemId, request.getSelectedVariants());
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("No update data provided"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(cartItem, "Cart item updated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to update cart item {}: {}", itemId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update cart item: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove cart item", description = "Remove a specific item from the cart")
    public ResponseEntity<ApiResponse<String>> removeCartItem(
            @PathVariable Long itemId,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            cartService.removeFromCart(user.getId(), itemId);
            
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully"));
            
        } catch (Exception e) {
            log.error("Failed to remove cart item {}: {}", itemId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to remove cart item: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/products/{productId}")
    @Operation(summary = "Remove product from cart", description = "Remove all instances of a product from the cart")
    public ResponseEntity<ApiResponse<String>> removeProductFromCart(
            @PathVariable String productId,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            cartService.removeProductFromCart(user.getId(), productId);
            
            return ResponseEntity.ok(ApiResponse.success("Product removed from cart successfully"));
            
        } catch (Exception e) {
            log.error("Failed to remove product {} from cart: {}", productId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to remove product from cart: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart", description = "Remove all items from the cart")
    public ResponseEntity<ApiResponse<String>> clearCart(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            cartService.clearCart(user.getId());
            
            return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully"));
            
        } catch (Exception e) {
            log.error("Failed to clear cart: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to clear cart"));
        }
    }
    
    @PostMapping("/validate")
    @Operation(summary = "Validate cart", description = "Validate cart items for stock availability and pricing")
    public ResponseEntity<ApiResponse<String>> validateCart(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            cartService.validateCartItems(user.getId());
            
            return ResponseEntity.ok(ApiResponse.success("Cart validated successfully"));
            
        } catch (Exception e) {
            log.error("Failed to validate cart: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to validate cart"));
        }
    }
    
    @GetMapping("/count")
    @Operation(summary = "Get cart item count", description = "Get the total number of items in the cart")
    public ResponseEntity<ApiResponse<Integer>> getCartItemCount(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            int count = cartService.getCartItemCount(user.getId());
            
            return ResponseEntity.ok(ApiResponse.success(count, "Cart item count retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to get cart item count: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get cart item count"));
        }
    }
    
    @GetMapping("/total-quantity")
    @Operation(summary = "Get cart total quantity", description = "Get the total quantity of all items in the cart")
    public ResponseEntity<ApiResponse<Integer>> getCartTotalQuantity(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            int totalQuantity = cartService.getCartTotalQuantity(user.getId());
            
            return ResponseEntity.ok(ApiResponse.success(totalQuantity, "Cart total quantity retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to get cart total quantity: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get cart total quantity"));
        }
    }
    
    @GetMapping("/check/{productId}")
    @Operation(summary = "Check if product in cart", description = "Check if a specific product is in the cart")
    public ResponseEntity<ApiResponse<Boolean>> isProductInCart(
            @PathVariable String productId,
            Authentication authentication) {
        
        try {
            User user = userService.getUserByEmail(authentication.getName());
            boolean inCart = cartService.isProductInCart(user.getId(), productId);
            
            return ResponseEntity.ok(ApiResponse.success(inCart, "Product cart status retrieved successfully"));
            
        } catch (Exception e) {
            log.error("Failed to check if product {} is in cart: {}", productId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to check product cart status"));
        }
    }
    
    // Request DTOs
    public static class AddToCartRequest {
        @NotBlank(message = "Product ID is required")
        private String productId;
        
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
        
        private Map<String, String> selectedVariants;
        
        // Getters and setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public Map<String, String> getSelectedVariants() { return selectedVariants; }
        public void setSelectedVariants(Map<String, String> selectedVariants) { this.selectedVariants = selectedVariants; }
    }
    
    public static class UpdateCartItemRequest {
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
        
        private Map<String, String> selectedVariants;
        
        // Getters and setters
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public Map<String, String> getSelectedVariants() { return selectedVariants; }
        public void setSelectedVariants(Map<String, String> selectedVariants) { this.selectedVariants = selectedVariants; }
    }
}
