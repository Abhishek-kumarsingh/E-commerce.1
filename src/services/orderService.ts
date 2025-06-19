import { apiClient } from '../lib/api';
import { 
  Order, 
  OrderItem, 
  OrderStatus, 
  PaymentMethod,
  CheckoutForm,
  PaginatedResponse,
  ApiResponse 
} from '../types';

export const orderService = {
  // Create new order
  createOrder: async (orderData: CheckoutForm & { items: OrderItem[] }): Promise<ApiResponse<Order>> => {
    return apiClient.post('/orders', orderData);
  },

  // Get user orders
  getUserOrders: async (page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<Order>>> => {
    return apiClient.get(`/orders?page=${page}&limit=${limit}`);
  },

  // Get single order
  getOrder: async (id: string): Promise<ApiResponse<Order>> => {
    return apiClient.get(`/orders/${id}`);
  },

  // Get order by order number
  getOrderByNumber: async (orderNumber: string): Promise<ApiResponse<Order>> => {
    return apiClient.get(`/orders/number/${orderNumber}`);
  },

  // Cancel order
  cancelOrder: async (id: string, reason?: string): Promise<ApiResponse<Order>> => {
    return apiClient.post(`/orders/${id}/cancel`, { reason });
  },

  // Track order
  trackOrder: async (orderNumber: string): Promise<ApiResponse<{
    order: Order;
    tracking: {
      status: string;
      location: string;
      timestamp: string;
      description: string;
    }[];
  }>> => {
    return apiClient.get(`/orders/track/${orderNumber}`);
  },

  // Request return/refund
  requestReturn: async (
    orderId: string, 
    items: Array<{ itemId: string; quantity: number; reason: string }>
  ): Promise<ApiResponse<void>> => {
    return apiClient.post(`/orders/${orderId}/return`, { items });
  },

  // Get order invoice
  getInvoice: async (orderId: string): Promise<ApiResponse<{ url: string }>> => {
    return apiClient.get(`/orders/${orderId}/invoice`);
  },

  // Reorder items
  reorder: async (orderId: string): Promise<ApiResponse<{ cartItems: number }>> => {
    return apiClient.post(`/orders/${orderId}/reorder`);
  },

  // Payment methods
  payment: {
    // Get saved payment methods
    getPaymentMethods: async (): Promise<ApiResponse<PaymentMethod[]>> => {
      return apiClient.get('/payment-methods');
    },

    // Add payment method
    addPaymentMethod: async (paymentMethod: Omit<PaymentMethod, 'id'>): Promise<ApiResponse<PaymentMethod>> => {
      return apiClient.post('/payment-methods', paymentMethod);
    },

    // Update payment method
    updatePaymentMethod: async (id: string, updates: Partial<PaymentMethod>): Promise<ApiResponse<PaymentMethod>> => {
      return apiClient.put(`/payment-methods/${id}`, updates);
    },

    // Delete payment method
    deletePaymentMethod: async (id: string): Promise<ApiResponse<void>> => {
      return apiClient.delete(`/payment-methods/${id}`);
    },

    // Set default payment method
    setDefaultPaymentMethod: async (id: string): Promise<ApiResponse<void>> => {
      return apiClient.post(`/payment-methods/${id}/set-default`);
    },

    // Process payment
    processPayment: async (paymentData: {
      orderId: string;
      paymentMethodId: string;
      amount: number;
      currency: string;
    }): Promise<ApiResponse<{ 
      success: boolean; 
      transactionId: string; 
      redirectUrl?: string;
    }>> => {
      return apiClient.post('/payments/process', paymentData);
    },

    // Verify payment
    verifyPayment: async (transactionId: string): Promise<ApiResponse<{
      status: 'success' | 'failed' | 'pending';
      orderId: string;
    }>> => {
      return apiClient.get(`/payments/verify/${transactionId}`);
    },

    // Refund payment
    refundPayment: async (orderId: string, amount?: number, reason?: string): Promise<ApiResponse<{
      refundId: string;
      amount: number;
      status: string;
    }>> => {
      return apiClient.post(`/payments/refund`, { orderId, amount, reason });
    },
  },

  // Admin functions
  admin: {
    // Get all orders
    getAllOrders: async (
      page = 1, 
      limit = 20, 
      status?: OrderStatus,
      search?: string
    ): Promise<ApiResponse<PaginatedResponse<Order>>> => {
      const params = new URLSearchParams({ 
        page: page.toString(), 
        limit: limit.toString() 
      });
      
      if (status) params.append('status', status);
      if (search) params.append('search', search);

      return apiClient.get(`/admin/orders?${params.toString()}`);
    },

    // Update order status
    updateOrderStatus: async (
      id: string, 
      status: OrderStatus, 
      notes?: string,
      trackingNumber?: string
    ): Promise<ApiResponse<Order>> => {
      return apiClient.patch(`/admin/orders/${id}/status`, { 
        status, 
        notes, 
        trackingNumber 
      });
    },

    // Get order analytics
    getOrderAnalytics: async (
      startDate?: string, 
      endDate?: string
    ): Promise<ApiResponse<{
      totalOrders: number;
      totalRevenue: number;
      averageOrderValue: number;
      ordersByStatus: Record<OrderStatus, number>;
      revenueByDay: Array<{ date: string; revenue: number }>;
      topProducts: Array<{ productId: string; productName: string; quantity: number; revenue: number }>;
    }>> => {
      const params = new URLSearchParams();
      if (startDate) params.append('startDate', startDate);
      if (endDate) params.append('endDate', endDate);

      return apiClient.get(`/admin/orders/analytics?${params.toString()}`);
    },

    // Export orders
    exportOrders: async (
      format: 'csv' | 'excel',
      filters?: {
        startDate?: string;
        endDate?: string;
        status?: OrderStatus;
      }
    ): Promise<ApiResponse<{ downloadUrl: string }>> => {
      return apiClient.post('/admin/orders/export', { format, filters });
    },

    // Bulk update orders
    bulkUpdateOrders: async (
      orderIds: string[],
      updates: { status?: OrderStatus; trackingNumber?: string; notes?: string }
    ): Promise<ApiResponse<void>> => {
      return apiClient.post('/admin/orders/bulk-update', { orderIds, updates });
    },
  },
};
