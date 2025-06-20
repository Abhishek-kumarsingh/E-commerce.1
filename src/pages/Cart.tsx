import React from 'react';
import { Helmet } from 'react-helmet-async';
import { Link } from 'react-router-dom';
import { ShoppingCart, Plus, Minus, Trash2, Heart, ArrowRight } from 'lucide-react';
import { useCartStore } from '../store/cartStore';
import { useAuthStore } from '../store/authStore';
import { toast } from 'react-hot-toast';

const Cart: React.FC = () => {
  const { items, updateQuantity, removeFromCart, getCartTotal, getCartCount } = useCartStore();
  const { isAuthenticated } = useAuthStore();

  const handleQuantityChange = (itemId: string, newQuantity: number) => {
    if (newQuantity < 1) {
      removeFromCart(itemId);
      toast.success('Item removed from cart');
    } else {
      updateQuantity(itemId, newQuantity);
    }
  };

  const handleRemoveItem = (itemId: string) => {
    removeFromCart(itemId);
    toast.success('Item removed from cart');
  };

  const handleMoveToWishlist = async (itemId: string) => {
    try {
      // TODO: Add to wishlist API call
      // await wishlistService.addToWishlist(itemId);
      removeFromCart(itemId);
      toast.success('Item moved to wishlist');
    } catch (error) {
      toast.error('Failed to move item to wishlist');
    }
  };

  const subtotal = getCartTotal();
  const shipping = subtotal > 100 ? 0 : 10; // Free shipping over $100
  const tax = subtotal * 0.08; // 8% tax
  const total = subtotal + shipping + tax;

  return (
    <>
      <Helmet>
        <title>Shopping Cart - EcommerceHub</title>
        <meta name="description" content="Review and manage items in your shopping cart" />
      </Helmet>

      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Header */}
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 flex items-center">
              <ShoppingCart className="w-8 h-8 mr-3" />
              Shopping Cart
            </h1>
            <p className="mt-2 text-gray-600">
              {getCartCount()} {getCartCount() === 1 ? 'item' : 'items'} in your cart
            </p>
          </div>

          {items.length === 0 ? (
            /* Empty Cart */
            <div className="text-center py-12">
              <ShoppingCart className="w-16 h-16 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">Your cart is empty</h3>
              <p className="text-gray-600 mb-6">
                Looks like you haven't added any items to your cart yet.
              </p>
              <Link
                to="/products"
                className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700"
              >
                Start Shopping
              </Link>
            </div>
          ) : (
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
              {/* Cart Items */}
              <div className="lg:col-span-2">
                <div className="bg-white shadow rounded-lg">
                  <div className="px-6 py-4 border-b border-gray-200">
                    <h2 className="text-lg font-medium text-gray-900">Cart Items</h2>
                  </div>
                  <div className="divide-y divide-gray-200">
                    {items.map((item) => (
                      <div key={item.id} className="px-6 py-6">
                        <div className="flex items-center">
                          {/* Product Image */}
                          <div className="flex-shrink-0">
                            <img
                              src={item.image}
                              alt={item.name}
                              className="w-20 h-20 object-cover rounded-md"
                            />
                          </div>

                          {/* Product Details */}
                          <div className="ml-6 flex-1">
                            <div className="flex items-start justify-between">
                              <div>
                                <h3 className="text-lg font-medium text-gray-900">
                                  <Link to={`/product/${item.id}`} className="hover:text-indigo-600">
                                    {item.name}
                                  </Link>
                                </h3>
                                <p className="text-lg font-medium text-gray-900 mt-1">
                                  ${item.price.toFixed(2)}
                                </p>
                              </div>
                              <button
                                onClick={() => handleRemoveItem(item.id)}
                                className="text-red-500 hover:text-red-700 p-1"
                                title="Remove item"
                              >
                                <Trash2 className="w-5 h-5" />
                              </button>
                            </div>

                            {/* Quantity Controls */}
                            <div className="flex items-center justify-between mt-4">
                              <div className="flex items-center border border-gray-300 rounded-md">
                                <button
                                  onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                                  className="p-2 hover:bg-gray-100 transition-colors"
                                  disabled={item.quantity <= 1}
                                >
                                  <Minus className="w-4 h-4" />
                                </button>
                                <span className="px-4 py-2 text-gray-900 font-medium">
                                  {item.quantity}
                                </span>
                                <button
                                  onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                                  className="p-2 hover:bg-gray-100 transition-colors"
                                >
                                  <Plus className="w-4 h-4" />
                                </button>
                              </div>

                              <div className="flex items-center space-x-4">
                                <button
                                  onClick={() => handleMoveToWishlist(item.id)}
                                  className="flex items-center text-sm text-gray-600 hover:text-red-500 transition-colors"
                                >
                                  <Heart className="w-4 h-4 mr-1" />
                                  Move to Wishlist
                                </button>
                                <div className="text-lg font-medium text-gray-900">
                                  ${(item.price * item.quantity).toFixed(2)}
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Continue Shopping */}
                <div className="mt-6">
                  <Link
                    to="/products"
                    className="inline-flex items-center text-indigo-600 hover:text-indigo-500"
                  >
                    Continue Shopping
                    <ArrowRight className="w-4 h-4 ml-2" />
                  </Link>
                </div>
              </div>

              {/* Order Summary */}
              <div className="lg:col-span-1">
                <div className="bg-white shadow rounded-lg sticky top-8">
                  <div className="px-6 py-4 border-b border-gray-200">
                    <h2 className="text-lg font-medium text-gray-900">Order Summary</h2>
                  </div>
                  <div className="px-6 py-4 space-y-4">
                    <div className="flex justify-between">
                      <span className="text-gray-600">Subtotal</span>
                      <span className="text-gray-900">${subtotal.toFixed(2)}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-gray-600">Shipping</span>
                      <span className="text-gray-900">
                        {shipping === 0 ? 'Free' : `$${shipping.toFixed(2)}`}
                      </span>
                    </div>
                    {shipping === 0 && subtotal > 0 && (
                      <p className="text-sm text-green-600">
                        🎉 You qualify for free shipping!
                      </p>
                    )}
                    {shipping > 0 && (
                      <p className="text-sm text-gray-600">
                        Add ${(100 - subtotal).toFixed(2)} more for free shipping
                      </p>
                    )}
                    <div className="flex justify-between">
                      <span className="text-gray-600">Tax</span>
                      <span className="text-gray-900">${tax.toFixed(2)}</span>
                    </div>
                    <div className="border-t border-gray-200 pt-4">
                      <div className="flex justify-between">
                        <span className="text-lg font-medium text-gray-900">Total</span>
                        <span className="text-lg font-medium text-gray-900">${total.toFixed(2)}</span>
                      </div>
                    </div>
                  </div>
                  <div className="px-6 py-4 border-t border-gray-200">
                    {isAuthenticated ? (
                      <Link
                        to="/checkout"
                        className="w-full flex items-center justify-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                      >
                        Proceed to Checkout
                      </Link>
                    ) : (
                      <div className="space-y-3">
                        <Link
                          to="/login?redirect=/checkout"
                          className="w-full flex items-center justify-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                          Sign In to Checkout
                        </Link>
                        <p className="text-sm text-gray-600 text-center">
                          or{' '}
                          <Link to="/register" className="text-indigo-600 hover:text-indigo-500">
                            create an account
                          </Link>
                        </p>
                      </div>
                    )}
                  </div>
                </div>

                {/* Security Notice */}
                <div className="mt-6 bg-gray-100 rounded-lg p-4">
                  <h3 className="text-sm font-medium text-gray-900 mb-2">Secure Checkout</h3>
                  <p className="text-sm text-gray-600">
                    Your payment information is encrypted and secure. We accept all major credit cards and PayPal.
                  </p>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default Cart;
