import { apiClient } from '../lib/api';
import { User, Address, UserPreferences, ApiResponse, PaginatedResponse } from '../types';

export const userService = {
  // Get current user profile
  getProfile: async (): Promise<ApiResponse<User>> => {
    return apiClient.get('/users/profile');
  },

  // Update user profile
  updateProfile: async (updates: Partial<User>): Promise<ApiResponse<User>> => {
    return apiClient.put('/users/profile', updates);
  },

  // Update user preferences
  updatePreferences: async (preferences: Partial<UserPreferences>): Promise<ApiResponse<UserPreferences>> => {
    return apiClient.put('/users/preferences', preferences);
  },

  // Upload profile avatar
  uploadAvatar: async (file: File): Promise<ApiResponse<{ avatarUrl: string }>> => {
    return apiClient.upload('/users/avatar', file);
  },

  // Delete user account
  deleteAccount: async (password: string): Promise<ApiResponse<void>> => {
    return apiClient.delete('/users/account', { data: { password } });
  },

  // Address management
  addresses: {
    // Get all addresses
    getAddresses: async (): Promise<ApiResponse<Address[]>> => {
      return apiClient.get('/users/addresses');
    },

    // Add new address
    addAddress: async (address: Omit<Address, 'id'>): Promise<ApiResponse<Address>> => {
      return apiClient.post('/users/addresses', address);
    },

    // Update address
    updateAddress: async (id: string, address: Partial<Address>): Promise<ApiResponse<Address>> => {
      return apiClient.put(`/users/addresses/${id}`, address);
    },

    // Delete address
    deleteAddress: async (id: string): Promise<ApiResponse<void>> => {
      return apiClient.delete(`/users/addresses/${id}`);
    },

    // Set default address
    setDefaultAddress: async (id: string): Promise<ApiResponse<void>> => {
      return apiClient.post(`/users/addresses/${id}/set-default`);
    },

    // Validate address
    validateAddress: async (address: Omit<Address, 'id' | 'isDefault'>): Promise<ApiResponse<{
      valid: boolean;
      suggestions?: Address[];
      issues?: string[];
    }>> => {
      return apiClient.post('/users/addresses/validate', address);
    },
  },

  // Wishlist management
  wishlist: {
    // Get wishlist
    getWishlist: async (page = 1, limit = 20): Promise<ApiResponse<PaginatedResponse<{
      id: string;
      productId: string;
      product: any; // Product type
      addedAt: string;
    }>>> => {
      return apiClient.get(`/users/wishlist?page=${page}&limit=${limit}`);
    },

    // Add to wishlist
    addToWishlist: async (productId: string): Promise<ApiResponse<void>> => {
      return apiClient.post('/users/wishlist', { productId });
    },

    // Remove from wishlist
    removeFromWishlist: async (productId: string): Promise<ApiResponse<void>> => {
      return apiClient.delete(`/users/wishlist/${productId}`);
    },

    // Check if product is in wishlist
    isInWishlist: async (productId: string): Promise<ApiResponse<{ inWishlist: boolean }>> => {
      return apiClient.get(`/users/wishlist/check/${productId}`);
    },

    // Move wishlist item to cart
    moveToCart: async (productId: string, quantity = 1): Promise<ApiResponse<void>> => {
      return apiClient.post(`/users/wishlist/${productId}/move-to-cart`, { quantity });
    },

    // Clear wishlist
    clearWishlist: async (): Promise<ApiResponse<void>> => {
      return apiClient.delete('/users/wishlist');
    },
  },

  // Notification preferences
  notifications: {
    // Get notification settings
    getSettings: async (): Promise<ApiResponse<{
      email: boolean;
      sms: boolean;
      push: boolean;
      orderUpdates: boolean;
      promotions: boolean;
      newsletter: boolean;
    }>> => {
      return apiClient.get('/users/notifications/settings');
    },

    // Update notification settings
    updateSettings: async (settings: Record<string, boolean>): Promise<ApiResponse<void>> => {
      return apiClient.put('/users/notifications/settings', settings);
    },

    // Get notifications
    getNotifications: async (page = 1, limit = 20): Promise<ApiResponse<PaginatedResponse<{
      id: string;
      title: string;
      message: string;
      type: 'order' | 'promotion' | 'system';
      read: boolean;
      createdAt: string;
    }>>> => {
      return apiClient.get(`/users/notifications?page=${page}&limit=${limit}`);
    },

    // Mark notification as read
    markAsRead: async (notificationId: string): Promise<ApiResponse<void>> => {
      return apiClient.post(`/users/notifications/${notificationId}/read`);
    },

    // Mark all notifications as read
    markAllAsRead: async (): Promise<ApiResponse<void>> => {
      return apiClient.post('/users/notifications/read-all');
    },

    // Delete notification
    deleteNotification: async (notificationId: string): Promise<ApiResponse<void>> => {
      return apiClient.delete(`/users/notifications/${notificationId}`);
    },
  },

  // Admin functions
  admin: {
    // Get all users
    getAllUsers: async (
      page = 1, 
      limit = 20, 
      search?: string,
      role?: 'user' | 'admin'
    ): Promise<ApiResponse<PaginatedResponse<User>>> => {
      const params = new URLSearchParams({ 
        page: page.toString(), 
        limit: limit.toString() 
      });
      
      if (search) params.append('search', search);
      if (role) params.append('role', role);

      return apiClient.get(`/admin/users?${params.toString()}`);
    },

    // Get user by ID
    getUser: async (id: string): Promise<ApiResponse<User>> => {
      return apiClient.get(`/admin/users/${id}`);
    },

    // Update user
    updateUser: async (id: string, updates: Partial<User>): Promise<ApiResponse<User>> => {
      return apiClient.put(`/admin/users/${id}`, updates);
    },

    // Deactivate user
    deactivateUser: async (id: string, reason?: string): Promise<ApiResponse<void>> => {
      return apiClient.post(`/admin/users/${id}/deactivate`, { reason });
    },

    // Activate user
    activateUser: async (id: string): Promise<ApiResponse<void>> => {
      return apiClient.post(`/admin/users/${id}/activate`);
    },

    // Get user analytics
    getUserAnalytics: async (): Promise<ApiResponse<{
      totalUsers: number;
      activeUsers: number;
      newUsersThisMonth: number;
      usersByRole: Record<string, number>;
      userGrowth: Array<{ date: string; count: number }>;
    }>> => {
      return apiClient.get('/admin/users/analytics');
    },
  },
};
