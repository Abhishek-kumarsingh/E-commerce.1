import { apiClient } from '../lib/api';
import { 
  Product, 
  ProductReview, 
  Category, 
  FilterOptions, 
  PaginatedResponse,
  ApiResponse 
} from '../types';

export const productService = {
  // Get all products with filters and pagination
  getProducts: async (filters?: FilterOptions): Promise<ApiResponse<PaginatedResponse<Product>>> => {
    const params = new URLSearchParams();
    
    if (filters) {
      if (filters.category) params.append('category', filters.category);
      if (filters.subcategory) params.append('subcategory', filters.subcategory);
      if (filters.priceRange) {
        params.append('minPrice', filters.priceRange[0].toString());
        params.append('maxPrice', filters.priceRange[1].toString());
      }
      if (filters.rating) params.append('rating', filters.rating.toString());
      if (filters.brand) filters.brand.forEach(b => params.append('brand', b));
      if (filters.inStock !== undefined) params.append('inStock', filters.inStock.toString());
      if (filters.featured !== undefined) params.append('featured', filters.featured.toString());
      if (filters.tags) filters.tags.forEach(t => params.append('tags', t));
      if (filters.sortBy) params.append('sortBy', filters.sortBy);
      if (filters.page) params.append('page', filters.page.toString());
      if (filters.limit) params.append('limit', filters.limit.toString());
    }

    return apiClient.get(`/products?${params.toString()}`);
  },

  // Get single product by ID
  getProduct: async (id: string): Promise<ApiResponse<Product>> => {
    return apiClient.get(`/products/${id}`);
  },

  // Get featured products
  getFeaturedProducts: async (limit = 8): Promise<ApiResponse<Product[]>> => {
    return apiClient.get(`/products/featured?limit=${limit}`);
  },

  // Search products
  searchProducts: async (
    query: string, 
    filters?: FilterOptions
  ): Promise<ApiResponse<PaginatedResponse<Product>>> => {
    const params = new URLSearchParams({ q: query });
    
    if (filters) {
      if (filters.category) params.append('category', filters.category);
      if (filters.sortBy) params.append('sortBy', filters.sortBy);
      if (filters.page) params.append('page', filters.page.toString());
      if (filters.limit) params.append('limit', filters.limit.toString());
    }

    return apiClient.get(`/products/search?${params.toString()}`);
  },

  // Get product recommendations
  getRecommendations: async (productId: string, limit = 4): Promise<ApiResponse<Product[]>> => {
    return apiClient.get(`/products/${productId}/recommendations?limit=${limit}`);
  },

  // Get products by category
  getProductsByCategory: async (
    categorySlug: string, 
    filters?: FilterOptions
  ): Promise<ApiResponse<PaginatedResponse<Product>>> => {
    const params = new URLSearchParams();
    
    if (filters) {
      if (filters.sortBy) params.append('sortBy', filters.sortBy);
      if (filters.page) params.append('page', filters.page.toString());
      if (filters.limit) params.append('limit', filters.limit.toString());
    }

    return apiClient.get(`/categories/${categorySlug}/products?${params.toString()}`);
  },

  // Get product reviews
  getProductReviews: async (
    productId: string, 
    page = 1, 
    limit = 10
  ): Promise<ApiResponse<PaginatedResponse<ProductReview>>> => {
    return apiClient.get(`/products/${productId}/reviews?page=${page}&limit=${limit}`);
  },

  // Add product review
  addProductReview: async (
    productId: string, 
    review: Omit<ProductReview, 'id' | 'productId' | 'userId' | 'userName' | 'userAvatar' | 'helpful' | 'createdAt'>
  ): Promise<ApiResponse<ProductReview>> => {
    return apiClient.post(`/products/${productId}/reviews`, review);
  },

  // Mark review as helpful
  markReviewHelpful: async (reviewId: string): Promise<ApiResponse<void>> => {
    return apiClient.post(`/reviews/${reviewId}/helpful`);
  },

  // Get all categories
  getCategories: async (): Promise<ApiResponse<Category[]>> => {
    return apiClient.get('/categories');
  },

  // Get category by slug
  getCategory: async (slug: string): Promise<ApiResponse<Category>> => {
    return apiClient.get(`/categories/${slug}`);
  },

  // Get brands
  getBrands: async (): Promise<ApiResponse<string[]>> => {
    return apiClient.get('/products/brands');
  },

  // Get price range
  getPriceRange: async (category?: string): Promise<ApiResponse<{ min: number; max: number }>> => {
    const params = category ? `?category=${category}` : '';
    return apiClient.get(`/products/price-range${params}`);
  },

  // Admin functions
  admin: {
    // Create product
    createProduct: async (product: Omit<Product, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<Product>> => {
      return apiClient.post('/admin/products', product);
    },

    // Update product
    updateProduct: async (id: string, product: Partial<Product>): Promise<ApiResponse<Product>> => {
      return apiClient.put(`/admin/products/${id}`, product);
    },

    // Delete product
    deleteProduct: async (id: string): Promise<ApiResponse<void>> => {
      return apiClient.delete(`/admin/products/${id}`);
    },

    // Update stock
    updateStock: async (id: string, quantity: number): Promise<ApiResponse<void>> => {
      return apiClient.patch(`/admin/products/${id}/stock`, { quantity });
    },

    // Bulk update products
    bulkUpdateProducts: async (updates: Array<{ id: string; data: Partial<Product> }>): Promise<ApiResponse<void>> => {
      return apiClient.post('/admin/products/bulk-update', { updates });
    },

    // Create category
    createCategory: async (category: Omit<Category, 'id' | 'productCount'>): Promise<ApiResponse<Category>> => {
      return apiClient.post('/admin/categories', category);
    },

    // Update category
    updateCategory: async (id: string, category: Partial<Category>): Promise<ApiResponse<Category>> => {
      return apiClient.put(`/admin/categories/${id}`, category);
    },

    // Delete category
    deleteCategory: async (id: string): Promise<ApiResponse<void>> => {
      return apiClient.delete(`/admin/categories/${id}`);
    },
  },
};
