import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CreditCard, Truck, Lock } from 'lucide-react';
import { useCartStore } from '../store/cartStore';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';

const Checkout: React.FC = () => {
  const { items, getTotalPrice, clearCart } = useCartStore();
  // const { isAuthenticated } = useAuthStore(); // Not used but kept if needed later
  const navigate = useNavigate();

  const [shippingInfo, setShippingInfo] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    address: '',
    city: '',
    state: '',
    zipCode: '',
    country: 'USA'
  });

  const [paymentInfo, setPaymentInfo] = useState({
    cardNumber: '',
    expiryDate: '',
    cvv: '',
    cardholderName: ''
  });

  const [isProcessing, setIsProcessing] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const [touched, setTouched] = useState<{ [key: string]: boolean }>({});

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
    section: 'shipping' | 'payment'
  ) => {
    const { name, value } = e.target;
    setTouched((t) => ({ ...t, [name]: true }));
    if (section === 'shipping') {
      setShippingInfo((prev) => ({ ...prev, [name]: value }));
    } else {
      setPaymentInfo((prev) => ({ ...prev, [name]: value }));
    }
  };

  const validateForm = () => {
    const newErrors: { [key: string]: string } = {};

    // Shipping validation
    if (!shippingInfo.firstName) newErrors.firstName = 'First name is required';
    if (!shippingInfo.lastName) newErrors.lastName = 'Last name is required';
    if (!shippingInfo.email) newErrors.email = 'Email is required';
    if (!shippingInfo.phone) newErrors.phone = 'Phone is required';
    if (!shippingInfo.address) newErrors.address = 'Address is required';
    if (!shippingInfo.city) newErrors.city = 'City is required';
    if (!shippingInfo.state) newErrors.state = 'State is required';
    if (!shippingInfo.zipCode) newErrors.zipCode = 'ZIP code is required';
    if (!shippingInfo.country) newErrors.country = 'Country is required';

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (shippingInfo.email && !emailRegex.test(shippingInfo.email)) newErrors.email = 'Invalid email address';

    // Payment validation
    if (!paymentInfo.cardNumber) newErrors.cardNumber = 'Card number is required';
    const cardNumberRegex = /^\d{16}$/;
    const cleanCardNumber = paymentInfo.cardNumber.replace(/\s+/g, '');
    if (paymentInfo.cardNumber && !cardNumberRegex.test(cleanCardNumber)) newErrors.cardNumber = 'Card number must be 16 digits';

    if (!paymentInfo.expiryDate) newErrors.expiryDate = 'Expiry date is required';
    const expiryRegex = /^(0[1-9]|1[0-2])\/\d{2}$/;
    if (paymentInfo.expiryDate && !expiryRegex.test(paymentInfo.expiryDate)) newErrors.expiryDate = 'Expiry must be MM/YY';

    if (!paymentInfo.cvv) newErrors.cvv = 'CVV is required';
    const cvvRegex = /^\d{3,4}$/;
    if (paymentInfo.cvv && !cvvRegex.test(paymentInfo.cvv)) newErrors.cvv = 'CVV must be 3 or 4 digits';

    if (!paymentInfo.cardholderName) newErrors.cardholderName = 'Cardholder name is required';

    setErrors(newErrors);
    return newErrors;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newErrors = validateForm();
    setTouched({
      firstName: true,
      lastName: true,
      email: true,
      phone: true,
      address: true,
      city: true,
      state: true,
      zipCode: true,
      country: true,
      cardNumber: true,
      expiryDate: true,
      cvv: true,
      cardholderName: true
    });

    if (Object.keys(newErrors).length > 0) {
      const firstError = Object.keys(newErrors)[0];
      const el = document.getElementById(firstError);
      if (el) el.focus();
      return;
    }

    setIsProcessing(true);

    try {
      await new Promise((resolve) => setTimeout(resolve, 2000));
      toast.success('Order placed successfully! Redirecting...', {
        icon: '🎉',
        duration: 2000
      });
      setTimeout(() => {
        clearCart();
        navigate('/orders');
      }, 1200);
    } catch (error) {
      console.error('Checkout error:', error);
      toast.error('There was a problem processing your order. Please try again.');
    } finally {
      setIsProcessing(false);
    }
  };

  const getFieldError = (field: string) => {
    return errors[field] && touched[field]
      ? 'border-red-500 bg-red-50 dark:bg-red-900/20'
      : '';
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(price);
  };

  if (items.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
        <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
            Your cart is empty
          </h1>
          <p className="text-gray-600 dark:text-gray-400 mb-8">
            Add some products to your cart before checking out.
          </p>
          <button
            onClick={() => navigate('/products')}
            className="bg-primary-600 text-white px-6 py-3 rounded-lg hover:bg-primary-700 transition-colors"
          >
            Continue Shopping
          </button>
        </div>
      </div>
    );
  }

  const subtotal = getTotalPrice();
  const shipping = subtotal > 50 ? 0 : 9.99;
  const tax = subtotal * 0.08;
  const total = subtotal + shipping + tax;

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-4 sm:py-8">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          <h1 className="text-2xl sm:text-3xl font-bold text-gray-900 dark:text-white mb-4 sm:mb-8">
            Checkout
          </h1>
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 sm:gap-8">
            {/* Checkout Form */}
            <div className="space-y-8">
              {/* Shipping Information */}
              <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm p-6">
                <div className="flex items-center mb-6">
                  <Truck className="h-6 w-6 text-primary-600 mr-3" />
                  <h2 className="text-xl font-semibold text-gray-900 dark:text-white">
                    Shipping Information
                  </h2>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                      First Name
                    </label>
                    <input
                      id="firstName"
                      name="firstName"
                      value={shippingInfo.firstName}
                      onChange={(e) => handleInputChange(e, 'shipping')}
                      className={`w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 dark:bg-gray-700 dark:text-white ${getFieldError(
                        'firstName'
                      )}`}
                      aria-invalid={!!errors.firstName && touched.firstName}
                      aria-describedby={errors.firstName && touched.firstName ? 'error-firstName' : undefined}
                      required
                    />
                    {errors.firstName && touched.firstName && (
                      <p id="error-firstName" className="mt-1 text-sm text-red-600">
                        {errors.firstName}
                      </p>
                    )}
                  </div>

                  {/* Repeat similar structure for other fields... */}

                  {/* For brevity, only one field shown above; add remaining input fields similarly */}

                </div>
              </div>

              {/* Payment Information */}
              <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm p-6">
                <div className="flex items-center mb-6">
                  <CreditCard className="h-6 w-6 text-primary-600 mr-3" />
                  <h2 className="text-xl font-semibold text-gray-900 dark:text-white">
                    Payment Information
                  </h2>
                  <Lock className="h-4 w-4 text-green-600 ml-2" />
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="md:col-span-2">
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                      Card Number
                    </label>
                    <input
                      id="cardNumber"
                      name="cardNumber"
                      value={paymentInfo.cardNumber}
                      onChange={(e) => handleInputChange(e, 'payment')}
                      className={`w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 dark:bg-gray-700 dark:text-white ${getFieldError(
                        'cardNumber'
                      )}`}
                      aria-invalid={!!errors.cardNumber && touched.cardNumber}
                      aria-describedby={errors.cardNumber && touched.cardNumber ? 'error-cardNumber' : 'helper-cardNumber'}
                      required
                    />
                    {errors.cardNumber && touched.cardNumber && (
                      <p id="error-cardNumber" className="mt-1 text-sm text-red-600">
                        {errors.cardNumber}
                      </p>
                    )}
                  </div>
                </div>
              </div>
            </div>

            {/* Order Summary */}
            <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm p-6 h-fit">
              <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-6">Order Summary</h2>
              <div className="space-y-4 mb-6">
                {items.map((item) => (
                  <div key={item.product.id} className="flex items-center space-x-4">
                    <img
                      src={item.product.image}
                      alt={item.product.name}
                      className="w-16 h-16 object-cover rounded-lg"
                    />
                    <div className="flex-1">
                      <h4 className="font-medium text-gray-900 dark:text-white text-sm">{item.product.name}</h4>
                      <p className="text-gray-600 dark:text-gray-400 text-sm">Qty: {item.quantity}</p>
                    </div>
                    <span className="font-medium text-gray-900 dark:text-white">
                      {formatPrice(item.product.price * item.quantity)}
                    </span>
                  </div>
                ))}
              </div>
              <div className="border-t border-gray-200 dark:border-gray-700 pt-4 space-y-2">
                <div className="flex justify-between">
                  <span className="text-gray-600 dark:text-gray-400">Subtotal</span>
                  <span className="text-gray-900 dark:text-white">{formatPrice(subtotal)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600 dark:text-gray-400">Shipping</span>
                  <span className="text-gray-900 dark:text-white">
                    {shipping === 0 ? 'Free' : formatPrice(shipping)}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600 dark:text-gray-400">Tax</span>
                  <span className="text-gray-900 dark:text-white">{formatPrice(tax)}</span>
                </div>
                <div className="flex justify-between text-lg font-semibold pt-2 border-t border-gray-200 dark:border-gray-700">
                  <span className="text-gray-900 dark:text-white">Total</span>
                  <span className="text-gray-900 dark:text-white">{formatPrice(total)}</span>
                </div>
              </div>
              <form onSubmit={handleSubmit} className="mt-6">
                <button
                  type="submit"
                  disabled={isProcessing}
                  className="w-full bg-primary-600 text-white py-3 rounded-lg hover:bg-primary-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors font-medium focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-2 dark:focus:ring-offset-gray-800"
                  aria-label="Place order"
                >
                  {isProcessing ? (
                    <div className="flex items-center justify-center">
                      <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                      <span>Processing...</span>
                    </div>
                  ) : (
                    <span>Place Order - {formatPrice(total)}</span>
                  )}
                </button>
              </form>
            </div>
          </div>
        </motion.div>
      </div>
    </div>
  );
};

export default Checkout;