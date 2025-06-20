import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import { Link } from 'react-router-dom';
import { Heart, ShoppingCart, Trash2, Star, DollarSign } from 'lucide-react';
import { useAuthStore } from '../store/authStore';
import { useCartStore } from '../store/cartStore';
import { toast } from 'react-hot-toast';

interface WishlistItem {
  id: string;
  name: string;
  price: number;
  originalPrice?: number;
  image: string;
  rating: number;
  reviewCount: number;
  inStock: boolean;
  slug: string;
}

const Wishlist: React.FC = () => {
  const { user } = useAuthStore();
  const { addToCart } = useCartStore();
  const [wishlistItems, setWishlistItems] = useState<WishlistItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchWishlist();
  }, []);

  const fetchWishlist = async () => {
    try {
      setLoading(true);
      // TODO: Replace with actual API call
      // const response = await wishlistService.getUserWishlist();
      // setWishlistItems(response.data);
      
      // Mock data for now
      const mockWishlist: WishlistItem[] = [
        {
          id: '1',
          name: 'Wireless Bluetooth Headphones',
          price: 199.99,
          originalPrice: 249.99,
          image: '/api/placeholder/300/300',
          rating: 4.5,
          reviewCount: 128,
          inStock: true,
          slug: 'wireless-bluetooth-headphones'
        },
        {
          id: '2',
          name: 'Smart Fitness Watch',
          price: 299.99,
          image: '/api/placeholder/300/300',
          rating: 4.8,
          reviewCount: 256,
          inStock: true,
          slug: 'smart-fitness-watch'
        },
        {
          id: '3',
          name: 'Premium Coffee Maker',
          price: 149.99,
          originalPrice: 199.99,
          image: '/api/placeholder/300/300',
          rating: 4.3,
          reviewCount: 89,
          inStock: false,
          slug: 'premium-coffee-maker'
        }
      ];
      setWishlistItems(mockWishlist);
    } catch (error) {
      toast.error('Failed to load wishlist');
    } finally {
      setLoading(false);
    }
  };

  const removeFromWishlist = async (itemId: string) => {
    try {
      // TODO: Replace with actual API call
      // await wishlistService.removeFromWishlist(itemId);
      
      setWishlistItems(prev => prev.filter(item => item.id !== itemId));
      toast.success('Item removed from wishlist');
    } catch (error) {
      toast.error('Failed to remove item from wishlist');
    }
  };

  const handleAddToCart = async (item: WishlistItem) => {
    try {
      await addToCart({
        id: item.id,
        name: item.name,
        price: item.price,
        image: item.image,
        quantity: 1
      });
      toast.success('Item added to cart');
    } catch (error) {
      toast.error('Failed to add item to cart');
    }
  };

  const renderStars = (rating: number) => {
    return (
      <div className="flex items-center">
        {[1, 2, 3, 4, 5].map((star) => (
          <Star
            key={star}
            className={`w-4 h-4 ${
              star <= rating
                ? 'text-yellow-400 fill-current'
                : 'text-gray-300'
            }`}
          />
        ))}
      </div>
    );
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Please log in to view your wishlist</h2>
        </div>
      </div>
    );
  }

  return (
    <>
      <Helmet>
        <title>My Wishlist - EcommerceHub</title>
        <meta name="description" content="View and manage your saved items" />
      </Helmet>

      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Header */}
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 flex items-center">
              <Heart className="w-8 h-8 mr-3 text-red-500" />
              My Wishlist
            </h1>
            <p className="mt-2 text-gray-600">
              {wishlistItems.length} {wishlistItems.length === 1 ? 'item' : 'items'} saved for later
            </p>
          </div>

          {/* Wishlist Content */}
          {loading ? (
            <div className="flex justify-center items-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
            </div>
          ) : wishlistItems.length === 0 ? (
            <div className="text-center py-12">
              <Heart className="w-16 h-16 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">Your wishlist is empty</h3>
              <p className="text-gray-600 mb-6">
                Save items you love to your wishlist and shop them later.
              </p>
              <Link
                to="/products"
                className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700"
              >
                Start Shopping
              </Link>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {wishlistItems.map((item) => (
                <div key={item.id} className="bg-white rounded-lg shadow-md overflow-hidden group hover:shadow-lg transition-shadow">
                  {/* Product Image */}
                  <div className="relative">
                    <Link to={`/product/${item.slug}`}>
                      <img
                        src={item.image}
                        alt={item.name}
                        className="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300"
                      />
                    </Link>
                    <button
                      onClick={() => removeFromWishlist(item.id)}
                      className="absolute top-2 right-2 p-2 bg-white rounded-full shadow-md hover:bg-red-50 transition-colors"
                      title="Remove from wishlist"
                    >
                      <Trash2 className="w-4 h-4 text-red-500" />
                    </button>
                    {!item.inStock && (
                      <div className="absolute inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                        <span className="text-white font-medium">Out of Stock</span>
                      </div>
                    )}
                  </div>

                  {/* Product Info */}
                  <div className="p-4">
                    <Link to={`/product/${item.slug}`}>
                      <h3 className="text-lg font-medium text-gray-900 hover:text-indigo-600 transition-colors line-clamp-2">
                        {item.name}
                      </h3>
                    </Link>

                    {/* Rating */}
                    <div className="flex items-center mt-2">
                      {renderStars(item.rating)}
                      <span className="ml-2 text-sm text-gray-600">
                        ({item.reviewCount})
                      </span>
                    </div>

                    {/* Price */}
                    <div className="flex items-center mt-3">
                      <span className="text-xl font-bold text-gray-900 flex items-center">
                        <DollarSign className="w-5 h-5" />
                        {item.price.toFixed(2)}
                      </span>
                      {item.originalPrice && (
                        <span className="ml-2 text-sm text-gray-500 line-through">
                          ${item.originalPrice.toFixed(2)}
                        </span>
                      )}
                    </div>

                    {/* Actions */}
                    <div className="mt-4 space-y-2">
                      <button
                        onClick={() => handleAddToCart(item)}
                        disabled={!item.inStock}
                        className={`w-full flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md transition-colors ${
                          item.inStock
                            ? 'text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500'
                            : 'text-gray-400 bg-gray-200 cursor-not-allowed'
                        }`}
                      >
                        <ShoppingCart className="w-4 h-4 mr-2" />
                        {item.inStock ? 'Add to Cart' : 'Out of Stock'}
                      </button>
                      
                      <Link
                        to={`/product/${item.slug}`}
                        className="w-full flex items-center justify-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                      >
                        View Details
                      </Link>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* Continue Shopping */}
          {wishlistItems.length > 0 && (
            <div className="mt-12 text-center">
              <Link
                to="/products"
                className="inline-flex items-center px-6 py-3 border border-gray-300 text-base font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
              >
                Continue Shopping
              </Link>
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default Wishlist;
