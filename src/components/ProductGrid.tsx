import React from 'react';
import { Product } from '../types';
import ProductCard from './ProductCard';
import { motion } from 'framer-motion';

interface ProductGridProps {
  products: Product[];
  loading?: boolean;
  viewMode?: 'grid' | 'list';
}

const ProductGrid: React.FC<ProductGridProps> = ({ products, loading = false, viewMode = 'grid' }) => {
  if (loading) {
    return (
      <div className={`grid gap-6 ${
        viewMode === 'list' 
          ? 'grid-cols-1' 
          : 'grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5'
      }`}>
        {Array.from({ length: 8 }).map((_, index) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: index * 0.1 }}
            className={`card overflow-hidden ${
              viewMode === 'list' ? 'flex flex-row' : ''
            }`}
          >
            <div className={`loading-shimmer rounded-t-2xl ${
              viewMode === 'list' ? 'w-48 h-32' : 'h-64'
            }`}></div>
            <div className="p-6 space-y-4 flex-1">
              <div className="flex justify-between items-center">
                <div className="loading-shimmer h-6 rounded-full w-20"></div>
                <div className="loading-shimmer h-4 rounded w-16"></div>
              </div>
              <div className="loading-shimmer h-6 rounded w-3/4"></div>
              <div className="loading-shimmer h-4 rounded w-full"></div>
              <div className="loading-shimmer h-4 rounded w-2/3"></div>
              <div className="flex justify-between items-center pt-2">
                <div className="loading-shimmer h-8 rounded w-1/3"></div>
                <div className="loading-shimmer h-10 rounded-xl w-1/4"></div>
              </div>
            </div>
          </motion.div>
        ))}
      </div>
    );
  }

  if (products.length === 0) {
    return (
      <div className="text-center py-12">
        <div className="text-gray-500 dark:text-gray-400 text-lg">
          No products found matching your criteria.
        </div>
      </div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.5 }}
      className={`grid gap-6 ${
        viewMode === 'list' 
          ? 'grid-cols-1' 
          : 'grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5'
      }`}
    >
      {products.map((product, index) => (
        <ProductCard 
          key={product.id} 
          product={product} 
          index={index} 
          viewMode={viewMode}
        />
      ))}
    </motion.div>
  );
};

export default ProductGrid;