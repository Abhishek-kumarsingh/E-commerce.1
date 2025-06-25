import { Product } from '../types';

export const categories = [
  { id: '1', name: 'Electronics', slug: 'electronics', icon: 'Smartphone', productCount: 156 },
  { id: '2', name: 'Clothing', slug: 'clothing', icon: 'Shirt', productCount: 243 },
  { id: '3', name: 'Home & Garden', slug: 'home-garden', icon: 'Home', productCount: 89 },
  { id: '4', name: 'Books', slug: 'books', icon: 'Book', productCount: 167 },
  { id: '5', name: 'Sports', slug: 'sports', icon: 'Dumbbell', productCount: 78 },
  { id: '6', name: 'Beauty', slug: 'beauty', icon: 'Sparkles', productCount: 134 },
];

export const products: Product[] = [
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
    stockQuantity: 45,
    featured: true,
    tags: ['wireless', 'noise-cancelling', 'premium'],
    brand: 'AudioTech',
    specifications: {
      'Battery Life': '30 hours',
      'Connectivity': 'Bluetooth 5.0',
      'Weight': '250g',
      'Warranty': '2 years'
    } as Record<string, string>,
    createdAt: '2024-01-15T10:00:00Z',
    updatedAt: '2024-01-15T10:00:00Z'
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
    stockQuantity: 32,
    featured: true,
    tags: ['fitness', 'smartwatch', 'health'],
    brand: 'FitTech',
    specifications: {
      'Display': '1.4" AMOLED',
      'Battery': '7 days',
      'Water Resistance': '50m',
      'GPS': 'Built-in'
    } as Record<string, string>,
    createdAt: '2024-01-10T14:30:00Z',
    updatedAt: '2024-01-10T14:30:00Z'
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
    stockQuantity: 120,
    featured: false,
    tags: ['organic', 'comfortable', 'casual'],
    brand: 'EcoWear',
    specifications: {
      'Material': '100% Organic Cotton',
      'Care': 'Machine Wash',
      'Fit': 'Regular',
      'Origin': 'Sustainably Sourced'
    } as Record<string, string>,
    createdAt: '2024-01-05T09:15:00Z',
    updatedAt: '2024-01-05T09:15:00Z'
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
    stockQuantity: 18,
    featured: true,
    tags: ['photography', 'professional', 'prime lens'],
    brand: 'LensCraft',
    specifications: {
      'Focal Length': '50mm',
      'Aperture': 'f/1.4',
      'Mount': 'Universal',
      'Weight': '600g'
    } as Record<string, string>,
    createdAt: '2024-01-12T16:45:00Z',
    updatedAt: '2024-01-12T16:45:00Z'
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
    stockQuantity: 25,
    featured: false,
    tags: ['furniture', 'comfort', 'reading'],
    brand: 'HomeFurn',
    specifications: {
      'Material': 'Premium Fabric',
      'Dimensions': '32" W x 34" D x 40" H',
      'Weight Capacity': '300 lbs',
      'Assembly': 'Required'
    } as Record<string, string>,
    createdAt: '2024-01-08T11:20:00Z',
    updatedAt: '2024-01-08T11:20:00Z'
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
    stockQuantity: 200,
    featured: true,
    tags: ['fiction', 'bestseller', 'award-winning'],
    brand: 'Literary Press',
    specifications: {
      'Pages': '384',
      'Format': 'Paperback',
      'Language': 'English',
      'Publication': '2024'
    } as Record<string, string>,
    createdAt: '2024-01-03T13:10:00Z',
    updatedAt: '2024-01-03T13:10:00Z'
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
    stockQuantity: 75,
    featured: false,
    tags: ['yoga', 'fitness', 'eco-friendly'],
    brand: 'ZenFit',
    specifications: {
      'Thickness': '6mm',
      'Material': 'Natural Rubber',
      'Dimensions': '72" x 24"',
      'Weight': '2.5 lbs'
    } as Record<string, string>,
    createdAt: '2024-01-06T15:30:00Z',
    updatedAt: '2024-01-06T15:30:00Z'
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
    stockQuantity: 60,
    featured: true,
    tags: ['skincare', 'luxury', 'anti-aging'],
    brand: 'BeautyLux',
    specifications: {
      'Products': '5-piece set',
      'Skin Type': 'All skin types',
      'Key Ingredients': 'Vitamin C, Hyaluronic Acid',
      'Cruelty-Free': 'Yes'
    } as Record<string, string>,
    createdAt: '2024-01-14T12:00:00Z',
    updatedAt: '2024-01-14T12:00:00Z'
  },
  {
    id: '9',
    name: 'Gaming Mechanical Keyboard',
    description: 'RGB backlit mechanical keyboard with tactile switches, perfect for gaming and productivity.',
    price: 129.99,
    originalPrice: 179.99,
    image: 'https://images.pexels.com/photos/2115257/pexels-photo-2115257.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/2115257/pexels-photo-2115257.jpeg?auto=compress&cs=tinysrgb&w=500',
      'https://images.pexels.com/photos/1714208/pexels-photo-1714208.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'electronics',
    rating: 4.7,
    reviewCount: 342,
    inStock: true,
    stockQuantity: 85,
    featured: true,
    tags: ['gaming', 'mechanical', 'rgb', 'productivity'],
    brand: 'GameTech',
    specifications: {
      'Switch Type': 'Cherry MX Blue',
      'Backlight': 'RGB',
      'Layout': 'Full Size',
      'Connection': 'USB-C',
      'Compatibility': 'Windows, Mac, Linux'
    } as Record<string, string>,
    createdAt: '2024-01-16T09:30:00Z',
    updatedAt: '2024-01-16T09:30:00Z'
  },
  {
    id: '10',
    name: 'Wireless Bluetooth Speaker',
    description: '360-degree sound with deep bass, waterproof design perfect for outdoor adventures.',
    price: 89.99,
    originalPrice: 119.99,
    image: 'https://images.pexels.com/photos/1649771/pexels-photo-1649771.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/1649771/pexels-photo-1649771.jpeg?auto=compress&cs=tinysrgb&w=500',
      'https://images.pexels.com/photos/164938/pexels-photo-164938.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'electronics',
    rating: 4.5,
    reviewCount: 198,
    inStock: true,
    stockQuantity: 67,
    featured: false,
    tags: ['wireless', 'bluetooth', 'waterproof', 'portable'],
    brand: 'SoundWave',
    specifications: {
      'Battery Life': '12 hours',
      'Water Rating': 'IPX7',
      'Connectivity': 'Bluetooth 5.0',
      'Range': '30 feet',
      'Weight': '1.2 lbs'
    } as Record<string, string>,
    createdAt: '2024-01-17T14:15:00Z',
    updatedAt: '2024-01-17T14:15:00Z'
  },
  {
    id: '11',
    name: 'Designer Sunglasses',
    description: 'Premium polarized sunglasses with UV protection and stylish frame design.',
    price: 199.99,
    originalPrice: 299.99,
    image: 'https://images.pexels.com/photos/701877/pexels-photo-701877.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/701877/pexels-photo-701877.jpeg?auto=compress&cs=tinysrgb&w=500',
      'https://images.pexels.com/photos/46710/pexels-photo-46710.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'clothing',
    rating: 4.6,
    reviewCount: 156,
    inStock: true,
    stockQuantity: 42,
    featured: true,
    tags: ['designer', 'polarized', 'uv-protection', 'fashion'],
    brand: 'LuxVision',
    specifications: {
      'Lens Material': 'Polarized Glass',
      'Frame Material': 'Titanium',
      'UV Protection': '100% UV400',
      'Weight': '28g',
      'Warranty': '2 years'
    } as Record<string, string>,
    createdAt: '2024-01-18T11:45:00Z',
    updatedAt: '2024-01-18T11:45:00Z'
  },
  {
    id: '12',
    name: 'Smart Home Security Camera',
    description: '4K wireless security camera with night vision, motion detection, and mobile app control.',
    price: 149.99,
    originalPrice: 199.99,
    image: 'https://images.pexels.com/photos/430208/pexels-photo-430208.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/430208/pexels-photo-430208.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'electronics',
    rating: 4.4,
    reviewCount: 289,
    inStock: true,
    stockQuantity: 38,
    featured: false,
    tags: ['security', 'smart-home', '4k', 'wireless'],
    brand: 'SecureHome',
    specifications: {
      'Resolution': '4K Ultra HD',
      'Night Vision': 'Up to 30ft',
      'Storage': 'Cloud & Local',
      'Power': 'Battery/Wired',
      'App': 'iOS & Android'
    } as Record<string, string>,
    createdAt: '2024-01-19T16:20:00Z',
    updatedAt: '2024-01-19T16:20:00Z'
  },
  {
    id: '13',
    name: 'Ergonomic Office Chair',
    description: 'Premium ergonomic office chair with lumbar support, adjustable height, and breathable mesh.',
    price: 349.99,
    originalPrice: 449.99,
    image: 'https://images.pexels.com/photos/2181996/pexels-photo-2181996.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/2181996/pexels-photo-2181996.jpeg?auto=compress&cs=tinysrgb&w=500',
      'https://images.pexels.com/photos/1957477/pexels-photo-1957477.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'home-garden',
    rating: 4.8,
    reviewCount: 445,
    inStock: true,
    stockQuantity: 29,
    featured: true,
    tags: ['ergonomic', 'office', 'adjustable', 'comfort'],
    brand: 'ErgoWork',
    specifications: {
      'Material': 'Breathable Mesh',
      'Weight Capacity': '330 lbs',
      'Height Range': '17"-21"',
      'Warranty': '5 years',
      'Assembly': 'Required'
    } as Record<string, string>,
    createdAt: '2024-01-20T10:30:00Z',
    updatedAt: '2024-01-20T10:30:00Z'
  },
  {
    id: '14',
    name: 'Stainless Steel Water Bottle',
    description: 'Insulated stainless steel water bottle that keeps drinks cold for 24h or hot for 12h.',
    price: 34.99,
    originalPrice: 49.99,
    image: 'https://images.pexels.com/photos/1000084/pexels-photo-1000084.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/1000084/pexels-photo-1000084.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'sports',
    rating: 4.7,
    reviewCount: 523,
    inStock: true,
    stockQuantity: 156,
    featured: false,
    tags: ['insulated', 'eco-friendly', 'durable', 'sports'],
    brand: 'HydroLife',
    specifications: {
      'Capacity': '32 oz',
      'Material': '18/8 Stainless Steel',
      'Insulation': 'Double Wall Vacuum',
      'Lid Type': 'Leak-proof',
      'BPA Free': 'Yes'
    } as Record<string, string>,
    createdAt: '2024-01-21T13:45:00Z',
    updatedAt: '2024-01-21T13:45:00Z'
  },
  {
    id: '15',
    name: 'Wireless Charging Pad',
    description: 'Fast wireless charging pad compatible with all Qi-enabled devices, sleek design.',
    price: 39.99,
    originalPrice: 59.99,
    image: 'https://images.pexels.com/photos/4219654/pexels-photo-4219654.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/4219654/pexels-photo-4219654.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'electronics',
    rating: 4.3,
    reviewCount: 167,
    inStock: true,
    stockQuantity: 94,
    featured: false,
    tags: ['wireless', 'charging', 'qi-compatible', 'fast-charge'],
    brand: 'ChargeTech',
    specifications: {
      'Output': '15W Fast Charge',
      'Compatibility': 'Qi-enabled devices',
      'LED Indicator': 'Yes',
      'Safety': 'Over-charge protection',
      'Cable': 'USB-C included'
    } as Record<string, string>,
    createdAt: '2024-01-22T08:15:00Z',
    updatedAt: '2024-01-22T08:15:00Z'
  },
  {
    id: '16',
    name: 'Artisan Coffee Beans',
    description: 'Premium single-origin coffee beans, freshly roasted with rich flavor and aroma.',
    price: 24.99,
    originalPrice: 34.99,
    image: 'https://images.pexels.com/photos/894695/pexels-photo-894695.jpeg?auto=compress&cs=tinysrgb&w=500',
    images: [
      'https://images.pexels.com/photos/894695/pexels-photo-894695.jpeg?auto=compress&cs=tinysrgb&w=500',
      'https://images.pexels.com/photos/1695052/pexels-photo-1695052.jpeg?auto=compress&cs=tinysrgb&w=500'
    ],
    category: 'home-garden',
    rating: 4.9,
    reviewCount: 678,
    inStock: true,
    stockQuantity: 234,
    featured: true,
    tags: ['coffee', 'artisan', 'single-origin', 'premium'],
    brand: 'RoastMaster',
    specifications: {
      'Weight': '12 oz',
      'Roast Level': 'Medium',
      'Origin': 'Colombian Highlands',
      'Processing': 'Washed',
      'Roast Date': 'Within 7 days'
    } as Record<string, string>,
    createdAt: '2024-01-23T15:30:00Z',
    updatedAt: '2024-01-23T15:30:00Z'
  }
];