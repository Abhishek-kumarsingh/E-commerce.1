import React from 'react';
import { Helmet } from 'react-helmet-async';
import { TrendingUp, BarChart3, PieChart } from 'lucide-react';

const AdminAnalytics: React.FC = () => {
  return (
    <>
      <Helmet>
        <title>Analytics - Admin</title>
      </Helmet>
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 flex items-center">
              <TrendingUp className="w-8 h-8 mr-3" />
              Analytics
            </h1>
          </div>
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="bg-white shadow rounded-lg p-6">
              <h2 className="text-lg font-medium text-gray-900 flex items-center mb-4">
                <BarChart3 className="w-5 h-5 mr-2" />
                Sales Analytics
              </h2>
              <p className="text-gray-600">Sales charts and metrics will be implemented here.</p>
            </div>
            <div className="bg-white shadow rounded-lg p-6">
              <h2 className="text-lg font-medium text-gray-900 flex items-center mb-4">
                <PieChart className="w-5 h-5 mr-2" />
                User Analytics
              </h2>
              <p className="text-gray-600">User behavior charts will be implemented here.</p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default AdminAnalytics;
