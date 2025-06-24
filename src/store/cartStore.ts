import { create } from 'zustand';
import { persist, subscribeWithSelector } from 'zustand/middleware';
import { CartItem, Product } from '../types';
import { cartService } from '../services/cartService';
import toast from 'react-hot-toast';

interface CartSummary {
  itemCount: number;
  subtotal: number;
  tax: number;
  shipping: number;
  discount: number;
  total: number;
  estimatedDelivery: string;
}

interface CartState {
  // State
  items: CartItem[];
  isOpen: boolean;
  isLoading: boolean;
  summary: CartSummary | null;
  appliedCoupon: { code: string; discount: number } | null;

  // Actions
  addItem: (product: Product, quantity?: number, selectedVariants?: Record<string, string>) => Promise<void>;
  removeItem: (itemId: string) => Promise<void>;
  updateQuantity: (itemId: string, quantity: number) => Promise<void>;
  clearCart: () => Promise<void>;
  toggleCart: () => void;
  loadCart: () => Promise<void>;
  applyCoupon: (couponCode: string) => Promise<void>;
  removeCoupon: () => Promise<void>;
  moveToWishlist: (itemId: string) => Promise<void>;
  validateCart: () => Promise<boolean>;

  // Getters
  getTotalItems: () => number;
  getTotalPrice: () => number;
  getItemById: (itemId: string) => CartItem | undefined;
  hasProduct: (productId: string) => boolean;
}

export const useCartStore = create<CartState>()(
  subscribeWithSelector(
    persist(
      (set, get) => ({
        // Initial state
        items: [],
        isOpen: false,
        isLoading: false,
        summary: null,
        appliedCoupon: null,

        // Add item to cart
        addItem: async (product: Product, quantity = 1, selectedVariants) => {
          set({ isLoading: true });
          try {
            // Try API first, fallback to local storage
            try {
              const response = await cartService.addToCart(product.id, quantity, selectedVariants);
              if (response.success) {
                await get().loadCart();
                toast.success(`${product.name} added to cart`);
                return;
              }
            } catch (error) {
              console.warn('API not available, using local storage');
            }

            // Fallback to local storage
            const currentItems = get().items;
            const existingItem = currentItems.find(item => item.product.id === product.id);
            
            if (existingItem) {
              const updatedItems = currentItems.map(item =>
                item.product.id === product.id
                  ? { ...item, quantity: item.quantity + quantity }
                  : item
              );
              set({ items: updatedItems });
            } else {
              const newItem: CartItem = {
                id: `${product.id}-${Date.now()}`,
                product,
                quantity,
                selectedVariants: selectedVariants || {},
                addedAt: new Date().toISOString()
              };
              set({ items: [...currentItems, newItem] });
            }
            
            toast.success(`${product.name} added to cart`);
          } catch (error) {
            console.error('Failed to add item to cart:', error);
            toast.error('Failed to add item to cart');
          } finally {
            set({ isLoading: false });
          }
        },

        // Remove item from cart
        removeItem: async (itemId: string) => {
          set({ isLoading: true });
          try {
            // Try API first, fallback to local storage
            try {
              const response = await cartService.removeFromCart(itemId);
              if (response.success) {
                await get().loadCart();
                toast.success('Item removed from cart');
                return;
              }
            } catch (error) {
              console.warn('API not available, using local storage');
            }

            // Fallback to local storage
            const currentItems = get().items;
            const updatedItems = currentItems.filter(item => item.id !== itemId);
            set({ items: updatedItems });
            toast.success('Item removed from cart');
          } catch (error) {
            console.error('Failed to remove item from cart:', error);
            toast.error('Failed to remove item from cart');
          } finally {
            set({ isLoading: false });
          }
        },

        // Update item quantity
        updateQuantity: async (itemId: string, quantity: number) => {
          if (quantity <= 0) {
            await get().removeItem(itemId);
            return;
          }

          set({ isLoading: true });
          try {
            // Try API first, fallback to local storage
            try {
              const response = await cartService.updateCartItem(itemId, quantity);
              if (response.success) {
                await get().loadCart();
                return;
              }
            } catch (error) {
              console.warn('API not available, using local storage');
            }

            // Fallback to local storage
            const currentItems = get().items;
            const updatedItems = currentItems.map(item =>
              item.id === itemId ? { ...item, quantity } : item
            );
            set({ items: updatedItems });
          } catch (error) {
            console.error('Failed to update item quantity:', error);
            toast.error('Failed to update quantity');
          } finally {
            set({ isLoading: false });
          }
        },

        // Clear entire cart
        clearCart: async () => {
          set({ isLoading: true });
          try {
            // Try API first, fallback to local storage
            try {
              const response = await cartService.clearCart();
              if (response.success) {
                set({ items: [], summary: null, appliedCoupon: null });
                toast.success('Cart cleared');
                return;
              }
            } catch (error) {
              console.warn('API not available, using local storage');
            }

            // Fallback to local storage
            set({ items: [], summary: null, appliedCoupon: null });
            toast.success('Cart cleared');
          } catch (error) {
            console.error('Failed to clear cart:', error);
            toast.error('Failed to clear cart');
          } finally {
            set({ isLoading: false });
          }
        },

        // Toggle cart sidebar
        toggleCart: () => {
          set({ isOpen: !get().isOpen });
        },

        // Load cart from server
        loadCart: async () => {
          try {
            // Try API first, fallback to local storage
            try {
              const [cartResponse, summaryResponse] = await Promise.all([
                cartService.getCart(),
                cartService.getCartSummary()
              ]);

              if (cartResponse.success && summaryResponse.success) {
                set({
                  items: cartResponse.data,
                  summary: summaryResponse.data
                });
                return;
              }
            } catch (error) {
              console.warn('API not available, using local storage');
            }

            // Fallback to local storage - summary will be calculated from items
            const currentItems = get().items;
            const summary: CartSummary = {
              itemCount: currentItems.reduce((total, item) => total + item.quantity, 0),
              subtotal: currentItems.reduce((total, item) => total + (item.product.price * item.quantity), 0),
              tax: 0,
              shipping: 0,
              discount: 0,
              total: currentItems.reduce((total, item) => total + (item.product.price * item.quantity), 0),
              estimatedDelivery: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString()
            };
            set({ summary });
          } catch (error) {
            console.error('Failed to load cart:', error);
          }
        },

        // Apply coupon code
        applyCoupon: async (couponCode: string) => {
          set({ isLoading: true });
          try {
            const response = await cartService.applyCoupon(couponCode);
            if (response.success) {
              set({
                appliedCoupon: {
                  code: couponCode,
                  discount: response.data.discount
                }
              });
              await get().loadCart();
              toast.success(response.data.message || 'Coupon applied successfully');
            }
          } catch (error: any) {
            console.error('Failed to apply coupon:', error);
            toast.error(error.response?.data?.message || 'Invalid coupon code');
          } finally {
            set({ isLoading: false });
          }
        },

        // Remove applied coupon
        removeCoupon: async () => {
          set({ isLoading: true });
          try {
            const response = await cartService.removeCoupon();
            if (response.success) {
              set({ appliedCoupon: null });
              await get().loadCart();
              toast.success('Coupon removed');
            }
          } catch (error) {
            console.error('Failed to remove coupon:', error);
            toast.error('Failed to remove coupon');
          } finally {
            set({ isLoading: false });
          }
        },

        // Move item to wishlist
        moveToWishlist: async (itemId: string) => {
          set({ isLoading: true });
          try {
            const response = await cartService.moveToWishlist(itemId);
            if (response.success) {
              await get().loadCart();
              toast.success('Item moved to wishlist');
            }
          } catch (error) {
            console.error('Failed to move item to wishlist:', error);
            toast.error('Failed to move item to wishlist');
          } finally {
            set({ isLoading: false });
          }
        },

        // Validate cart
        validateCart: async () => {
          try {
            const response = await cartService.validateCart();
            return response.success;
          } catch (error) {
            console.error('Failed to validate cart:', error);
            return false;
          }
        },

        // Getters
        getTotalItems: () => {
          return get().items.reduce((total, item) => total + item.quantity, 0);
        },

        getTotalPrice: () => {
          return get().items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
        },

        getItemById: (itemId: string) => {
          return get().items.find(item => item.id === itemId);
        },

        hasProduct: (productId: string) => {
          return get().items.some(item => item.product.id === productId);
        },
      }),
      {
        name: 'cart-storage',
        partialize: (state) => ({
          items: state.items,
          appliedCoupon: state.appliedCoupon,
        }),
      }
    )
  )
);