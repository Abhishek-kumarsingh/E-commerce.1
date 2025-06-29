/// <reference types="vitest" />
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  // Define environment variables with defaults to ensure they're always available
  define: {
    'import.meta.env.VITE_CLERK_PUBLISHABLE_KEY': 
      JSON.stringify(process.env.VITE_CLERK_PUBLISHABLE_KEY || 'pk_test_c2luY2VyZS1yYXR0bGVyLTgwLmNsZXJrLmFjY291bnRzLmRldiQ'),
    'import.meta.env.VITE_API_BASE_URL': 
      JSON.stringify(process.env.VITE_API_BASE_URL || 'https://your-backend-url.herokuapp.com/api'),
  },
  build: {
    outDir: 'dist',
    sourcemap: false,
    minify: 'terser',
    rollupOptions: {
      input: {
        main: path.resolve(__dirname, 'index.html')
      },
      output: {
        format: 'es',
        chunkFileNames: 'assets/[name]-[hash].js',
        assetFileNames: 'assets/[name]-[hash][extname]'
      }
    }
  },
  server: {
    port: 3000,
    open: true
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts']
  }
});
