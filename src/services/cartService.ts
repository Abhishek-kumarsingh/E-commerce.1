import { apiClient } from '../lib/api';
import { CartItem, ApiResponse } from '../types';

export const cartService = {
  // Get user's cart
  getCart: async (): Promise<ApiResponse<CartItem[]>> => {
    return apiClient.get('/cart');
  },

  // Add item to cart
  addToCart: async (
    productId: string, 
    quantity: number = 1,
    selectedVariants?: Record<string, string>
  ): Promise<ApiResponse<CartItem>> => {
    return apiClient.post('/cart/items', {
      productId,
      quantity,
      selectedVariants,
    });
  },

  // Update cart item quantity
  updateCartItem: async (
    itemId: string, 
    quantity: number
  ): Promise<ApiResponse<CartItem>> => {
    return apiClient.put(`/cart/items/${itemId}`, { quantity });
  },

  // Remove item from cart
  removeFromCart: async (itemId: string): Promise<ApiResponse<void>> => {
    return apiClient.delete(`/cart/items/${itemId}`);
  },

  // Clear entire cart
  clearCart: async (): Promise<ApiResponse<void>> => {
    return apiClient.delete('/cart');
  },

  // Get cart summary
  getCartSummary: async (): Promise<ApiResponse<{
    itemCount: number;
    subtotal: number;
    tax: number;
    shipping: number;
    discount: number;
    total: number;
    estimatedDelivery: string;
  }>> => {
    return apiClient.get('/cart/summary');
  },

  // Apply coupon code
  applyCoupon: async (couponCode: string): Promise<ApiResponse<{
    discount: number;
    couponId: string;
    message: string;
  }>> => {
    return apiClient.post('/cart/coupon', { couponCode });
  },

  // Remove coupon
  removeCoupon: async (): Promise<ApiResponse<void>> => {
    return apiClient.delete('/cart/coupon');
  },

  // Save cart for later (for guest users)
  saveCart: async (cartItems: Omit<CartItem, 'id' | 'addedAt'>[]): Promise<ApiResponse<{ cartId: string }>> => {
    return apiClient.post('/cart/save', { items: cartItems });
  },

  // Restore saved cart
  restoreCart: async (cartId: string): Promise<ApiResponse<CartItem[]>> => {
    return apiClient.post(`/cart/restore/${cartId}`);
  },

  // Move item to wishlist
  moveToWishlist: async (itemId: string): Promise<ApiResponse<void>> => {
    return apiClient.post(`/cart/items/${itemId}/move-to-wishlist`);
  },

  // Get shipping options
  getShippingOptions: async (zipCode?: string): Promise<ApiResponse<Array<{
    id: string;
    name: string;
    description: string;
    price: number;
    estimatedDays: number;
    available: boolean;
  }>>> => {
    const params = zipCode ? `?zipCode=${zipCode}` : '';
    return apiClient.get(`/cart/shipping-options${params}`);
  },

  // Calculate shipping
  calculateShipping: async (
    shippingOptionId: string,
    address: {
      zipCode: string;
      city: string;
      state: string;
      country: string;
    }
  ): Promise<ApiResponse<{
    cost: number;
    estimatedDelivery: string;
    available: boolean;
  }>> => {
    return apiClient.post('/cart/calculate-shipping', {
      shippingOptionId,
      address,
    });
  },

  // Validate cart before checkout
  validateCart: async (): Promise<ApiResponse<{
    valid: boolean;
    issues: Array<{
      itemId: string;
      productName: string;
      issue: 'out_of_stock' | 'price_changed' | 'unavailable';
      currentPrice?: number;
      availableQuantity?: number;
    }>;
  }>> => {
    return apiClient.post('/cart/validate');
  },

  // Merge guest cart with user cart
  mergeCart: async (guestCartItems: Omit<CartItem, 'id' | 'addedAt'>[]): Promise<ApiResponse<CartItem[]>> => {
    return apiClient.post('/cart/merge', { guestItems: guestCartItems });
  },
};
