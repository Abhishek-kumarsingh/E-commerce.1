import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { ClerkProvider } from '@clerk/clerk-react';
import App from './App.tsx';
import './index.css';

// Get the Clerk publishable key from environment variables
const PUBLISHABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY;

// Check if we're in development mode
const isDevelopment = import.meta.env.DEV;

// Only throw an error in development mode, use a fallback in production
if (!PUBLISHABLE_KEY && isDevelopment) {
  console.error("Missing Clerk Publishable Key - Authentication features will not work");
}

// Use a wrapper component to conditionally render ClerkProvider
const AppWithAuth = () => {
  // If we have a publishable key, use ClerkProvider
  if (PUBLISHABLE_KEY) {
    return (
      <ClerkProvider publishableKey={PUBLISHABLE_KEY} afterSignOutUrl="/">
        <App />
      </ClerkProvider>
    );
  }
  
  // Otherwise, render the app without authentication
  console.warn("Running without authentication - some features may be limited");
  return <App />;
};

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <AppWithAuth />
  </StrictMode>
);
