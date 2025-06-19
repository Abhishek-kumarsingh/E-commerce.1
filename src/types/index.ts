// Product related types
export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  originalPrice?: number;
  image: string;
  images: string[];
  category: string;
  subcategory?: string;
  rating: number;
  reviewCount: number;
  inStock: boolean;
  stockQuantity: number;
  featured: boolean;
  tags: string[];
  brand: string;
  specifications: Record<string, string>;
  variants?: ProductVariant[];
  createdAt: string;
  updatedAt: string;
}

export interface ProductVariant {
  id: string;
  name: string;
  value: string;
  priceModifier: number;
  stockQuantity: number;
}

export interface ProductReview {
  id: string;
  productId: string;
  userId: string;
  userName: string;
  userAvatar?: string;
  rating: number;
  title: string;
  comment: string;
  helpful: number;
  verified: boolean;
  createdAt: string;
}

// User related types
export interface User {
  id: string;
  email: string;
  name: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  phone?: string;
  dateOfBirth?: string;
  isAdmin: boolean;
  isVerified: boolean;
  addresses: Address[];
  wishlist: string[];
  preferences: UserPreferences;
  createdAt: string;
  updatedAt: string;
}

export interface UserPreferences {
  currency: string;
  language: string;
  notifications: {
    email: boolean;
    sms: boolean;
    push: boolean;
  };
  theme: 'light' | 'dark' | 'system';
}

export interface Address {
  id: string;
  name: string;
  street: string;
  apartment?: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
  phone?: string;
  isDefault: boolean;
  type: 'home' | 'work' | 'other';
}

// Cart and Order types
export interface CartItem {
  id: string;
  product: Product;
  quantity: number;
  selectedVariants?: Record<string, string>;
  addedAt: string;
}

export interface Order {
  id: string;
  orderNumber: string;
  userId: string;
  items: OrderItem[];
  subtotal: number;
  tax: number;
  shipping: number;
  discount: number;
  total: number;
  status: OrderStatus;
  shippingAddress: Address;
  billingAddress: Address;
  paymentMethod: PaymentMethod;
  paymentStatus: PaymentStatus;
  trackingNumber?: string;
  estimatedDelivery?: string;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface OrderItem {
  id: string;
  product: Product;
  quantity: number;
  price: number;
  selectedVariants?: Record<string, string>;
}

export type OrderStatus =
  | 'pending'
  | 'confirmed'
  | 'processing'
  | 'shipped'
  | 'out_for_delivery'
  | 'delivered'
  | 'cancelled'
  | 'refunded';

export type PaymentStatus =
  | 'pending'
  | 'processing'
  | 'completed'
  | 'failed'
  | 'cancelled'
  | 'refunded';

export interface PaymentMethod {
  id: string;
  type: 'card' | 'upi' | 'wallet' | 'bank_transfer' | 'cod';
  provider: string;
  last4?: string;
  expiryMonth?: number;
  expiryYear?: number;
  isDefault: boolean;
}

// Category and Filter types
export interface Category {
  id: string;
  name: string;
  slug: string;
  icon: string;
  image?: string;
  description?: string;
  productCount: number;
  parentId?: string;
  children?: Category[];
  isActive: boolean;
  sortOrder: number;
}

export interface FilterOptions {
  category?: string;
  subcategory?: string;
  priceRange?: [number, number];
  rating?: number;
  brand?: string[];
  inStock?: boolean;
  featured?: boolean;
  tags?: string[];
  sortBy?: SortOption;
  page?: number;
  limit?: number;
}

export type SortOption =
  | 'relevance'
  | 'price-low'
  | 'price-high'
  | 'rating'
  | 'newest'
  | 'popularity'
  | 'discount';

// API Response types
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  errors?: string[];
}

export interface PaginatedResponse<T> {
  items: T[];
  total: number;
  page: number;
  limit: number;
  totalPages: number;
  hasNext: boolean;
  hasPrev: boolean;
}

// Form types
export interface LoginForm {
  email: string;
  password: string;
  rememberMe: boolean;
}

export interface RegisterForm {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword: string;
  acceptTerms: boolean;
}

export interface CheckoutForm {
  shippingAddress: Omit<Address, 'id' | 'isDefault'>;
  billingAddress: Omit<Address, 'id' | 'isDefault'>;
  sameAsShipping: boolean;
  paymentMethod: string;
  saveAddress: boolean;
  notes?: string;
}