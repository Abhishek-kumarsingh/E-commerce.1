import React, { useEffect, useState } from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import Header from './Header';
import Footer from './Footer';
import CartSidebar from './CartSidebar';
import { useThemeStore } from '../store/themeStore';
import { Toaster } from 'react-hot-toast';
import { motion, AnimatePresence } from 'framer-motion';
import { ArrowUp } from 'lucide-react';

const Layout: React.FC = () => {
  const { isDark, setTheme } = useThemeStore();
  const [showScrollTop, setShowScrollTop] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const location = useLocation();

  useEffect(() => {
    // Initialize theme on app start
    const savedTheme = localStorage.getItem('theme-storage');
    if (savedTheme) {
      const { state } = JSON.parse(savedTheme);
      setTheme(state.isDark);
    }
  }, [setTheme]);

  // Handle scroll to top button
  useEffect(() => {
    const handleScroll = () => {
      setShowScrollTop(window.scrollY > 400);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  // Handle route changes with loading state
  useEffect(() => {
    setIsLoading(true);
    const timer = setTimeout(() => setIsLoading(false), 300);
    return () => clearTimeout(timer);
  }, [location.pathname]);

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className={`min-h-screen ${isDark ? 'dark' : ''}`}>
      {/* Page loading indicator */}
      <AnimatePresence>
        {isLoading && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="fixed top-0 left-0 right-0 z-50 h-1 bg-gradient-to-r from-primary-600 to-secondary-600"
          >
            <motion.div
              initial={{ x: '-100%' }}
              animate={{ x: '100%' }}
              transition={{ duration: 0.8, ease: 'easeInOut' }}
              className="h-full w-full bg-gradient-to-r from-transparent via-white to-transparent"
            />
          </motion.div>
        )}
      </AnimatePresence>

      <Header />

      {/* Main content with page transitions */}
      <main className="relative">
        <AnimatePresence mode="wait">
          <motion.div
            key={location.pathname}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            transition={{ duration: 0.3, ease: 'easeInOut' }}
          >
            <Outlet />
          </motion.div>
        </AnimatePresence>
      </main>

      <Footer />

      <CartSidebar />

      {/* Scroll to top button */}
      <AnimatePresence>
        {showScrollTop && (
          <motion.button
            initial={{ opacity: 0, scale: 0.8, y: 20 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0.8, y: 20 }}
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={scrollToTop}
            className="fixed bottom-8 right-8 z-40 p-4 bg-gradient-to-r from-primary-600 to-secondary-600 text-white rounded-full shadow-hard hover:shadow-glow-lg transition-all duration-300 backdrop-blur-sm"
          >
            <ArrowUp className="h-6 w-6" />
          </motion.button>
        )}
      </AnimatePresence>

      {/* Enhanced Toast notifications */}
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 4000,
          style: {
            background: 'rgba(255, 255, 255, 0.1)',
            backdropFilter: 'blur(20px)',
            border: '1px solid rgba(255, 255, 255, 0.2)',
            borderRadius: '16px',
            color: isDark ? '#ffffff' : '#000000',
            padding: '16px 20px',
            fontSize: '14px',
            fontWeight: '500',
            boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.37)',
          },
          success: {
            duration: 3000,
            style: {
              background: 'rgba(16, 185, 129, 0.1)',
              border: '1px solid rgba(16, 185, 129, 0.2)',
              color: '#10b981',
            },
            iconTheme: {
              primary: '#10b981',
              secondary: '#ffffff',
            },
          },
          error: {
            duration: 5000,
            style: {
              background: 'rgba(239, 68, 68, 0.1)',
              border: '1px solid rgba(239, 68, 68, 0.2)',
              color: '#ef4444',
            },
            iconTheme: {
              primary: '#ef4444',
              secondary: '#ffffff',
            },
          },
          loading: {
            style: {
              background: 'rgba(99, 102, 241, 0.1)',
              border: '1px solid rgba(99, 102, 241, 0.2)',
              color: '#6366f1',
            },
          },
        }}
      />
    </div>
  );
};

export default Layout;