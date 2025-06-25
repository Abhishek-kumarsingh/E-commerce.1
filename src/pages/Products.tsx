import React, { useState, useEffect } from 'react';
import { useSearchParams, useParams } from 'react-router-dom';
import { Filter, Grid, List, SortAsc, Search, X } from 'lucide-react';
import { products, categories } from '../data/mockData';
import { Product, FilterOptions } from '../types';
import ProductGrid from '../components/ProductGrid';
import FilterSidebar from '../components/FilterSidebar';
import { motion } from 'framer-motion';
import { Helmet } from 'react-helmet-async';

const Products: React.FC = () => {
  const [searchParams] = useSearchParams();
  const { slug } = useParams<{ slug: string }>();
  const [filteredProducts, setFilteredProducts] = useState<Product[]>(products);
  const [filters, setFilters] = useState<FilterOptions>({});
  const [sortBy, setSortBy] = useState<string>('popularity');
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
  const [searchQuery, setSearchQuery] = useState('');

  // Get category from URL params or search params
  const categoryParam = slug || searchParams.get('category') || '';
  const currentCategory = categories.find(cat => cat.slug === categoryParam);

  useEffect(() => {
    if (categoryParam) {
      setFilters(prev => ({ ...prev, category: categoryParam }));
    }
  }, [categoryParam]);

  useEffect(() => {
    setLoading(true);
    
    // Reduced delay for better performance
    const timer = setTimeout(() => {
      let filtered = [...products];

      // Search filter
      if (searchQuery) {
        filtered = filtered.filter(product =>
          product.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
          product.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
          product.brand.toLowerCase().includes(searchQuery.toLowerCase())
        );
      }

      // Category filter
      if (filters.category) {
        if (Array.isArray(filters.category)) {
          filtered = filtered.filter(product => filters.category!.includes(product.category));
        } else {
          filtered = filtered.filter(product => product.category === filters.category);
        }
      }

      // Price range filter
      if (filters.priceRange) {
        filtered = filtered.filter(product =>
          product.price >= filters.priceRange![0] && product.price <= filters.priceRange![1]
        );
      }

      // Brand filter
      if (filters.brand) {
        if (Array.isArray(filters.brand)) {
          filtered = filtered.filter(product => filters.brand!.includes(product.brand));
        } else {
          filtered = filtered.filter(product => product.brand === filters.brand);
        }
      }

      // Rating filter
      if (filters.rating) {
        filtered = filtered.filter(product => product.rating >= filters.rating!);
      }

      // Stock filter
      if (filters.inStock) {
        filtered = filtered.filter(product => product.inStock);
      }

      // Sorting
      switch (sortBy) {
        case 'price-low':
          filtered.sort((a, b) => a.price - b.price);
          break;
        case 'price-high':
          filtered.sort((a, b) => b.price - a.price);
          break;
        case 'rating':
          filtered.sort((a, b) => b.rating - a.rating);
          break;
        case 'newest':
          filtered.sort((a, b) => parseInt(b.id) - parseInt(a.id));
          break;
        case 'popularity':
        default:
          filtered.sort((a, b) => b.reviewCount - a.reviewCount);
          break;
      }

      setFilteredProducts(filtered);
      setLoading(false);
    }, 100); // Reduced from 500ms to 100ms

    return () => clearTimeout(timer);
  }, [searchQuery, filters, sortBy]);

  const handleFiltersChange = (newFilters: FilterOptions) => {
    setFilters(newFilters);
  };

  const handleSortChange = (newSortBy: string) => {
    setSortBy(newSortBy);
  };

  const clearAllFilters = () => {
    setFilters({});
    setSearchQuery('');
    setSortBy('popularity');
  };

  const activeFiltersCount = Object.keys(filters).filter(key => filters[key as keyof FilterOptions]).length + (searchQuery ? 1 : 0);

  // Page title and description
  const pageTitle = currentCategory 
    ? `${currentCategory.name} - EcommerceHub`
    : searchQuery 
    ? `Search Results for "${searchQuery}" - EcommerceHub`
    : 'All Products - EcommerceHub';

  const pageDescription = currentCategory
    ? `Explore our ${currentCategory.name} collection with ${currentCategory.productCount} products`
    : searchQuery
    ? `Search results for "${searchQuery}" - Find what you're looking for`
    : 'Discover amazing products with unbeatable quality and style';

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
        <meta name="description" content={pageDescription} />
      </Helmet>

      <div className="min-h-screen bg-gradient-to-br from-neutral-50 via-white to-primary-50/30 dark:from-neutral-950 dark:via-neutral-900 dark:to-primary-950/30">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {/* Enhanced Header */}
          <motion.div 
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            className="mb-8"
          >
            {/* Breadcrumb */}
            {currentCategory && (
              <motion.div 
                className="mb-4"
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ duration: 0.6, delay: 0.1 }}
              >
                <nav className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
                  <a href="/" className="hover:text-primary-600 dark:hover:text-primary-400 transition-colors">
                    Home
                  </a>
                  <span>/</span>
                  <a href="/products" className="hover:text-primary-600 dark:hover:text-primary-400 transition-colors">
                    Products
                  </a>
                  <span>/</span>
                  <span className="text-neutral-900 dark:text-neutral-100 font-medium">
                    {currentCategory.name}
                  </span>
                </nav>
              </motion.div>
            )}

            <div className="text-center mb-12">
              <motion.h1 
                className="text-4xl md:text-5xl font-bold gradient-text mb-6"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.1 }}
              >
                {currentCategory 
                  ? currentCategory.name 
                  : searchQuery 
                  ? `Search Results for "${searchQuery}"` 
                  : 'Discover Amazing Products'
                }
              </motion.h1>
              <motion.p 
                className="text-lg md:text-xl text-neutral-600 dark:text-neutral-300 max-w-3xl mx-auto leading-relaxed"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.2 }}
              >
                {currentCategory
                  ? `${currentCategory.productCount} products in ${currentCategory.name}`
                  : searchQuery 
                  ? `Found ${filteredProducts.length} products matching your search`
                  : 'Explore our curated collection of premium products with unbeatable quality and style'
                }
              </motion.p>
            </div>

            {/* Search Bar */}
            <motion.div 
              className="max-w-3xl mx-auto mb-12"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.3 }}
            >
              <div className="relative shadow-lg">
                <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                <input
                  type="text"
                  placeholder="Search products, brands, or categories..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-12 pr-4 py-5 bg-white/90 dark:bg-neutral-900/90 backdrop-blur-sm border border-neutral-200 dark:border-neutral-700 rounded-2xl text-neutral-900 dark:text-neutral-100 placeholder-neutral-500 dark:placeholder-neutral-400 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all duration-300"
                />
              </div>
            </motion.div>
          </motion.div>

          {/* Enhanced Controls */}
          <motion.div 
            className="flex flex-col lg:flex-row justify-between items-start lg:items-center mb-8 space-y-4 lg:space-y-0"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.4 }}
          >
            {/* Mobile Filter Button */}
            <div className="flex items-center space-x-3 lg:hidden">
              <motion.button
                onClick={() => setIsFilterOpen(true)}
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
                className="flex items-center space-x-2 bg-white/80 dark:bg-neutral-900/80 backdrop-blur-sm px-4 py-3 rounded-xl shadow-soft border border-neutral-200 dark:border-neutral-700 text-neutral-700 dark:text-neutral-300 hover:bg-white dark:hover:bg-neutral-800 transition-all duration-300"
              >
                <Filter className="h-4 w-4" />
                <span className="font-medium">Filters</span>
                {activeFiltersCount > 0 && (
                  <span className="bg-primary-500 text-white text-xs rounded-full px-2 py-1 min-w-[20px] text-center">
                    {activeFiltersCount}
                  </span>
                )}
              </motion.button>

              {activeFiltersCount > 0 && (
                <motion.button
                  onClick={clearAllFilters}
                  whileHover={{ scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                  className="flex items-center space-x-2 bg-red-50 dark:bg-red-900/20 px-4 py-3 rounded-xl border border-red-200 dark:border-red-800 text-red-600 dark:text-red-400 hover:bg-red-100 dark:hover:bg-red-900/30 transition-all duration-300"
                >
                  <X className="h-4 w-4" />
                  <span className="font-medium">Clear</span>
                </motion.button>
              )}
            </div>

            {/* Desktop Controls */}
            <div className="flex items-center space-x-4 w-full lg:w-auto">
              {/* View Mode Toggle */}
              <div className="flex items-center bg-white/80 dark:bg-neutral-900/80 backdrop-blur-sm rounded-xl p-1 border border-neutral-200 dark:border-neutral-700">
                <motion.button
                  onClick={() => setViewMode('grid')}
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  className={`p-2 rounded-lg transition-all duration-300 ${
                    viewMode === 'grid'
                      ? 'bg-primary-500 text-white shadow-glow'
                      : 'text-neutral-500 dark:text-neutral-400 hover:text-neutral-700 dark:hover:text-neutral-200'
                  }`}
                >
                  <Grid className="h-4 w-4" />
                </motion.button>
                <motion.button
                  onClick={() => setViewMode('list')}
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  className={`p-2 rounded-lg transition-all duration-300 ${
                    viewMode === 'list'
                      ? 'bg-primary-500 text-white shadow-glow'
                      : 'text-neutral-500 dark:text-neutral-400 hover:text-neutral-700 dark:hover:text-neutral-200'
                  }`}
                >
                  <List className="h-4 w-4" />
                </motion.button>
              </div>

              {/* Sort Dropdown */}
              <div className="flex items-center space-x-2">
                <SortAsc className="h-4 w-4 text-neutral-500" />
                <span className="text-sm font-medium text-neutral-700 dark:text-neutral-300">Sort by:</span>
                <select
                  value={sortBy}
                  onChange={(e) => handleSortChange(e.target.value)}
                  className="bg-white/80 dark:bg-neutral-900/80 backdrop-blur-sm border border-neutral-200 dark:border-neutral-700 rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all duration-300"
                >
                  <option value="popularity">Popularity</option>
                  <option value="price-low">Price: Low to High</option>
                  <option value="price-high">Price: High to Low</option>
                  <option value="rating">Highest Rated</option>
                  <option value="newest">Newest</option>
                </select>
              </div>

              {/* Desktop Filter Button */}
              <motion.button
                onClick={() => setIsFilterOpen(true)}
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
                className="hidden lg:flex items-center space-x-2 bg-white/80 dark:bg-neutral-900/80 backdrop-blur-sm px-4 py-2 rounded-xl shadow-soft border border-neutral-200 dark:border-neutral-700 text-neutral-700 dark:text-neutral-300 hover:bg-white dark:hover:bg-neutral-800 transition-all duration-300"
              >
                <Filter className="h-4 w-4" />
                <span className="font-medium">Filters</span>
                {activeFiltersCount > 0 && (
                  <span className="bg-primary-500 text-white text-xs rounded-full px-2 py-1 min-w-[20px] text-center">
                    {activeFiltersCount}
                  </span>
                )}
              </motion.button>
            </div>
          </motion.div>

          {/* Active Filters Display */}
          {activeFiltersCount > 0 && (
            <motion.div 
              className="mb-6"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.5 }}
            >
              <div className="flex flex-wrap items-center gap-2">
                <span className="text-sm font-medium text-neutral-700 dark:text-neutral-300">Active filters:</span>
                {searchQuery && (
                  <span className="inline-flex items-center px-3 py-1 rounded-full text-sm bg-primary-100 dark:bg-primary-900/30 text-primary-700 dark:text-primary-300">
                    Search: "{searchQuery}"
                    <button
                      onClick={() => setSearchQuery('')}
                      className="ml-2 hover:text-primary-900 dark:hover:text-primary-100"
                    >
                      <X className="h-3 w-3" />
                    </button>
                  </span>
                )}
                {Object.entries(filters).map(([key, value]) => {
                  if (!value) return null;
                  return (
                    <span key={key} className="inline-flex items-center px-3 py-1 rounded-full text-sm bg-secondary-100 dark:bg-secondary-900/30 text-secondary-700 dark:text-secondary-300">
                      {key}: {Array.isArray(value) ? `${value[0]}-${value[1]}` : value}
                      <button
                        onClick={() => setFilters(prev => ({ ...prev, [key]: undefined }))}
                        className="ml-2 hover:text-secondary-900 dark:hover:text-secondary-100"
                      >
                        <X className="h-3 w-3" />
                      </button>
                    </span>
                  );
                })}
                <button
                  onClick={clearAllFilters}
                  className="text-sm text-red-600 dark:text-red-400 hover:text-red-800 dark:hover:text-red-200 font-medium"
                >
                  Clear all
                </button>
              </div>
            </motion.div>
          )}

          {/* Main Content */}
          <div className="flex flex-col lg:flex-row gap-8">
            {/* Sidebar */}
            <div className="lg:w-80 flex-shrink-0 lg:sticky lg:top-24">
              <FilterSidebar
                filters={filters}
                onFiltersChange={handleFiltersChange}
                isOpen={isFilterOpen}
                onClose={() => setIsFilterOpen(false)}
              />
            </div>

            {/* Products Grid */}
            <div className="flex-1 w-full">
              <div className="container mx-auto px-4">
                <ProductGrid 
                  products={filteredProducts} 
                  loading={loading} 
                  viewMode={viewMode}
                />
              </div>
            </div>
          </div>

          {/* Empty State */}
          {!loading && filteredProducts.length === 0 && (
            <motion.div 
              className="text-center py-16"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
            >
              <div className="max-w-md mx-auto">
                <div className="w-24 h-24 mx-auto mb-6 bg-neutral-100 dark:bg-neutral-800 rounded-full flex items-center justify-center">
                  <Search className="h-12 w-12 text-neutral-400" />
                </div>
                <h3 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                  No products found
                </h3>
                <p className="text-neutral-600 dark:text-neutral-400 mb-6">
                  Try adjusting your search criteria or filters to find what you're looking for.
                </p>
                <motion.button
                  onClick={clearAllFilters}
                  whileHover={{ scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                  className="btn-primary"
                >
                  Clear All Filters
                </motion.button>
              </div>
            </motion.div>
          )}
        </div>
      </div>
    </>
  );
};

export default Products;