import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Star, Heart, ShoppingCart, Eye, Zap, Sparkles } from 'lucide-react';
import { Product } from '../types';
import { useCartStore } from '../store/cartStore';
import { motion, AnimatePresence } from 'framer-motion';
import toast from 'react-hot-toast';

interface ProductCardProps {
  product: Product;
  index?: number;
  viewMode?: 'grid' | 'list';
}

const ProductCard: React.FC<ProductCardProps> = ({ product, index = 0, viewMode = 'grid' }) => {
  const [isHovered, setIsHovered] = useState(false);
  const [isWishlisted, setIsWishlisted] = useState(false);
  const [imageLoaded, setImageLoaded] = useState(false);
  const { addItem } = useCartStore();

  const handleAddToCart = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    addItem(product);
    toast.success('Added to cart!', {
      icon: '🛒',
      duration: 2000,
      style: {
        background: 'rgba(99, 102, 241, 0.1)',
        backdropFilter: 'blur(20px)',
        border: '1px solid rgba(99, 102, 241, 0.2)',
        color: 'rgb(99, 102, 241)',
      },
    });
  };

  const handleWishlist = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsWishlisted(!isWishlisted);
    toast.success(isWishlisted ? 'Removed from wishlist' : 'Added to wishlist!', {
      icon: isWishlisted ? '💔' : '❤️',
      duration: 2000,
    });
  };

  const renderStars = (rating: number) => {
    return Array.from({ length: 5 }, (_, i) => (
      <Star
        key={i}
        className={`h-3.5 w-3.5 transition-colors duration-200 ${
          i < Math.floor(rating)
            ? 'text-yellow-400 fill-current'
            : 'text-neutral-300 dark:text-neutral-600'
        }`}
      />
    ));
  };

  const discountPercentage = product.originalPrice
    ? Math.round(((product.originalPrice - product.price) / product.originalPrice) * 100)
    : 0;

  const isListView = viewMode === 'list';

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, delay: index * 0.1 }}
      whileHover={{ y: isListView ? 0 : -8, scale: isListView ? 1.01 : 1.02 }}
      onHoverStart={() => setIsHovered(true)}
      onHoverEnd={() => setIsHovered(false)}
      className={`group relative ${
        isListView ? 'flex flex-row' : 'h-full w-full'
      }`}
    >
      <div className={`card-interactive overflow-hidden backdrop-blur-sm h-full w-full ${
        isListView ? 'flex flex-row' : ''
      }`}>
        <Link to={`/product/${product.id}`} className={`block h-full w-full ${
          isListView ? 'flex flex-row' : ''
        }`}>
          {/* Image container with enhanced effects */}
          <div className={`relative overflow-hidden bg-neutral-100 dark:bg-neutral-800 ${
            isListView 
              ? 'w-48 h-32 flex-shrink-0 rounded-l-2xl' 
              : 'rounded-t-2xl h-60 w-full'
          }`}>
            {/* Loading skeleton */}
            {!imageLoaded && (
              <div className={`absolute inset-0 loading-shimmer ${
                isListView ? 'rounded-l-2xl' : 'rounded-t-2xl'
              }`}></div>
            )}

            <motion.img
              src={product.image}
              alt={product.name}
              className={`w-full h-full object-cover transition-all duration-500 ${
                imageLoaded ? 'opacity-100' : 'opacity-0'
              } ${isHovered && !isListView ? 'scale-110' : 'scale-100'}`}
              onLoad={() => setImageLoaded(true)}
              loading="lazy"
            />

            {/* Gradient overlay on hover */}
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: isHovered ? 1 : 0 }}
              transition={{ duration: 0.3 }}
              className="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent"
            />

            {/* Badges and labels */}
            <div className="absolute top-3 left-3 flex flex-col space-y-2">
              {discountPercentage > 0 && (
                <motion.div
                  initial={{ scale: 0, rotate: -12 }}
                  animate={{ scale: 1, rotate: -12 }}
                  transition={{ delay: 0.2, type: "spring", stiffness: 500 }}
                  className="bg-gradient-to-r from-red-500 to-pink-500 text-white px-3 py-1 rounded-full text-xs font-bold shadow-lg flex items-center space-x-1"
                >
                  <Zap className="h-3 w-3" />
                  <span>{discountPercentage}% OFF</span>
                </motion.div>
              )}

              {product.featured && (
                <motion.div
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  transition={{ delay: 0.3, type: "spring", stiffness: 500 }}
                  className="bg-gradient-to-r from-primary-500 to-secondary-500 text-white px-3 py-1 rounded-full text-xs font-bold shadow-lg flex items-center space-x-1"
                >
                  <Sparkles className="h-3 w-3" />
                  <span>Featured</span>
                </motion.div>
              )}

              {!product.inStock && (
                <div className="bg-neutral-900/80 text-white px-3 py-1 rounded-full text-xs font-bold backdrop-blur-sm">
                  Out of Stock
                </div>
              )}
            </div>

            {/* Action buttons */}
            <div className="absolute top-3 right-3 flex flex-col space-y-2">
              <AnimatePresence>
                {isHovered && (
                  <>
                    <motion.button
                      initial={{ scale: 0, opacity: 0 }}
                      animate={{ scale: 1, opacity: 1 }}
                      exit={{ scale: 0, opacity: 0 }}
                      transition={{ delay: 0.1 }}
                      onClick={handleWishlist}
                      className={`p-3 rounded-full backdrop-blur-sm border transition-all duration-300 ${
                        isWishlisted
                          ? 'bg-red-500 border-red-500 text-white'
                          : 'bg-white/90 dark:bg-neutral-900/90 border-white/20 dark:border-neutral-700/20 text-neutral-700 dark:text-neutral-300 hover:bg-red-50 dark:hover:bg-red-900/20 hover:text-red-500'
                      }`}
                    >
                      <Heart className={`h-4 w-4 ${isWishlisted ? 'fill-current' : ''}`} />
                    </motion.button>

                    <motion.button
                      initial={{ scale: 0, opacity: 0 }}
                      animate={{ scale: 1, opacity: 1 }}
                      exit={{ scale: 0, opacity: 0 }}
                      transition={{ delay: 0.2 }}
                      className="p-3 rounded-full bg-white/90 dark:bg-neutral-900/90 backdrop-blur-sm border border-white/20 dark:border-neutral-700/20 text-neutral-700 dark:text-neutral-300 hover:bg-primary-50 dark:hover:bg-primary-900/20 hover:text-primary-500 transition-all duration-300"
                    >
                      <Eye className="h-4 w-4" />
                    </motion.button>
                  </>
                )}
              </AnimatePresence>
            </div>

            {/* Quick add to cart on hover */}
            <AnimatePresence>
              {isHovered && product.inStock && !isListView && (
                <motion.div
                  initial={{ y: 100, opacity: 0 }}
                  animate={{ y: 0, opacity: 1 }}
                  exit={{ y: 100, opacity: 0 }}
                  transition={{ duration: 0.3 }}
                  className="absolute bottom-3 left-3 right-3"
                >
                  <motion.button
                    onClick={handleAddToCart}
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                    className="w-full btn-primary py-3 text-sm font-medium backdrop-blur-sm"
                  >
                    <ShoppingCart className="h-4 w-4 mr-2" />
                    Quick Add to Cart
                  </motion.button>
                </motion.div>
              )}
            </AnimatePresence>
          </div>

          {/* Product information */}
          <div className={`space-y-3 ${
            isListView 
              ? 'p-6 flex-1 flex flex-col justify-between' 
              : 'p-5 flex-1 flex flex-col justify-between w-full'
          }`}>
            {/* Brand and rating */}
            <div className="flex items-center justify-between">
              <motion.span
                className="text-xs font-bold text-primary-600 dark:text-primary-400 uppercase tracking-wider px-2 py-1 bg-primary-50 dark:bg-primary-900/20 rounded-full"
                whileHover={{ scale: 1.05 }}
              >
                {product.brand}
              </motion.span>
              <div className="flex items-center space-x-2">
                <div className="flex items-center space-x-1">
                  {renderStars(product.rating)}
                </div>
                <span className="text-xs text-neutral-500 dark:text-neutral-400 font-medium">
                  ({product.reviewCount})
                </span>
              </div>
            </div>

            {/* Product name */}
            <h3 className={`font-bold text-neutral-900 dark:text-neutral-100 line-clamp-2 group-hover:text-primary-600 dark:group-hover:text-primary-400 transition-colors duration-300 leading-tight min-h-[2.75rem] flex items-center ${
              isListView ? 'text-xl' : 'text-lg'
            }`}>
              {product.name}
            </h3>

            {/* Description */}
            <p className={`text-neutral-600 dark:text-neutral-400 line-clamp-2 leading-relaxed min-h-[2.75rem] ${
              isListView ? 'text-base' : 'text-sm'
            }`}>
              {product.description}
            </p>

            {/* Price and action */}
            <div className={`flex items-center justify-between min-h-[3.5rem] ${
              isListView ? 'pt-2' : 'pt-1'
            }`}>
              <div className="flex items-center space-x-3">
                <span className={`font-bold text-neutral-900 dark:text-neutral-100 ${
                  isListView ? 'text-2xl' : 'text-xl'
                }`}>
                  ${product.price}
                </span>
                {product.originalPrice && (
                  <div className="flex flex-col">
                    <span className="text-sm text-neutral-500 dark:text-neutral-400 line-through">
                      ${product.originalPrice}
                    </span>
                    <span className="text-xs text-accent-600 dark:text-accent-400 font-medium">
                      Save ${(product.originalPrice - product.price).toFixed(2)}
                    </span>
                  </div>
                )}
              </div>

              {/* Desktop add to cart button */}
              <motion.button
                onClick={handleAddToCart}
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                disabled={!product.inStock}
                className={`hidden md:flex items-center space-x-2 px-4 py-2 rounded-xl font-medium text-sm transition-all duration-300 ${
                  product.inStock
                    ? 'bg-gradient-to-r from-primary-600 to-secondary-600 text-white hover:from-primary-700 hover:to-secondary-700 shadow-lg hover:shadow-glow'
                    : 'bg-neutral-200 dark:bg-neutral-700 text-neutral-500 dark:text-neutral-400 cursor-not-allowed'
                }`}
              >
                <ShoppingCart className="h-4 w-4" />
                <span>{product.inStock ? 'Add' : 'Sold Out'}</span>
              </motion.button>
            </div>

            {/* Stock status */}
            {!product.inStock && (
              <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                className="flex items-center justify-center py-2 px-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-xl"
              >
                <span className="text-sm font-medium text-red-600 dark:text-red-400">
                  Currently Out of Stock
                </span>
              </motion.div>
            )}

            {/* Mobile add to cart button */}
            <motion.button
              onClick={handleAddToCart}
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              disabled={!product.inStock}
              className={`md:hidden w-full flex items-center justify-center space-x-2 py-2 rounded-xl font-medium transition-all duration-300 h-[2.75rem] ${
                product.inStock
                  ? 'bg-gradient-to-r from-primary-600 to-secondary-600 text-white hover:from-primary-700 hover:to-secondary-700'
                  : 'bg-neutral-200 dark:bg-neutral-700 text-neutral-500 dark:text-neutral-400 cursor-not-allowed'
              }`}
            >
              <ShoppingCart className="h-4 w-4" />
              <span>{product.inStock ? 'Add to Cart' : 'Out of Stock'}</span>
            </motion.button>
          </div>
        </Link>
      </div>
    </motion.div>
  );
};

export default ProductCard;