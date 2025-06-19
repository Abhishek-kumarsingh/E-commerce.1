import React, { useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import Header from './Header';
import CartSidebar from './CartSidebar';
import { useThemeStore } from '../store/themeStore';
import { Toaster } from 'react-hot-toast';

const Layout: React.FC = () => {
  const { isDark, setTheme } = useThemeStore();

  useEffect(() => {
    // Initialize theme on app start
    const savedTheme = localStorage.getItem('theme-storage');
    if (savedTheme) {
      const { state } = JSON.parse(savedTheme);
      setTheme(state.isDark);
    }
  }, [setTheme]);

  return (
    <div className={`min-h-screen ${isDark ? 'dark' : ''}`}>
      <Header />
      <main>
        <Outlet />
      </main>
      <CartSidebar />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3000,
          style: {
            background: isDark ? '#1f2937' : '#ffffff',
            color: isDark ? '#ffffff' : '#000000',
          },
        }}
      />
    </div>
  );
};

export default Layout;