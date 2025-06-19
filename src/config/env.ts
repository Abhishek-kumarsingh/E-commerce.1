// Environment configuration
export const config = {
  // API Configuration
  API_BASE_URL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  
  // Authentication
  CLERK_PUBLISHABLE_KEY: import.meta.env.VITE_CLERK_PUBLISHABLE_KEY || '',
  
  // Payment Gateways
  STRIPE_PUBLISHABLE_KEY: import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY || '',
  RAZORPAY_KEY_ID: import.meta.env.VITE_RAZORPAY_KEY_ID || '',
  
  // App Configuration
  APP_NAME: 'EcommerceHub',
  APP_VERSION: '1.0.0',
  APP_DESCRIPTION: 'Modern E-commerce Platform',
  
  // Features
  FEATURES: {
    REVIEWS: true,
    WISHLIST: true,
    NOTIFICATIONS: true,
    ANALYTICS: true,
    MULTI_CURRENCY: false,
    MULTI_LANGUAGE: false,
  },
  
  // Pagination
  DEFAULT_PAGE_SIZE: 12,
  MAX_PAGE_SIZE: 100,
  
  // File Upload
  MAX_FILE_SIZE: 5 * 1024 * 1024, // 5MB
  ALLOWED_IMAGE_TYPES: ['image/jpeg', 'image/png', 'image/webp'],
  
  // Cache
  CACHE_DURATION: 5 * 60 * 1000, // 5 minutes
  
  // Development
  IS_DEVELOPMENT: import.meta.env.DEV,
  IS_PRODUCTION: import.meta.env.PROD,
  
  // Analytics
  GOOGLE_ANALYTICS_ID: import.meta.env.VITE_GA_ID || '',
  
  // Social Login
  GOOGLE_CLIENT_ID: import.meta.env.VITE_GOOGLE_CLIENT_ID || '',
  FACEBOOK_APP_ID: import.meta.env.VITE_FACEBOOK_APP_ID || '',
} as const;

// Validate required environment variables
export const validateEnv = () => {
  const required = [
    'VITE_API_BASE_URL',
    'VITE_CLERK_PUBLISHABLE_KEY',
  ];
  
  const missing = required.filter(key => !import.meta.env[key]);
  
  if (missing.length > 0) {
    console.warn('Missing environment variables:', missing);
  }
  
  return missing.length === 0;
};
