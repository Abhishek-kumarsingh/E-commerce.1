import React from 'react';
import { Link } from 'react-router-dom';
import { Home, ArrowLeft, Search } from 'lucide-react';

const NotFound: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="text-center">
          {/* 404 Illustration */}
          <div className="mx-auto w-32 h-32 bg-blue-100 rounded-full flex items-center justify-center mb-8">
            <span className="text-6xl font-bold text-blue-600">404</span>
          </div>
          
          <h1 className="text-3xl font-extrabold text-gray-900 mb-4">
            Page Not Found
          </h1>
          
          <p className="text-lg text-gray-600 mb-8">
            Sorry, we couldn't find the page you're looking for. 
            It might have been moved, deleted, or you entered the wrong URL.
          </p>
          
          {/* Action Buttons */}
          <div className="space-y-4 sm:space-y-0 sm:space-x-4 sm:flex sm:justify-center">
            <Link
              to="/"
              className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
            >
              <Home className="w-5 h-5 mr-2" />
              Go Home
            </Link>
            
            <button
              onClick={() => window.history.back()}
              className="inline-flex items-center px-6 py-3 border border-gray-300 text-base font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
            >
              <ArrowLeft className="w-5 h-5 mr-2" />
              Go Back
            </button>
          </div>
          
          {/* Search Suggestion */}
          <div className="mt-8 p-6 bg-white rounded-lg shadow-sm border border-gray-200">
            <h3 className="text-lg font-medium text-gray-900 mb-2">
              Looking for something specific?
            </h3>
            <p className="text-gray-600 mb-4">
              Try searching for products or browse our categories
            </p>
            <Link
              to="/products"
              className="inline-flex items-center text-blue-600 hover:text-blue-500 font-medium"
            >
              <Search className="w-4 h-4 mr-2" />
              Browse Products
            </Link>
          </div>
          
          {/* Help Links */}
          <div className="mt-8 text-sm text-gray-500">
            <p>Need help? Contact our support team:</p>
            <div className="mt-2 space-x-4">
              <a href="mailto:support@ecommerce.com" className="text-blue-600 hover:text-blue-500">
                Email Support
              </a>
              <span>•</span>
              <a href="tel:+1234567890" className="text-blue-600 hover:text-blue-500">
                Call Us
              </a>
              <span>•</span>
              <Link to="/help" className="text-blue-600 hover:text-blue-500">
                Help Center
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotFound;
