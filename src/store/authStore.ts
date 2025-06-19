import { create } from 'zustand';
import { persist, subscribeWithSelector } from 'zustand/middleware';
import { User, LoginForm, RegisterForm } from '../types';
import { userService } from '../services/userService';
import { authToken } from '../lib/api';
import toast from 'react-hot-toast';

interface AuthState {
  // State
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;

  // Actions
  login: (credentials: LoginForm) => Promise<boolean>;
  register: (userData: RegisterForm) => Promise<boolean>;
  logout: () => Promise<void>;
  loadUser: () => Promise<void>;
  updateUser: (userData: Partial<User>) => Promise<void>;
  clearError: () => void;

  // Getters
  isAdmin: () => boolean;
  hasPermission: (permission: string) => boolean;
}

export const useAuthStore = create<AuthState>()(
  subscribeWithSelector(
    persist(
      (set, get) => ({
        // Initial state
        user: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,

        // Login user
        login: async (credentials: LoginForm): Promise<boolean> => {
          set({ isLoading: true, error: null });

          try {
            // For now, we'll use mock authentication
            // In production, this would call your backend API
            await new Promise(resolve => setTimeout(resolve, 1000));

            // Mock authentication logic
            if (credentials.email === 'admin@ecommerce.com' && credentials.password === 'admin123') {
              const mockUser: User = {
                id: '1',
                email: credentials.email,
                name: 'Admin User',
                firstName: 'Admin',
                lastName: 'User',
                isAdmin: true,
                isVerified: true,
                addresses: [],
                wishlist: [],
                preferences: {
                  currency: 'USD',
                  language: 'en',
                  notifications: {
                    email: true,
                    sms: false,
                    push: true,
                  },
                  theme: 'light',
                },
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString(),
              };

              // Mock JWT token
              const mockToken = 'mock-jwt-token-admin';
              authToken.set(mockToken);

              set({
                user: mockUser,
                isAuthenticated: true,
                isLoading: false
              });

              toast.success('Welcome back!');
              return true;
            } else if (credentials.email && credentials.password) {
              const mockUser: User = {
                id: '2',
                email: credentials.email,
                name: credentials.email.split('@')[0],
                firstName: credentials.email.split('@')[0],
                lastName: 'User',
                isAdmin: false,
                isVerified: true,
                addresses: [],
                wishlist: [],
                preferences: {
                  currency: 'USD',
                  language: 'en',
                  notifications: {
                    email: true,
                    sms: false,
                    push: true,
                  },
                  theme: 'light',
                },
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString(),
              };

              const mockToken = 'mock-jwt-token-user';
              authToken.set(mockToken);

              set({
                user: mockUser,
                isAuthenticated: true,
                isLoading: false
              });

              toast.success('Welcome back!');
              return true;
            } else {
              throw new Error('Invalid credentials');
            }
          } catch (error: any) {
            const errorMessage = error.response?.data?.message || error.message || 'Login failed';
            set({
              error: errorMessage,
              isLoading: false,
              isAuthenticated: false,
              user: null
            });
            toast.error(errorMessage);
            return false;
          }
        },

        // Register new user
        register: async (userData: RegisterForm): Promise<boolean> => {
          set({ isLoading: true, error: null });

          try {
            // Validate passwords match
            if (userData.password !== userData.confirmPassword) {
              throw new Error('Passwords do not match');
            }

            // Mock registration
            await new Promise(resolve => setTimeout(resolve, 1000));

            const mockUser: User = {
              id: Date.now().toString(),
              email: userData.email,
              name: `${userData.firstName} ${userData.lastName}`,
              firstName: userData.firstName,
              lastName: userData.lastName,
              isAdmin: false,
              isVerified: false,
              addresses: [],
              wishlist: [],
              preferences: {
                currency: 'USD',
                language: 'en',
                notifications: {
                  email: true,
                  sms: false,
                  push: true,
                },
                theme: 'light',
              },
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString(),
            };

            const mockToken = 'mock-jwt-token-new-user';
            authToken.set(mockToken);

            set({
              user: mockUser,
              isAuthenticated: true,
              isLoading: false
            });

            toast.success('Account created successfully!');
            return true;
          } catch (error: any) {
            const errorMessage = error.response?.data?.message || error.message || 'Registration failed';
            set({
              error: errorMessage,
              isLoading: false,
              isAuthenticated: false,
              user: null
            });
            toast.error(errorMessage);
            return false;
          }
        },

        // Logout user
        logout: async () => {
          try {
            authToken.remove();
            set({
              user: null,
              isAuthenticated: false,
              error: null
            });
            toast.success('Logged out successfully');
          } catch (error) {
            console.error('Logout error:', error);
          }
        },

        // Load user profile
        loadUser: async () => {
          const token = authToken.get();
          if (!token || !authToken.isValid()) {
            set({ isAuthenticated: false, user: null });
            return;
          }

          set({ isLoading: true });
          try {
            const response = await userService.getProfile();
            if (response.success) {
              set({
                user: response.data,
                isAuthenticated: true,
                isLoading: false
              });
            }
          } catch (error) {
            console.error('Failed to load user:', error);
            authToken.remove();
            set({
              isAuthenticated: false,
              user: null,
              isLoading: false
            });
          }
        },

        // Update user profile
        updateUser: async (userData: Partial<User>) => {
          const currentUser = get().user;
          if (!currentUser) return;

          set({ isLoading: true });
          try {
            const response = await userService.updateProfile(userData);
            if (response.success) {
              set({
                user: response.data,
                isLoading: false
              });
              toast.success('Profile updated successfully');
            }
          } catch (error: any) {
            const errorMessage = error.response?.data?.message || 'Failed to update profile';
            set({
              error: errorMessage,
              isLoading: false
            });
            toast.error(errorMessage);
          }
        },

        // Clear error
        clearError: () => {
          set({ error: null });
        },

        // Check if user is admin
        isAdmin: () => {
          return get().user?.isAdmin || false;
        },

        // Check user permissions
        hasPermission: (permission: string) => {
          const user = get().user;
          if (!user) return false;

          // Admin has all permissions
          if (user.isAdmin) return true;

          // Add more granular permission logic here
          return false;
        },
      }),
      {
        name: 'auth-storage',
        partialize: (state) => ({
          user: state.user,
          isAuthenticated: state.isAuthenticated
        }),
      }
    )
  )
);