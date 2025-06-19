// MongoDB initialization script for E-commerce platform
// This script creates sample products and categories

// Switch to the ecommerce_products database
db = db.getSiblingDB('ecommerce_products');

// Create categories collection with sample data
db.categories.insertMany([
  {
    _id: ObjectId(),
    name: "Electronics",
    slug: "electronics",
    description: "Electronic devices and gadgets",
    icon: "electronics-icon.svg",
    image: "electronics-banner.jpg",
    productCount: 0,
    sortOrder: 1,
    isActive: true,
    isFeatured: true,
    tags: ["electronics", "gadgets", "technology"],
    metaTitle: "Electronics - Latest Gadgets and Devices",
    metaDescription: "Shop the latest electronics, gadgets, and technology products",
    metaKeywords: ["electronics", "gadgets", "smartphones", "laptops"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Smartphones",
    slug: "smartphones",
    description: "Latest smartphones and mobile devices",
    parentId: null, // Will be updated to Electronics category ID
    productCount: 0,
    sortOrder: 1,
    isActive: true,
    isFeatured: true,
    tags: ["smartphones", "mobile", "android", "ios"],
    metaTitle: "Smartphones - Latest Mobile Devices",
    metaDescription: "Shop the latest smartphones from top brands",
    metaKeywords: ["smartphones", "mobile", "android", "iphone"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Laptops",
    slug: "laptops",
    description: "Laptops and computers for work and gaming",
    parentId: null, // Will be updated to Electronics category ID
    productCount: 0,
    sortOrder: 2,
    isActive: true,
    isFeatured: true,
    tags: ["laptops", "computers", "gaming", "work"],
    metaTitle: "Laptops - Best Computers for Work and Gaming",
    metaDescription: "Find the perfect laptop for work, gaming, or everyday use",
    metaKeywords: ["laptops", "computers", "gaming", "work"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Accessories",
    slug: "accessories",
    description: "Electronic accessories and peripherals",
    parentId: null, // Will be updated to Electronics category ID
    productCount: 0,
    sortOrder: 3,
    isActive: true,
    isFeatured: false,
    tags: ["accessories", "peripherals", "cables", "cases"],
    metaTitle: "Electronic Accessories - Cables, Cases, and More",
    metaDescription: "Shop electronic accessories, cables, cases, and peripherals",
    metaKeywords: ["accessories", "cables", "cases", "chargers"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Fashion",
    slug: "fashion",
    description: "Clothing, shoes, and fashion accessories",
    icon: "fashion-icon.svg",
    image: "fashion-banner.jpg",
    productCount: 0,
    sortOrder: 2,
    isActive: true,
    isFeatured: true,
    tags: ["fashion", "clothing", "shoes", "accessories"],
    metaTitle: "Fashion - Trendy Clothing and Accessories",
    metaDescription: "Discover the latest fashion trends in clothing and accessories",
    metaKeywords: ["fashion", "clothing", "shoes", "accessories"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Home & Garden",
    slug: "home-garden",
    description: "Home improvement and garden supplies",
    icon: "home-icon.svg",
    image: "home-banner.jpg",
    productCount: 0,
    sortOrder: 3,
    isActive: true,
    isFeatured: false,
    tags: ["home", "garden", "furniture", "decor"],
    metaTitle: "Home & Garden - Furniture and Decor",
    metaDescription: "Transform your home with our furniture and garden supplies",
    metaKeywords: ["home", "garden", "furniture", "decor"],
    createdAt: new Date(),
    updatedAt: new Date()
  }
]);

// Create products collection with sample data
db.products.insertMany([
  {
    _id: ObjectId(),
    name: "Wireless Bluetooth Headphones",
    sku: "WBH-001",
    description: "Premium wireless Bluetooth headphones with noise cancellation and 30-hour battery life. Perfect for music lovers and professionals.",
    price: NumberDecimal("299.99"),
    originalPrice: NumberDecimal("399.99"),
    mainImage: "headphones-main.jpg",
    images: ["headphones-1.jpg", "headphones-2.jpg", "headphones-3.jpg"],
    category: "Electronics",
    subcategory: "Accessories",
    brand: "AudioTech",
    stockQuantity: 50,
    lowStockThreshold: 10,
    weight: NumberDecimal("0.8"),
    dimensions: {
      length: NumberDecimal("20.0"),
      width: NumberDecimal("18.0"),
      height: NumberDecimal("8.0")
    },
    isActive: true,
    isFeatured: true,
    isDigital: false,
    tags: ["wireless", "bluetooth", "headphones", "noise-cancellation"],
    specifications: {
      "Battery Life": "30 hours",
      "Connectivity": "Bluetooth 5.0",
      "Noise Cancellation": "Active",
      "Weight": "800g",
      "Warranty": "2 years"
    },
    variants: [
      {
        name: "Color",
        options: ["Black", "White", "Blue"]
      },
      {
        name: "Size",
        options: ["Standard", "Large"]
      }
    ],
    viewCount: 1250,
    salesCount: 89,
    averageRating: NumberDecimal("4.5"),
    reviewCount: 23,
    metaTitle: "Wireless Bluetooth Headphones - Premium Audio Experience",
    metaDescription: "Experience premium audio with our wireless Bluetooth headphones featuring noise cancellation",
    metaKeywords: ["wireless headphones", "bluetooth", "noise cancellation", "premium audio"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Smartphone Case - Premium Protection",
    sku: "SC-002",
    description: "Durable smartphone case with military-grade protection. Compatible with wireless charging and featuring precise cutouts.",
    price: NumberDecimal("24.99"),
    mainImage: "phone-case-main.jpg",
    images: ["phone-case-1.jpg", "phone-case-2.jpg"],
    category: "Electronics",
    subcategory: "Accessories",
    brand: "ProtectPro",
    stockQuantity: 200,
    lowStockThreshold: 20,
    weight: NumberDecimal("0.1"),
    isActive: true,
    isFeatured: false,
    isDigital: false,
    tags: ["smartphone", "case", "protection", "wireless-charging"],
    specifications: {
      "Material": "TPU + PC",
      "Drop Protection": "Military Grade",
      "Wireless Charging": "Compatible",
      "Warranty": "1 year"
    },
    variants: [
      {
        name: "Color",
        options: ["Clear", "Black", "Blue", "Red"]
      },
      {
        name: "Phone Model",
        options: ["iPhone 14", "iPhone 14 Pro", "Samsung S23", "Samsung S23 Ultra"]
      }
    ],
    viewCount: 890,
    salesCount: 156,
    averageRating: NumberDecimal("4.2"),
    reviewCount: 45,
    metaTitle: "Premium Smartphone Case - Military Grade Protection",
    metaDescription: "Protect your smartphone with our military-grade case featuring wireless charging compatibility",
    metaKeywords: ["smartphone case", "protection", "wireless charging", "military grade"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "USB-C Fast Charging Cable",
    sku: "USB-003",
    description: "High-speed USB-C charging cable with data transfer capabilities. Durable braided design with reinforced connectors.",
    price: NumberDecimal("19.99"),
    mainImage: "usb-cable-main.jpg",
    images: ["usb-cable-1.jpg", "usb-cable-2.jpg"],
    category: "Electronics",
    subcategory: "Accessories",
    brand: "ChargeFast",
    stockQuantity: 300,
    lowStockThreshold: 50,
    weight: NumberDecimal("0.2"),
    isActive: true,
    isFeatured: false,
    isDigital: false,
    tags: ["usb-c", "charging", "cable", "fast-charging"],
    specifications: {
      "Length": "6 feet",
      "Data Transfer": "480 Mbps",
      "Charging Speed": "Fast Charge",
      "Material": "Braided Nylon",
      "Warranty": "1 year"
    },
    variants: [
      {
        name: "Length",
        options: ["3 feet", "6 feet", "10 feet"]
      },
      {
        name: "Color",
        options: ["Black", "White", "Red"]
      }
    ],
    viewCount: 567,
    salesCount: 234,
    averageRating: NumberDecimal("4.0"),
    reviewCount: 67,
    metaTitle: "USB-C Fast Charging Cable - High Speed Data Transfer",
    metaDescription: "Fast charging USB-C cable with high-speed data transfer and durable braided design",
    metaKeywords: ["usb-c cable", "fast charging", "data transfer", "braided"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Wireless Charging Pad",
    sku: "WC-004",
    description: "Qi-certified wireless charging pad with LED indicator and foreign object detection. Compatible with all Qi-enabled devices.",
    price: NumberDecimal("79.99"),
    originalPrice: NumberDecimal("99.99"),
    mainImage: "wireless-charger-main.jpg",
    images: ["wireless-charger-1.jpg", "wireless-charger-2.jpg"],
    category: "Electronics",
    subcategory: "Accessories",
    brand: "PowerWave",
    stockQuantity: 75,
    lowStockThreshold: 15,
    weight: NumberDecimal("0.3"),
    isActive: true,
    isFeatured: true,
    isDigital: false,
    tags: ["wireless", "charging", "qi-certified", "led-indicator"],
    specifications: {
      "Charging Speed": "15W Fast Charge",
      "Compatibility": "Qi-enabled devices",
      "Safety Features": "Foreign Object Detection",
      "LED Indicator": "Yes",
      "Warranty": "2 years"
    },
    variants: [
      {
        name: "Color",
        options: ["Black", "White"]
      }
    ],
    viewCount: 445,
    salesCount: 67,
    averageRating: NumberDecimal("4.3"),
    reviewCount: 18,
    metaTitle: "Wireless Charging Pad - Qi Certified Fast Charging",
    metaDescription: "Qi-certified wireless charging pad with fast charging and safety features",
    metaKeywords: ["wireless charging", "qi certified", "fast charging", "led indicator"],
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Portable Bluetooth Speaker",
    sku: "BS-005",
    description: "Compact portable Bluetooth speaker with 360-degree sound, waterproof design, and 12-hour battery life.",
    price: NumberDecimal("89.99"),
    mainImage: "bluetooth-speaker-main.jpg",
    images: ["bluetooth-speaker-1.jpg", "bluetooth-speaker-2.jpg", "bluetooth-speaker-3.jpg"],
    category: "Electronics",
    subcategory: "Accessories",
    brand: "SoundWave",
    stockQuantity: 120,
    lowStockThreshold: 25,
    weight: NumberDecimal("0.6"),
    isActive: true,
    isFeatured: true,
    isDigital: false,
    tags: ["bluetooth", "speaker", "portable", "waterproof"],
    specifications: {
      "Battery Life": "12 hours",
      "Water Resistance": "IPX7",
      "Sound": "360-degree",
      "Connectivity": "Bluetooth 5.0",
      "Warranty": "1 year"
    },
    variants: [
      {
        name: "Color",
        options: ["Black", "Blue", "Red", "Green"]
      }
    ],
    viewCount: 678,
    salesCount: 98,
    averageRating: NumberDecimal("4.1"),
    reviewCount: 34,
    metaTitle: "Portable Bluetooth Speaker - 360° Sound & Waterproof",
    metaDescription: "Compact Bluetooth speaker with 360-degree sound and waterproof design",
    metaKeywords: ["bluetooth speaker", "portable", "waterproof", "360 sound"],
    createdAt: new Date(),
    updatedAt: new Date()
  }
]);

// Create indexes for better performance
db.products.createIndex({ "name": "text", "description": "text", "tags": "text" });
db.products.createIndex({ "category": 1 });
db.products.createIndex({ "brand": 1 });
db.products.createIndex({ "price": 1 });
db.products.createIndex({ "isActive": 1 });
db.products.createIndex({ "isFeatured": 1 });
db.products.createIndex({ "stockQuantity": 1 });
db.products.createIndex({ "averageRating": -1 });
db.products.createIndex({ "viewCount": -1 });
db.products.createIndex({ "salesCount": -1 });
db.products.createIndex({ "createdAt": -1 });

db.categories.createIndex({ "name": "text", "description": "text", "tags": "text" });
db.categories.createIndex({ "slug": 1 }, { unique: true });
db.categories.createIndex({ "parentId": 1 });
db.categories.createIndex({ "isActive": 1 });
db.categories.createIndex({ "isFeatured": 1 });
db.categories.createIndex({ "sortOrder": 1 });

print("MongoDB initialization completed successfully!");
print("Created categories and products collections with sample data and indexes.");
