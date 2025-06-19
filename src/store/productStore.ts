import { create } from 'zustand';
import { subscribeWithSelector } from 'zustand/middleware';
import { 
  Product, 
  Category, 
  FilterOptions, 
  PaginatedResponse,
  ProductReview 
} from '../types';
import { productService } from '../services/productService';
import toast from 'react-hot-toast';

interface ProductState {
  // Products
  products: Product[];
  featuredProducts: Product[];
  currentProduct: Product | null;
  recommendations: Product[];
  
  // Categories
  categories: Category[];
  currentCategory: Category | null;
  
  // Reviews
  reviews: ProductReview[];
  
  // Filters and Search
  filters: FilterOptions;
  searchQuery: string;
  searchResults: Product[];
  
  // Pagination
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
    hasNext: boolean;
    hasPrev: boolean;
  };
  
  // Loading states
  isLoading: boolean;
  isLoadingProduct: boolean;
  isLoadingReviews: boolean;
  
  // Actions
  loadProducts: (filters?: FilterOptions) => Promise<void>;
  loadFeaturedProducts: () => Promise<void>;
  loadProduct: (id: string) => Promise<void>;
  loadProductReviews: (productId: string, page?: number) => Promise<void>;
  loadCategories: () => Promise<void>;
  loadCategory: (slug: string) => Promise<void>;
  searchProducts: (query: string, filters?: FilterOptions) => Promise<void>;
  setFilters: (filters: Partial<FilterOptions>) => void;
  clearFilters: () => void;
  setCurrentProduct: (product: Product | null) => void;
  addReview: (productId: string, review: Omit<ProductReview, 'id' | 'productId' | 'userId' | 'userName' | 'userAvatar' | 'helpful' | 'createdAt'>) => Promise<void>;
}

const initialFilters: FilterOptions = {
  page: 1,
  limit: 12,
  sortBy: 'relevance',
};

const initialPagination = {
  page: 1,
  limit: 12,
  total: 0,
  totalPages: 0,
  hasNext: false,
  hasPrev: false,
};

export const useProductStore = create<ProductState>()(
  subscribeWithSelector((set, get) => ({
    // Initial state
    products: [],
    featuredProducts: [],
    currentProduct: null,
    recommendations: [],
    categories: [],
    currentCategory: null,
    reviews: [],
    filters: initialFilters,
    searchQuery: '',
    searchResults: [],
    pagination: initialPagination,
    isLoading: false,
    isLoadingProduct: false,
    isLoadingReviews: false,

    // Load products with filters
    loadProducts: async (filters?: FilterOptions) => {
      set({ isLoading: true });
      try {
        const currentFilters = filters || get().filters;
        const response = await productService.getProducts(currentFilters);
        
        if (response.success) {
          set({
            products: response.data.items,
            pagination: {
              page: response.data.page,
              limit: response.data.limit,
              total: response.data.total,
              totalPages: response.data.totalPages,
              hasNext: response.data.hasNext,
              hasPrev: response.data.hasPrev,
            },
            filters: currentFilters,
          });
        }
      } catch (error) {
        console.error('Failed to load products:', error);
        toast.error('Failed to load products');
      } finally {
        set({ isLoading: false });
      }
    },

    // Load featured products
    loadFeaturedProducts: async () => {
      try {
        const response = await productService.getFeaturedProducts(8);
        if (response.success) {
          set({ featuredProducts: response.data });
        }
      } catch (error) {
        console.error('Failed to load featured products:', error);
      }
    },

    // Load single product
    loadProduct: async (id: string) => {
      set({ isLoadingProduct: true });
      try {
        const [productResponse, recommendationsResponse] = await Promise.all([
          productService.getProduct(id),
          productService.getRecommendations(id, 4)
        ]);

        if (productResponse.success) {
          set({ currentProduct: productResponse.data });
        }

        if (recommendationsResponse.success) {
          set({ recommendations: recommendationsResponse.data });
        }
      } catch (error) {
        console.error('Failed to load product:', error);
        toast.error('Failed to load product');
      } finally {
        set({ isLoadingProduct: false });
      }
    },

    // Load product reviews
    loadProductReviews: async (productId: string, page = 1) => {
      set({ isLoadingReviews: true });
      try {
        const response = await productService.getProductReviews(productId, page, 10);
        if (response.success) {
          set({ reviews: response.data.items });
        }
      } catch (error) {
        console.error('Failed to load reviews:', error);
        toast.error('Failed to load reviews');
      } finally {
        set({ isLoadingReviews: false });
      }
    },

    // Load categories
    loadCategories: async () => {
      try {
        const response = await productService.getCategories();
        if (response.success) {
          set({ categories: response.data });
        }
      } catch (error) {
        console.error('Failed to load categories:', error);
      }
    },

    // Load single category
    loadCategory: async (slug: string) => {
      try {
        const response = await productService.getCategory(slug);
        if (response.success) {
          set({ currentCategory: response.data });
        }
      } catch (error) {
        console.error('Failed to load category:', error);
        toast.error('Category not found');
      }
    },

    // Search products
    searchProducts: async (query: string, filters?: FilterOptions) => {
      set({ isLoading: true, searchQuery: query });
      try {
        const searchFilters = { ...get().filters, ...filters };
        const response = await productService.searchProducts(query, searchFilters);
        
        if (response.success) {
          set({
            searchResults: response.data.items,
            products: response.data.items,
            pagination: {
              page: response.data.page,
              limit: response.data.limit,
              total: response.data.total,
              totalPages: response.data.totalPages,
              hasNext: response.data.hasNext,
              hasPrev: response.data.hasPrev,
            },
          });
        }
      } catch (error) {
        console.error('Failed to search products:', error);
        toast.error('Search failed');
      } finally {
        set({ isLoading: false });
      }
    },

    // Set filters
    setFilters: (newFilters: Partial<FilterOptions>) => {
      const updatedFilters = { ...get().filters, ...newFilters };
      set({ filters: updatedFilters });
      get().loadProducts(updatedFilters);
    },

    // Clear filters
    clearFilters: () => {
      set({ filters: initialFilters });
      get().loadProducts(initialFilters);
    },

    // Set current product
    setCurrentProduct: (product: Product | null) => {
      set({ currentProduct: product });
    },

    // Add product review
    addReview: async (productId: string, reviewData) => {
      try {
        const response = await productService.addProductReview(productId, reviewData);
        if (response.success) {
          // Reload reviews to show the new one
          await get().loadProductReviews(productId);
          toast.success('Review added successfully');
        }
      } catch (error) {
        console.error('Failed to add review:', error);
        toast.error('Failed to add review');
      }
    },
  }))
);
