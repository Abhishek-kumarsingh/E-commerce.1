export const categories = [
  { id: '1', name: 'Electronics', slug: 'electronics', icon: 'Smartphone', productCount: 156 },
  { id: '2', name: 'Clothing', slug: 'clothing', icon: 'Shirt', productCount: 243 },
  { id: '3', name: 'Home & Garden', slug: 'home-garden', icon: 'Home', productCount: 89 },
  { id: '4', name: 'Books', slug: 'books', icon: 'Book', productCount: 167 },
  { id: '5', name: 'Sports', slug: 'sports', icon: 'Dumbbell', productCount: 78 },
  { id: '6', name: 'Beauty', slug: 'beauty', icon: 'Sparkles', productCount: 134 },
];

export const products = [
  {
    id: '1',
    name: 'Premium Wireless Headphones',
    description: 'High-quality wireless headphones with active noise cancellation and 30-hour battery life.',
    price: 299.99,
    originalPrice: 399.99,
    image: 'https://images.pexels.com/photos/3394650/pexels-photo-3394650.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/3394650/pexels-photo-3394650.jpeg?auto=compress&cs=tinysrgb&w=500',
      'https://images.pexels.com/photos/205926/pexels-photo-205926.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'electronics',
    rating: 4.8,
    reviewCount: 234,
    inStock: true,
    featured: true,
    tags: ['wireless', 'noise-cancelling', 'premium'],
    brand: 'AudioTech',
    specifications: {
      'Battery Life': '30 hours',
      'Connectivity': 'Bluetooth 5.0',
      'Weight': '250g',
      'Warranty': '2 years'
    }
  },
  {
    id: '2',
    name: 'Smart Fitness Watch',
    description: 'Advanced fitness tracking with heart rate monitoring, GPS, and smartphone integration.',
    price: 199.99,
    originalPrice: 249.99,
    image: 'https://images.pexels.com/photos/393047/pexels-photo-393047.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/393047/pexels-photo-393047.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'electronics',
    rating: 4.6,
    reviewCount: 189,
    inStock: true,
    featured: true,
    tags: ['fitness', 'smartwatch', 'health'],
    brand: 'FitTech',
    specifications: {
      'Display': '1.4" AMOLED',
      'Battery': '7 days',
      'Water Resistance': '50m',
      'GPS': 'Built-in'
    }
  },
  {
    id: '3',
    name: 'Organic Cotton T-Shirt',
    description: 'Comfortable, eco-friendly t-shirt made from 100% organic cotton.',
    price: 29.99,
    image: 'https://images.pexels.com/photos/1656684/pexels-photo-1656684.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/1656684/pexels-photo-1656684.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'clothing',
    rating: 4.4,
    reviewCount: 67,
    inStock: true,
    featured: false,
    tags: ['organic', 'comfortable', 'casual'],
    brand: 'EcoWear',
    specifications: {
      'Material': '100% Organic Cotton',
      'Care': 'Machine Wash',
      'Fit': 'Regular',
      'Origin': 'Sustainably Sourced'
    }
  },
  {
    id: '4',
    name: 'Professional Camera Lens',
    description: 'High-quality 50mm prime lens perfect for portrait and street photography.',
    price: 599.99,
    originalPrice: 699.99,
    image: 'https://images.pexels.com/photos/90946/pexels-photo-90946.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/90946/pexels-photo-90946.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'electronics',
    rating: 4.9,
    reviewCount: 156,
    inStock: true,
    featured: true,
    tags: ['photography', 'professional', 'prime lens'],
    brand: 'LensCraft',
    specifications: {
      'Focal Length': '50mm',
      'Aperture': 'f/1.4',
      'Mount': 'Universal',
      'Weight': '600g'
    }
  },
  {
    id: '5',
    name: 'Cozy Reading Chair',
    description: 'Comfortable armchair perfect for reading, with premium upholstery and ergonomic design.',
    price: 449.99,
    image: 'https://images.pexels.com/photos/1350789/pexels-photo-1350789.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/1350789/pexels-photo-1350789.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'home-garden',
    rating: 4.7,
    reviewCount: 89,
    inStock: true,
    featured: false,
    tags: ['furniture', 'comfort', 'reading'],
    brand: 'HomeFurn',
    specifications: {
      'Material': 'Premium Fabric',
      'Dimensions': '32" W x 34" D x 40" H',
      'Weight Capacity': '300 lbs',
      'Assembly': 'Required'
    }
  },
  {
    id: '6',
    name: 'Bestselling Novel',
    description: 'Captivating story that has topped bestseller lists worldwide. A must-read for book lovers.',
    price: 14.99,
    originalPrice: 19.99,
    image: 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'books',
    rating: 4.5,
    reviewCount: 412,
    inStock: true,
    featured: true,
    tags: ['fiction', 'bestseller', 'award-winning'],
    brand: 'Literary Press',
    specifications: {
      'Pages': '384',
      'Format': 'Paperback',
      'Language': 'English',
      'Publication': '2024'
    }
  },
  {
    id: '7',
    name: 'Yoga Mat Premium',
    description: 'Non-slip, eco-friendly yoga mat with superior grip and cushioning for all yoga practices.',
    price: 79.99,
    image: 'https://images.pexels.com/photos/4056723/pexels-photo-4056723.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/4056723/pexels-photo-4056723.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'sports',
    rating: 4.6,
    reviewCount: 123,
    inStock: true,
    featured: false,
    tags: ['yoga', 'fitness', 'eco-friendly'],
    brand: 'ZenFit',
    specifications: {
      'Thickness': '6mm',
      'Material': 'Natural Rubber',
      'Dimensions': '72" x 24"',
      'Weight': '2.5 lbs'
    }
  },
  {
    id: '8',
    name: 'Luxury Skincare Set',
    description: 'Complete skincare routine with premium ingredients for radiant, healthy skin.',
    price: 159.99,
    originalPrice: 199.99,
    image: 'https://images.pexels.com/photos/3762879/pexels-photo-3762879.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/3762879/pexels-photo-3762879.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'beauty',
    rating: 4.8,
    reviewCount: 267,
    inStock: true,
    featured: true,
    tags: ['skincare', 'luxury', 'anti-aging'],
    brand: 'BeautyLux',
    specifications: {
      'Products': '5-piece set',
      'Skin Type': 'All skin types',
      'Key Ingredients': 'Vitamin C, Hyaluronic Acid',
      'Cruelty-Free': 'Yes'
    }
  }
];

export const mockOrders = [
  {
    id: 'ORD-001',
    userId: '1',
    items: [
      { product: products[0], quantity: 1 },
      { product: products[1], quantity: 2 }
    ],
    total: 699.97,
    status: 'delivered' as const,
    shippingAddress: {
      id: '1',
      name: 'John Doe',
      street: '123 Main St',
      city: 'New York',
      state: 'NY',
      zipCode: '10001',
      country: 'USA',
      isDefault: true
    },
    paymentMethod: 'Credit Card',
    createdAt: '2024-01-15T10:30:00Z',
    updatedAt: '2024-01-18T14:22:00Z'
  },
  {
    id: 'ORD-002',
    userId: '2',
    items: [
      { product: products[2], quantity: 3 },
      { product: products[5], quantity: 1 }
    ],
    total: 104.96,
    status: 'shipped' as const,
    shippingAddress: {
      id: '2',
      name: 'Jane Smith',
      street: '456 Oak Ave',
      city: 'Los Angeles',
      state: 'CA',
      zipCode: '90210',
      country: 'USA',
      isDefault: true
    },
    paymentMethod: 'PayPal',
    createdAt: '2024-01-16T15:45:00Z',
    updatedAt: '2024-01-17T09:15:00Z'
  }
];