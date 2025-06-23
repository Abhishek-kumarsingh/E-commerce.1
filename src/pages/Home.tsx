import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, Star, Truck, Shield, RotateCcw, Headphones } from 'lucide-react';
import { products, categories } from '../data/mockData';
import ProductGrid from '../components/ProductGrid';
import { motion } from 'framer-motion';

const Home: React.FC = () => {
  const [featuredProducts, setFeaturedProducts] = useState(products.filter(p => p.featured));

  useEffect(() => {
    // Simulate loading featured products
    const timer = setTimeout(() => {
      setFeaturedProducts(products.filter(p => p.featured));
    }, 1000);

    return () => clearTimeout(timer);
  }, []);

  const features = [
    {
      icon: Truck,
      title: 'Free Shipping',
      description: 'Free shipping on orders over $50'
    },
    {
      icon: Shield,
      title: 'Secure Payment',
      description: '100% secure payment processing'
    },
    {
      icon: RotateCcw,
      title: 'Easy Returns',
      description: '30-day hassle-free returns'
    },
    {
      icon: Headphones,
      title: '24/7 Support',
      description: 'Round-the-clock customer support'
    }
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-neutral-50 via-white to-neutral-100 dark:from-neutral-950 dark:via-neutral-900 dark:to-neutral-950">
      {/* Enhanced Hero Section */}
      <section className="relative min-h-screen flex items-center justify-center overflow-hidden">
        {/* Animated background */}
        <div className="absolute inset-0 bg-gradient-to-br from-primary-600 via-primary-700 to-secondary-600"></div>
        <div className="absolute inset-0 bg-gradient-to-t from-black/30 via-transparent to-transparent"></div>

        {/* Floating elements */}
        <div className="absolute inset-0 overflow-hidden">
          <motion.div
            animate={{
              x: [0, 100, 0],
              y: [0, -100, 0],
              rotate: [0, 180, 360]
            }}
            transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
            className="absolute top-1/4 left-1/4 w-64 h-64 bg-white/10 rounded-full blur-xl"
          />
          <motion.div
            animate={{
              x: [0, -150, 0],
              y: [0, 100, 0],
              rotate: [360, 180, 0]
            }}
            transition={{ duration: 25, repeat: Infinity, ease: "linear" }}
            className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-secondary-500/20 rounded-full blur-2xl"
          />
        </div>

        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-32 text-center">
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 1, ease: "easeOut" }}
            className="space-y-8"
          >
            {/* Main headline */}
            <motion.h1
              className="text-5xl md:text-7xl lg:text-8xl font-bold text-white leading-tight"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 1, delay: 0.2 }}
            >
              <span className="block">Discover</span>
              <span className="block bg-gradient-to-r from-yellow-400 via-pink-400 to-purple-400 bg-clip-text text-transparent animate-gradient">
                Amazing
              </span>
              <span className="block">Products</span>
            </motion.h1>

            {/* Subtitle */}
            <motion.p
              className="text-xl md:text-2xl lg:text-3xl text-white/90 max-w-4xl mx-auto leading-relaxed font-light"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 1, delay: 0.4 }}
            >
              Shop the latest trends and find everything you need at unbeatable prices.
              <span className="block mt-2 text-lg md:text-xl text-white/70">
                Premium quality • Fast shipping • 30-day returns
              </span>
            </motion.p>

            {/* CTA Buttons */}
            <motion.div
              className="flex flex-col sm:flex-row items-center justify-center gap-6 pt-8"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 1, delay: 0.6 }}
            >
              <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                <Link
                  to="/products"
                  className="group relative inline-flex items-center bg-white text-primary-600 px-10 py-5 rounded-2xl font-bold text-lg shadow-2xl hover:shadow-glow-lg transition-all duration-300 overflow-hidden"
                >
                  <span className="relative z-10 flex items-center">
                    Shop Now
                    <ArrowRight className="ml-3 h-6 w-6 group-hover:translate-x-1 transition-transform" />
                  </span>
                  <div className="absolute inset-0 bg-gradient-to-r from-primary-600 to-secondary-600 opacity-0 group-hover:opacity-10 transition-opacity"></div>
                </Link>
              </motion.div>

              <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                <button className="glass-button text-white border-white/30 px-10 py-5 text-lg font-semibold">
                  <Star className="mr-3 h-6 w-6" />
                  View Featured
                </button>
              </motion.div>
            </motion.div>

            {/* Stats */}
            <motion.div
              className="grid grid-cols-3 gap-8 pt-16 max-w-2xl mx-auto"
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 1, delay: 0.8 }}
            >
              <div className="text-center">
                <div className="text-3xl md:text-4xl font-bold text-white">10K+</div>
                <div className="text-white/70 text-sm md:text-base">Products</div>
              </div>
              <div className="text-center">
                <div className="text-3xl md:text-4xl font-bold text-white">50K+</div>
                <div className="text-white/70 text-sm md:text-base">Happy Customers</div>
              </div>
              <div className="text-center">
                <div className="text-3xl md:text-4xl font-bold text-white">99%</div>
                <div className="text-white/70 text-sm md:text-base">Satisfaction</div>
              </div>
            </motion.div>
          </motion.div>
        </div>

        {/* Scroll indicator */}
        <motion.div
          className="absolute bottom-8 left-1/2 transform -translate-x-1/2"
          animate={{ y: [0, 10, 0] }}
          transition={{ duration: 2, repeat: Infinity }}
        >
          <div className="w-6 h-10 border-2 border-white/50 rounded-full flex justify-center">
            <div className="w-1 h-3 bg-white/70 rounded-full mt-2 animate-pulse"></div>
          </div>
        </motion.div>
      </section>

      {/* Enhanced Features Section */}
      <section className="py-24 relative">
        <div className="absolute inset-0 bg-gradient-to-b from-white via-neutral-50 to-white dark:from-neutral-900 dark:via-neutral-950 dark:to-neutral-900"></div>
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Section header */}
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8 }}
            className="text-center mb-20"
          >
            <h2 className="text-4xl md:text-5xl font-bold gradient-text mb-6">
              Why Choose ShopZone?
            </h2>
            <p className="text-xl text-neutral-600 dark:text-neutral-400 max-w-3xl mx-auto">
              Experience shopping like never before with our premium features and unmatched service quality
            </p>
          </motion.div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            {features.map((feature, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.6, delay: index * 0.1 }}
                whileHover={{ y: -8, scale: 1.02 }}
                className="group"
              >
                <div className="glass-card text-center h-full hover:shadow-hard transition-all duration-500">
                  {/* Icon with enhanced design */}
                  <motion.div
                    className="relative inline-flex items-center justify-center w-20 h-20 mb-6"
                    whileHover={{ rotate: 360, scale: 1.1 }}
                    transition={{ duration: 0.6 }}
                  >
                    <div className="absolute inset-0 bg-gradient-to-r from-primary-500 to-secondary-500 rounded-2xl blur-lg opacity-30 group-hover:opacity-50 transition-opacity"></div>
                    <div className="relative bg-gradient-to-r from-primary-500 to-secondary-500 rounded-2xl p-4 shadow-lg">
                      <feature.icon className="h-8 w-8 text-white" />
                    </div>
                  </motion.div>

                  <h3 className="text-xl font-bold text-neutral-900 dark:text-neutral-100 mb-4 group-hover:text-primary-600 dark:group-hover:text-primary-400 transition-colors">
                    {feature.title}
                  </h3>
                  <p className="text-neutral-600 dark:text-neutral-400 leading-relaxed">
                    {feature.description}
                  </p>

                  {/* Hover effect indicator */}
                  <motion.div
                    className="mt-6 w-12 h-1 bg-gradient-to-r from-primary-500 to-secondary-500 rounded-full mx-auto opacity-0 group-hover:opacity-100 transition-opacity"
                    initial={{ width: 0 }}
                    whileInView={{ width: 48 }}
                    transition={{ duration: 0.8, delay: index * 0.1 + 0.5 }}
                  />
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Enhanced Categories Section */}
      <section className="py-24 relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary-50 via-white to-secondary-50 dark:from-primary-950 dark:via-neutral-900 dark:to-secondary-950"></div>

        {/* Background pattern */}
        <div className="absolute inset-0 opacity-5">
          <div className="absolute top-0 left-0 w-full h-full bg-[radial-gradient(circle_at_50%_50%,rgba(99,102,241,0.3),transparent_50%)]"></div>
        </div>

        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8 }}
            className="text-center mb-20"
          >
            <h2 className="text-4xl md:text-5xl font-bold text-neutral-900 dark:text-neutral-100 mb-6">
              Shop by <span className="gradient-text">Category</span>
            </h2>
            <p className="text-xl text-neutral-600 dark:text-neutral-400 max-w-3xl mx-auto">
              Explore our curated collections and discover products tailored to your lifestyle
            </p>
          </motion.div>

          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-6">
            {categories.map((category, index) => (
              <motion.div
                key={category.id}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.6, delay: index * 0.1 }}
                whileHover={{ y: -8, scale: 1.05 }}
                className="group"
              >
                <Link
                  to={`/category/${category.slug}`}
                  className="block glass-card text-center h-full hover:shadow-hard transition-all duration-500"
                >
                  {/* Icon container */}
                  <motion.div
                    className="relative w-16 h-16 mx-auto mb-6"
                    whileHover={{ rotate: 360 }}
                    transition={{ duration: 0.8 }}
                  >
                    <div className="absolute inset-0 bg-gradient-to-r from-primary-500 to-secondary-500 rounded-2xl blur-lg opacity-30 group-hover:opacity-60 transition-opacity"></div>
                    <div className="relative bg-gradient-to-r from-primary-500 to-secondary-500 rounded-2xl p-4 shadow-lg">
                      <Star className="h-8 w-8 text-white" />
                    </div>
                  </motion.div>

                  <h3 className="font-bold text-lg text-neutral-900 dark:text-neutral-100 mb-2 group-hover:text-primary-600 dark:group-hover:text-primary-400 transition-colors">
                    {category.name}
                  </h3>
                  <p className="text-sm text-neutral-500 dark:text-neutral-400 mb-4">
                    {category.productCount} products
                  </p>

                  {/* Progress indicator */}
                  <motion.div
                    className="w-full h-1 bg-neutral-200 dark:bg-neutral-700 rounded-full overflow-hidden"
                    initial={{ opacity: 0 }}
                    whileInView={{ opacity: 1 }}
                    transition={{ delay: index * 0.1 + 0.5 }}
                  >
                    <motion.div
                      className="h-full bg-gradient-to-r from-primary-500 to-secondary-500 rounded-full"
                      initial={{ width: 0 }}
                      whileInView={{ width: `${Math.min(category.productCount / 50 * 100, 100)}%` }}
                      transition={{ duration: 1, delay: index * 0.1 + 0.7 }}
                    />
                  </motion.div>
                </Link>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Enhanced Featured Products Section */}
      <section className="py-24 relative">
        <div className="absolute inset-0 bg-gradient-to-b from-white via-neutral-50 to-white dark:from-neutral-900 dark:via-neutral-950 dark:to-neutral-900"></div>
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8 }}
            className="text-center mb-20"
          >
            <h2 className="text-4xl md:text-5xl font-bold text-neutral-900 dark:text-neutral-100 mb-6">
              <span className="gradient-text">Featured</span> Products
            </h2>
            <p className="text-xl text-neutral-600 dark:text-neutral-400 max-w-3xl mx-auto">
              Discover our handpicked selection of premium products, carefully curated for quality and style
            </p>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8, delay: 0.2 }}
          >
            <ProductGrid products={featuredProducts} />
          </motion.div>

          <motion.div
            className="text-center mt-16"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8, delay: 0.4 }}
          >
            <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
              <Link
                to="/products"
                className="group inline-flex items-center bg-gradient-to-r from-primary-600 to-secondary-600 text-white px-10 py-4 rounded-2xl font-bold text-lg shadow-hard hover:shadow-glow-lg transition-all duration-300 relative overflow-hidden"
              >
                <span className="relative z-10 flex items-center">
                  View All Products
                  <ArrowRight className="ml-3 h-6 w-6 group-hover:translate-x-1 transition-transform" />
                </span>
                <div className="absolute inset-0 bg-gradient-to-r from-secondary-600 to-primary-600 opacity-0 group-hover:opacity-100 transition-opacity"></div>
              </Link>
            </motion.div>
          </motion.div>
        </div>
      </section>

      {/* Enhanced Newsletter Section */}
      <section className="py-24 relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary-600 via-primary-700 to-secondary-600"></div>
        <div className="absolute inset-0 bg-gradient-to-t from-black/20 via-transparent to-transparent"></div>

        {/* Animated background elements */}
        <div className="absolute inset-0">
          <motion.div
            animate={{
              scale: [1, 1.2, 1],
              rotate: [0, 180, 360]
            }}
            transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
            className="absolute top-1/4 right-1/4 w-64 h-64 bg-white/10 rounded-full blur-2xl"
          />
          <motion.div
            animate={{
              scale: [1.2, 1, 1.2],
              rotate: [360, 180, 0]
            }}
            transition={{ duration: 25, repeat: Infinity, ease: "linear" }}
            className="absolute bottom-1/4 left-1/4 w-96 h-96 bg-secondary-400/20 rounded-full blur-3xl"
          />
        </div>

        <div className="relative max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.8 }}
            className="space-y-8"
          >
            <h2 className="text-4xl md:text-5xl font-bold text-white mb-6">
              Stay in the <span className="text-yellow-400">Loop</span>
            </h2>
            <p className="text-xl text-white/90 max-w-2xl mx-auto leading-relaxed">
              Join our exclusive community and be the first to discover new arrivals,
              special offers, and insider deals. Plus, get 10% off your first order!
            </p>

            {/* Newsletter form */}
            <motion.div
              className="max-w-lg mx-auto"
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.8, delay: 0.2 }}
            >
              <div className="glass rounded-2xl p-2 backdrop-blur-sm border border-white/20">
                <div className="flex flex-col sm:flex-row gap-2">
                  <input
                    type="email"
                    placeholder="Enter your email address"
                    className="flex-1 px-6 py-4 bg-white/10 backdrop-blur-sm border border-white/20 rounded-xl text-white placeholder-white/70 focus:outline-none focus:ring-2 focus:ring-white/50 focus:border-white/50 transition-all"
                  />
                  <motion.button
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    className="bg-white text-primary-600 px-8 py-4 rounded-xl hover:bg-neutral-100 transition-colors font-bold text-lg shadow-lg"
                  >
                    Subscribe
                  </motion.button>
                </div>
              </div>

              <p className="text-white/70 text-sm mt-4">
                No spam, unsubscribe at any time. We respect your privacy.
              </p>
            </motion.div>

            {/* Benefits */}
            <motion.div
              className="grid grid-cols-1 md:grid-cols-3 gap-8 pt-12"
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.8, delay: 0.4 }}
            >
              <div className="text-center">
                <div className="w-12 h-12 bg-white/20 rounded-full flex items-center justify-center mx-auto mb-4">
                  <Star className="h-6 w-6 text-white" />
                </div>
                <h3 className="font-semibold text-white mb-2">Exclusive Deals</h3>
                <p className="text-white/70 text-sm">Get access to member-only discounts and early sales</p>
              </div>
              <div className="text-center">
                <div className="w-12 h-12 bg-white/20 rounded-full flex items-center justify-center mx-auto mb-4">
                  <Truck className="h-6 w-6 text-white" />
                </div>
                <h3 className="font-semibold text-white mb-2">New Arrivals</h3>
                <p className="text-white/70 text-sm">Be first to shop the latest products and trends</p>
              </div>
              <div className="text-center">
                <div className="w-12 h-12 bg-white/20 rounded-full flex items-center justify-center mx-auto mb-4">
                  <Headphones className="h-6 w-6 text-white" />
                </div>
                <h3 className="font-semibold text-white mb-2">VIP Support</h3>
                <p className="text-white/70 text-sm">Priority customer service and personalized help</p>
              </div>
            </motion.div>
          </motion.div>
        </div>
      </section>
    </div>
  );
};

export default Home;