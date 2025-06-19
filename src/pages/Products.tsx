import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Filter, Grid, List, SortAsc } from 'lucide-react';
import { products } from '../data/mockData';
import { Product, FilterOptions } from '../types';
import ProductGrid from '../components/ProductGrid';
import FilterSidebar from '../components/FilterSidebar';

const Products: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filteredProducts, setFilteredProducts] = useState<Product[]>(products);
  const [filters, setFilters] = useState<FilterOptions>({});
  const [sortBy, setSortBy] = useState<string>('popularity');
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  const searchQuery = searchParams.get('q') || '';
  const categoryParam = searchParams.get('category') || '';

  useEffect(() => {
    if (categoryParam) {
      setFilters(prev => ({ ...prev, category: categoryParam }));
    }
  }, [categoryParam]);

  useEffect(() => {
    setLoading(true);
    
    // Simulate API delay
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
        filtered = filtered.filter(product => product.category === filters.category);
      }

      // Price range filter
      if (filters.priceRange) {
        filtered = filtered.filter(product =>
          product.price >= filters.priceRange![0] && product.price <= filters.priceRange![1]
        );
      }

      // Brand filter
      if (filters.brand) {
        filtered = filtered.filter(product => product.brand === filters.brand);
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
          // Assuming newer products have higher IDs
          filtered.sort((a, b) => parseInt(b.id) - parseInt(a.id));
          break;
        case 'popularity':
        default:
          filtered.sort((a, b) => b.reviewCount - a.reviewCount);
          break;
      }

      setFilteredProducts(filtered);
      setLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, [searchQuery, filters, sortBy]);

  const handleFiltersChange = (newFilters: FilterOptions) => {
    setFilters(newFilters);
  };

  const handleSortChange = (newSortBy: string) => {
    setSortBy(newSortBy);
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
            {searchQuery ? `Search Results for "${searchQuery}"` : 'All Products'}
          </h1>
          <p className="text-gray-600 dark:text-gray-300">
            {filteredProducts.length} products found
          </p>
        </div>

        {/* Controls */}
        <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center mb-8 space-y-4 lg:space-y-0">
          <button
            onClick={() => setIsFilterOpen(true)}
            className="lg:hidden flex items-center space-x-2 bg-white dark:bg-gray-800 px-4 py-2 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700"
          >
            <Filter className="h-4 w-4" />
            <span>Filters</span>
          </button>

          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2">
              <SortAsc className="h-4 w-4 text-gray-500" />
              <span className="text-sm font-medium text-gray-700 dark:text-gray-300">Sort by:</span>
              <select
                value={sortBy}
                onChange={(e) => handleSortChange(e.target.value)}
                className="bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
              >
                <option value="popularity">Popularity</option>
                <option value="price-low">Price: Low to High</option>
                <option value="price-high">Price: High to Low</option>
                <option value="rating">Highest Rated</option>
                <option value="newest">Newest</option>
              </select>
            </div>
          </div>
        </div>

        {/* Main Content */}
        <div className="flex flex-col lg:flex-row gap-8">
          {/* Sidebar */}
          <div className="lg:w-64 flex-shrink-0">
            <FilterSidebar
              filters={filters}
              onFiltersChange={handleFiltersChange}
              isOpen={isFilterOpen}
              onClose={() => setIsFilterOpen(false)}
            />
          </div>

          {/* Products Grid */}
          <div className="flex-1">
            <ProductGrid products={filteredProducts} loading={loading} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Products;