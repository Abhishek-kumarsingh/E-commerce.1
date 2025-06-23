import React from 'react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  Package, 
  Mail, 
  Phone, 
  MapPin, 
  Facebook, 
  Twitter, 
  Instagram, 
  Youtube,
  Heart,
  ArrowRight,
  Shield,
  Truck,
  RotateCcw,
  Headphones
} from 'lucide-react';

const Footer: React.FC = () => {
  const currentYear = new Date().getFullYear();

  const footerLinks = {
    shop: [
      { name: 'All Products', href: '/products' },
      { name: 'New Arrivals', href: '/products?filter=new' },
      { name: 'Best Sellers', href: '/products?filter=bestsellers' },
      { name: 'Sale Items', href: '/products?filter=sale' },
      { name: 'Gift Cards', href: '/gift-cards' },
    ],
    support: [
      { name: 'Help Center', href: '/help' },
      { name: 'Contact Us', href: '/contact' },
      { name: 'Shipping Info', href: '/shipping' },
      { name: 'Returns', href: '/returns' },
      { name: 'Size Guide', href: '/size-guide' },
    ],
    company: [
      { name: 'About Us', href: '/about' },
      { name: 'Careers', href: '/careers' },
      { name: 'Press', href: '/press' },
      { name: 'Sustainability', href: '/sustainability' },
      { name: 'Investors', href: '/investors' },
    ],
    legal: [
      { name: 'Privacy Policy', href: '/privacy' },
      { name: 'Terms of Service', href: '/terms' },
      { name: 'Cookie Policy', href: '/cookies' },
      { name: 'Accessibility', href: '/accessibility' },
    ],
  };

  const socialLinks = [
    { name: 'Facebook', icon: Facebook, href: '#', color: 'hover:text-blue-600' },
    { name: 'Twitter', icon: Twitter, href: '#', color: 'hover:text-sky-500' },
    { name: 'Instagram', icon: Instagram, href: '#', color: 'hover:text-pink-600' },
    { name: 'YouTube', icon: Youtube, href: '#', color: 'hover:text-red-600' },
  ];

  const features = [
    { icon: Truck, title: 'Free Shipping', desc: 'On orders over $50' },
    { icon: RotateCcw, title: 'Easy Returns', desc: '30-day return policy' },
    { icon: Shield, title: 'Secure Payment', desc: '100% protected' },
    { icon: Headphones, title: '24/7 Support', desc: 'Always here to help' },
  ];

  return (
    <footer className="bg-gradient-to-br from-neutral-900 via-neutral-950 to-black text-white relative overflow-hidden">
      {/* Background pattern */}
      <div className="absolute inset-0 opacity-5">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_50%_50%,rgba(99,102,241,0.3),transparent_50%)]"></div>
      </div>

      {/* Features bar */}
      <div className="border-b border-neutral-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            {features.map((feature, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5, delay: index * 0.1 }}
                className="flex items-center space-x-3 group"
              >
                <div className="p-3 bg-gradient-to-r from-primary-600 to-secondary-600 rounded-xl group-hover:scale-110 transition-transform">
                  <feature.icon className="h-5 w-5 text-white" />
                </div>
                <div>
                  <h4 className="font-semibold text-white">{feature.title}</h4>
                  <p className="text-sm text-neutral-400">{feature.desc}</p>
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </div>

      {/* Main footer content */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-8">
          {/* Brand section */}
          <div className="lg:col-span-2">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.6 }}
            >
              <Link to="/" className="flex items-center space-x-3 mb-6">
                <div className="relative">
                  <div className="absolute inset-0 bg-gradient-to-r from-primary-600 to-secondary-600 rounded-xl blur-lg opacity-50"></div>
                  <div className="relative bg-gradient-to-r from-primary-600 to-secondary-600 p-3 rounded-xl">
                    <Package className="h-8 w-8 text-white" />
                  </div>
                </div>
                <div>
                  <span className="text-2xl font-bold gradient-text">ShopZone</span>
                  <p className="text-sm text-neutral-400">Premium Store</p>
                </div>
              </Link>
              
              <p className="text-neutral-300 mb-6 leading-relaxed">
                Discover amazing products at unbeatable prices. We're committed to providing 
                you with the best shopping experience and premium quality items.
              </p>

              {/* Contact info */}
              <div className="space-y-3">
                <div className="flex items-center space-x-3 text-neutral-300">
                  <Mail className="h-4 w-4 text-primary-400" />
                  <span>hello@shopzone.com</span>
                </div>
                <div className="flex items-center space-x-3 text-neutral-300">
                  <Phone className="h-4 w-4 text-primary-400" />
                  <span>+1 (555) 123-4567</span>
                </div>
                <div className="flex items-center space-x-3 text-neutral-300">
                  <MapPin className="h-4 w-4 text-primary-400" />
                  <span>123 Commerce St, City, State 12345</span>
                </div>
              </div>
            </motion.div>
          </div>

          {/* Links sections */}
          {Object.entries(footerLinks).map(([category, links], index) => (
            <motion.div
              key={category}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.6, delay: index * 0.1 }}
            >
              <h3 className="font-bold text-white mb-6 capitalize">
                {category === 'shop' ? 'Shop' : 
                 category === 'support' ? 'Support' : 
                 category === 'company' ? 'Company' : 'Legal'}
              </h3>
              <ul className="space-y-3">
                {links.map((link) => (
                  <li key={link.name}>
                    <Link
                      to={link.href}
                      className="text-neutral-400 hover:text-white transition-colors duration-300 flex items-center group"
                    >
                      <span>{link.name}</span>
                      <ArrowRight className="h-3 w-3 ml-1 opacity-0 group-hover:opacity-100 group-hover:translate-x-1 transition-all" />
                    </Link>
                  </li>
                ))}
              </ul>
            </motion.div>
          ))}
        </div>

        {/* Newsletter signup */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.6, delay: 0.4 }}
          className="mt-16 pt-8 border-t border-neutral-800"
        >
          <div className="max-w-md">
            <h3 className="font-bold text-white mb-4">Stay Updated</h3>
            <p className="text-neutral-400 mb-4">
              Get the latest news, offers, and updates delivered to your inbox.
            </p>
            <div className="flex">
              <input
                type="email"
                placeholder="Enter your email"
                className="flex-1 px-4 py-3 bg-neutral-800 border border-neutral-700 rounded-l-xl text-white placeholder-neutral-500 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              />
              <button className="bg-gradient-to-r from-primary-600 to-secondary-600 text-white px-6 py-3 rounded-r-xl hover:from-primary-700 hover:to-secondary-700 transition-all font-semibold">
                Subscribe
              </button>
            </div>
          </div>
        </motion.div>
      </div>

      {/* Bottom bar */}
      <div className="border-t border-neutral-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex flex-col md:flex-row items-center justify-between space-y-4 md:space-y-0">
            <div className="flex items-center space-x-2 text-neutral-400">
              <span>© {currentYear} ShopZone. Made with</span>
              <Heart className="h-4 w-4 text-red-500 fill-current" />
              <span>by our team.</span>
            </div>

            {/* Social links */}
            <div className="flex items-center space-x-4">
              {socialLinks.map((social) => (
                <motion.a
                  key={social.name}
                  href={social.href}
                  whileHover={{ scale: 1.2, y: -2 }}
                  whileTap={{ scale: 0.9 }}
                  className={`p-2 bg-neutral-800 rounded-lg text-neutral-400 ${social.color} transition-all duration-300 hover:bg-neutral-700`}
                >
                  <social.icon className="h-5 w-5" />
                </motion.a>
              ))}
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
