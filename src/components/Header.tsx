import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import {
  Search,
  ShoppingCart,
  User,
  Menu,
  X,
  Moon,
  Sun,
  Heart,
  Package,
  Bell,
  CreditCard,
  UserCircle,
  Grid3X3,
  Sparkles,
  TrendingUp
} from 'lucide-react';
import { useCartStore } from '../store/cartStore';
import { useThemeStore } from '../store/themeStore';
import { categories } from '../data/mockData';
import { motion, AnimatePresence } from 'framer-motion';
import { SignedIn, SignedOut, SignInButton, SignUpButton, UserButton, useUser } from '@clerk/clerk-react';

const Header: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const searchRef = useRef<HTMLInputElement>(null);
  const userMenuRef = useRef<HTMLDivElement>(null);

  const { getTotalItems, toggleCart } = useCartStore();
  const { isDark, toggleTheme } = useThemeStore();
  const userClerk = useUser();

  // Handle scroll effect for glassmorphism
  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 20);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  // Close menus when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (userMenuRef.current && !userMenuRef.current.contains(event.target as Node)) {
        setIsUserMenuOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Close mobile menu on route change
  useEffect(() => {
    setIsMenuOpen(false);
  }, [location.pathname]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery)}`);
      setSearchQuery('');
      searchRef.current?.blur();
    }
  };

  

  return (
    <>
      {/* Top banner with modern gradient */}
      <motion.div
        initial={{ y: -50, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        className="bg-gradient-to-r from-primary-600 via-primary-700 to-secondary-600 text-white text-center py-3 relative overflow-hidden"
      >
        <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent animate-gradient"></div>
        <div className="relative flex items-center justify-center space-x-2">
          <Sparkles className="h-4 w-4 animate-pulse" />
          <p className="text-sm font-medium">Free shipping on orders over $50 | 30-day returns | New arrivals weekly!</p>
          <TrendingUp className="h-4 w-4 animate-bounce-gentle" />
        </div>
      </motion.div>

      {/* Main header with glassmorphism effect */}
      <motion.header
        className={`sticky top-0 z-50 transition-all duration-500 ${
          scrolled
            ? 'glass backdrop-blur-glass border-b border-white/20 dark:border-black/20'
            : 'bg-white/95 dark:bg-neutral-950/95 backdrop-blur-sm border-b border-neutral-200/50 dark:border-neutral-800/50'
        }`}
        initial={{ y: -100 }}
        animate={{ y: 0 }}
        transition={{ duration: 0.6, ease: "easeOut" }}
      >
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-20">
            {/* Logo with enhanced design */}
            <motion.div
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              <Link to="/" className="flex items-center space-x-3 flex-shrink-0 group">
                <div className="relative">
                  <div className="absolute inset-0 bg-gradient-to-r from-primary-600 to-secondary-600 rounded-xl blur-lg opacity-30 group-hover:opacity-50 transition-opacity"></div>
                  <div className="relative bg-gradient-to-r from-primary-600 to-secondary-600 p-2 rounded-xl">
                    <Package className="h-8 w-8 text-white" />
                  </div>
                </div>
                <div className="flex flex-col">
                  <span className="text-2xl font-bold gradient-text">ShopZone</span>
                  <span className="text-xs text-neutral-500 dark:text-neutral-400 font-medium">Premium Store</span>
                </div>
              </Link>
            </motion.div>

            {/* Enhanced Search bar */}
            <form onSubmit={handleSearch} className="hidden md:flex flex-1 max-w-2xl mx-8">
              <motion.div
                className="relative w-full"
                whileFocus={{ scale: 1.02 }}
                transition={{ duration: 0.2 }}
              >
                <div className={`relative transition-all duration-300 ${
                  isSearchFocused ? 'transform scale-105' : ''
                }`}>
                  <input
                    ref={searchRef}
                    type="text"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    onFocus={() => setIsSearchFocused(true)}
                    onBlur={() => setIsSearchFocused(false)}
                    placeholder="Search for products, brands, categories..."
                    className="input w-full pl-12 pr-24 py-4 text-base bg-white/80 dark:bg-neutral-900/80 backdrop-blur-sm border-2 border-neutral-200/50 dark:border-neutral-700/50 focus:border-primary-500 focus:bg-white dark:focus:bg-neutral-900 transition-all duration-300"
                  />
                  <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                  <motion.button
                    type="submit"
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    className="absolute right-2 top-1/2 transform -translate-y-1/2 btn-primary px-4 py-2 text-sm font-medium"
                  >
                    Search
                  </motion.button>
                </div>
              </motion.div>
            </form>

            {/* Right section with modern action buttons */}
            <div className="flex items-center space-x-2">
              {/* Theme toggle with enhanced design */}
              <motion.button
                onClick={toggleTheme}
                whileHover={{ scale: 1.1, rotate: 180 }}
                whileTap={{ scale: 0.9 }}
                className="relative p-3 rounded-xl bg-neutral-100/80 dark:bg-neutral-800/80 backdrop-blur-sm text-neutral-600 dark:text-neutral-300 hover:bg-neutral-200/80 dark:hover:bg-neutral-700/80 transition-all duration-300 group"
              >
                <AnimatePresence mode="wait">
                  {isDark ? (
                    <motion.div
                      key="sun"
                      initial={{ rotate: -180, opacity: 0 }}
                      animate={{ rotate: 0, opacity: 1 }}
                      exit={{ rotate: 180, opacity: 0 }}
                      transition={{ duration: 0.3 }}
                    >
                      <Sun className="h-5 w-5" />
                    </motion.div>
                  ) : (
                    <motion.div
                      key="moon"
                      initial={{ rotate: -180, opacity: 0 }}
                      animate={{ rotate: 0, opacity: 1 }}
                      exit={{ rotate: 180, opacity: 0 }}
                      transition={{ duration: 0.3 }}
                    >
                      <Moon className="h-5 w-5" />
                    </motion.div>
                  )}
                </AnimatePresence>
              </motion.button>

              {/* Notifications */}
              <motion.button
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
                className="hidden md:flex relative p-3 rounded-xl bg-neutral-100/80 dark:bg-neutral-800/80 backdrop-blur-sm text-neutral-600 dark:text-neutral-300 hover:bg-neutral-200/80 dark:hover:bg-neutral-700/80 transition-all duration-300"
              >
                <Bell className="h-5 w-5" />
                <span className="absolute -top-1 -right-1 bg-accent-500 text-white text-xs rounded-full h-4 w-4 flex items-center justify-center animate-pulse">
                  3
                </span>
              </motion.button>

              {/* Wishlist with heart animation */}
              <motion.button
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
                className="hidden md:flex p-3 rounded-xl bg-neutral-100/80 dark:bg-neutral-800/80 backdrop-blur-sm text-neutral-600 dark:text-neutral-300 hover:bg-neutral-200/80 dark:hover:bg-neutral-700/80 hover:text-red-500 transition-all duration-300 group"
              >
                <Heart className="h-5 w-5 group-hover:fill-current transition-all duration-300" />
              </motion.button>

              {/* Enhanced Cart */}
              <motion.button
                onClick={toggleCart}
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
                className="relative p-3 rounded-xl bg-gradient-to-r from-primary-500/20 to-secondary-500/20 backdrop-blur-sm text-primary-600 dark:text-primary-400 hover:from-primary-500/30 hover:to-secondary-500/30 transition-all duration-300 group"
              >
                <ShoppingCart className="h-5 w-5 group-hover:animate-bounce-gentle" />
                <AnimatePresence>
                  {getTotalItems() > 0 && (
                    <motion.span
                      initial={{ scale: 0, opacity: 0 }}
                      animate={{ scale: 1, opacity: 1 }}
                      exit={{ scale: 0, opacity: 0 }}
                      className="absolute -top-2 -right-2 bg-gradient-to-r from-primary-600 to-secondary-600 text-white text-xs rounded-full h-6 w-6 flex items-center justify-center font-bold shadow-lg"
                    >
                      {getTotalItems()}
                    </motion.span>
                  )}
                </AnimatePresence>
              </motion.button>

              {/* User menu with Clerk */}
              <div className="relative" ref={userMenuRef}>
                <SignedIn>
                  <UserButton afterSignOutUrl="/" />
                </SignedIn>
                <SignedOut>
                  <div className="flex space-x-2">
                    <motion.div
                      whileHover={{ scale: 1.05 }}
                      whileTap={{ scale: 0.95 }}
                      className="rounded-xl bg-neutral-100/80 dark:bg-neutral-800/80 backdrop-blur-sm hover:bg-neutral-200/80 dark:hover:bg-neutral-700/80 transition-all duration-300"
                    >
                      <SignInButton mode="modal">
                        <button className="flex items-center space-x-2 p-3 text-sm font-medium text-neutral-700 dark:text-neutral-300">
                          <UserCircle className="h-5 w-5" />
                          <span className="hidden md:block">Sign In</span>
                        </button>
                      </SignInButton>
                    </motion.div>
                    
                    <motion.div
                      whileHover={{ scale: 1.05 }}
                      whileTap={{ scale: 0.95 }}
                      className="rounded-xl bg-primary-500/20 backdrop-blur-sm hover:bg-primary-500/30 transition-all duration-300"
                    >
                      <SignUpButton mode="modal">
                        <button className="flex items-center space-x-2 p-3 text-sm font-medium text-primary-600 dark:text-primary-400">
                          <User className="h-5 w-5" />
                          <span className="hidden md:block">Sign Up</span>
                        </button>
                      </SignUpButton>
                    </motion.div>
                  </div>
                </SignedOut>
              </div>

              {/* Mobile menu button */}
              <motion.button
                onClick={() => setIsMenuOpen(!isMenuOpen)}
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
                className="md:hidden p-3 rounded-xl bg-neutral-100/80 dark:bg-neutral-800/80 backdrop-blur-sm text-neutral-600 dark:text-neutral-300 hover:bg-neutral-200/80 dark:hover:bg-neutral-700/80 transition-all duration-300"
              >
                <AnimatePresence mode="wait">
                  {isMenuOpen ? (
                    <motion.div
                      key="close"
                      initial={{ rotate: -90, opacity: 0 }}
                      animate={{ rotate: 0, opacity: 1 }}
                      exit={{ rotate: 90, opacity: 0 }}
                      transition={{ duration: 0.2 }}
                    >
                      <X className="h-5 w-5" />
                    </motion.div>
                  ) : (
                    <motion.div
                      key="menu"
                      initial={{ rotate: -90, opacity: 0 }}
                      animate={{ rotate: 0, opacity: 1 }}
                      exit={{ rotate: 90, opacity: 0 }}
                      transition={{ duration: 0.2 }}
                    >
                      <Menu className="h-5 w-5" />
                    </motion.div>
                  )}
                </AnimatePresence>
              </motion.button>
            </div>
          </div>

          {/* Enhanced Mobile menu */}
          <AnimatePresence>
            {isMenuOpen && (
              <motion.div
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: 'auto' }}
                exit={{ opacity: 0, height: 0 }}
                transition={{ duration: 0.3 }}
                className="md:hidden border-t border-white/20 dark:border-black/20 backdrop-blur-sm"
              >
                <div className="px-4 py-6 space-y-6">
                  {/* Mobile search */}
                  <form onSubmit={handleSearch}>
                    <div className="relative">
                      <input
                        type="text"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        placeholder="Search products..."
                        className="w-full pl-12 pr-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-white/80 dark:bg-neutral-900/80 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
                      />
                      <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                    </div>
                  </form>

                  {/* Quick actions */}
                  <div className="grid grid-cols-2 gap-4">
                    <motion.button
                      whileHover={{ scale: 1.02 }}
                      whileTap={{ scale: 0.98 }}
                      onClick={toggleCart}
                      className="flex items-center justify-center space-x-2 p-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors"
                    >
                      <ShoppingCart className="h-5 w-5" />
                      <span className="font-medium">Cart ({getTotalItems()})</span>
                    </motion.button>
                    <Link
                      to="/checkout"
                      onClick={() => setIsMenuOpen(false)}
                      className="flex items-center justify-center space-x-2 p-3 bg-secondary-600 text-white rounded-lg hover:bg-secondary-700 transition-colors"
                    >
                      <CreditCard className="h-5 w-5" />
                      <span className="font-medium">Checkout</span>
                    </Link>
                  </div>
                  
                  {/* Additional quick links */}
                  <div className="grid grid-cols-2 gap-4">
                    <Link
                      to="/wishlist"
                      onClick={() => setIsMenuOpen(false)}
                      className="flex items-center justify-center space-x-2 p-3 glass rounded-lg hover:bg-white/20 dark:hover:bg-black/20 transition-colors"
                    >
                      <Heart className="h-5 w-5" />
                      <span className="font-medium">Wishlist</span>
                    </Link>
                    <Link
                      to="/orders"
                      onClick={() => setIsMenuOpen(false)}
                      className="flex items-center justify-center space-x-2 p-3 glass rounded-lg hover:bg-white/20 dark:hover:bg-black/20 transition-colors"
                    >
                      <Package className="h-5 w-5" />
                      <span className="font-medium">Orders</span>
                    </Link>
                  </div>

                  {/* Categories */}
                  <div>
                    <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-4">Categories</h3>
                    <div className="grid grid-cols-2 gap-3">
                      {categories.slice(0, 6).map((category, index) => (
                        <motion.div
                          key={category.id}
                          initial={{ opacity: 0, x: -20 }}
                          animate={{ opacity: 1, x: 0 }}
                          transition={{ delay: index * 0.05 }}
                        >
                          <Link
                            to={`/category/${category.slug}`}
                            className="flex flex-col items-center justify-center p-3 bg-white/10 dark:bg-black/10 rounded-lg hover:bg-white/20 dark:hover:bg-black/20 transition-colors"
                            onClick={() => setIsMenuOpen(false)}
                          >
                            <div className="text-sm font-medium text-neutral-700 dark:text-neutral-300">
                              {category.name}
                            </div>
                            <div className="text-xs text-neutral-500 dark:text-neutral-400">
                              {category.productCount} items
                            </div>
                          </Link>
                        </motion.div>
                      ))}
                    </div>
                    <div className="mt-4 text-center">
                      <Link
                        to="/categories"
                        onClick={() => setIsMenuOpen(false)}
                        className="text-primary-600 dark:text-primary-400 text-sm font-medium hover:underline"
                      >
                        View All Categories
                      </Link>
                    </div>
                  </div>
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </div>

        {/* Enhanced Categories bar */}
        <div className="hidden md:block glass border-t border-white/10 dark:border-black/10">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex items-center space-x-1 py-4 overflow-x-auto scrollbar-modern">
              <motion.div
                whileHover={{ scale: 1.05 }}
                className="flex items-center space-x-2 px-4 py-2 rounded-xl bg-gradient-to-r from-primary-500/20 to-secondary-500/20 text-primary-600 dark:text-primary-400 font-medium whitespace-nowrap"
              >
                <Grid3X3 className="h-4 w-4" />
                <span>All Categories</span>
              </motion.div>
              {categories.map((category, index) => (
                <motion.div
                  key={category.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: index * 0.05 }}
                >
                  <Link
                    to={`/category/${category.slug}`}
                    className="block px-4 py-2 rounded-xl text-sm font-medium text-neutral-600 dark:text-neutral-300 hover:text-primary-600 dark:hover:text-primary-400 hover:bg-white/10 dark:hover:bg-black/10 transition-all duration-300 whitespace-nowrap"
                  >
                    {category.name}
                  </Link>
                </motion.div>
              ))}
            </div>
          </div>
        </div>
      </motion.header>
    </>
  );
};

export default Header;