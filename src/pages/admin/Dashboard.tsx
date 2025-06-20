import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import { 
  Users, 
  Package, 
  ShoppingCart, 
  DollarSign, 
  TrendingUp, 
  TrendingDown,
  Eye,
  Calendar
} from 'lucide-react';

interface DashboardStats {
  totalUsers: number;
  totalProducts: number;
  totalOrders: number;
  totalRevenue: number;
  userGrowth: number;
  productGrowth: number;
  orderGrowth: number;
  revenueGrowth: number;
}

interface RecentOrder {
  id: string;
  orderNumber: string;
  customer: string;
  total: number;
  status: string;
  date: string;
}

const AdminDashboard: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats>({
    totalUsers: 0,
    totalProducts: 0,
    totalOrders: 0,
    totalRevenue: 0,
    userGrowth: 0,
    productGrowth: 0,
    orderGrowth: 0,
    revenueGrowth: 0,
  });
  const [recentOrders, setRecentOrders] = useState<RecentOrder[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      // TODO: Replace with actual API calls
      // const [statsResponse, ordersResponse] = await Promise.all([
      //   adminService.getDashboardStats(),
      //   adminService.getRecentOrders()
      // ]);
      
      // Mock data for now
      const mockStats: DashboardStats = {
        totalUsers: 1247,
        totalProducts: 89,
        totalOrders: 342,
        totalRevenue: 45678.90,
        userGrowth: 12.5,
        productGrowth: 8.3,
        orderGrowth: -2.1,
        revenueGrowth: 15.7,
      };

      const mockOrders: RecentOrder[] = [
        {
          id: '1',
          orderNumber: 'ORD-2024-001',
          customer: 'John Doe',
          total: 299.99,
          status: 'PENDING',
          date: '2024-01-20'
        },
        {
          id: '2',
          orderNumber: 'ORD-2024-002',
          customer: 'Jane Smith',
          total: 149.99,
          status: 'SHIPPED',
          date: '2024-01-19'
        },
        {
          id: '3',
          orderNumber: 'ORD-2024-003',
          customer: 'Bob Johnson',
          total: 89.99,
          status: 'DELIVERED',
          date: '2024-01-18'
        }
      ];

      setStats(mockStats);
      setRecentOrders(mockOrders);
    } catch (error) {
      console.error('Failed to fetch dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const StatCard: React.FC<{
    title: string;
    value: string | number;
    growth: number;
    icon: React.ReactNode;
    prefix?: string;
  }> = ({ title, value, growth, icon, prefix = '' }) => (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className="text-2xl font-semibold text-gray-900">
            {prefix}{typeof value === 'number' ? value.toLocaleString() : value}
          </p>
        </div>
        <div className="p-3 bg-indigo-100 rounded-full">
          {icon}
        </div>
      </div>
      <div className="mt-4 flex items-center">
        {growth >= 0 ? (
          <TrendingUp className="w-4 h-4 text-green-500 mr-1" />
        ) : (
          <TrendingDown className="w-4 h-4 text-red-500 mr-1" />
        )}
        <span className={`text-sm font-medium ${growth >= 0 ? 'text-green-600' : 'text-red-600'}`}>
          {Math.abs(growth)}%
        </span>
        <span className="text-sm text-gray-600 ml-1">from last month</span>
      </div>
    </div>
  );

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  return (
    <>
      <Helmet>
        <title>Admin Dashboard - EcommerceHub</title>
        <meta name="description" content="Admin dashboard for managing the e-commerce platform" />
      </Helmet>

      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Header */}
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
            <p className="mt-2 text-gray-600">
              Welcome back! Here's what's happening with your store today.
            </p>
          </div>

          {/* Stats Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <StatCard
              title="Total Users"
              value={stats.totalUsers}
              growth={stats.userGrowth}
              icon={<Users className="w-6 h-6 text-indigo-600" />}
            />
            <StatCard
              title="Total Products"
              value={stats.totalProducts}
              growth={stats.productGrowth}
              icon={<Package className="w-6 h-6 text-indigo-600" />}
            />
            <StatCard
              title="Total Orders"
              value={stats.totalOrders}
              growth={stats.orderGrowth}
              icon={<ShoppingCart className="w-6 h-6 text-indigo-600" />}
            />
            <StatCard
              title="Total Revenue"
              value={stats.totalRevenue.toFixed(2)}
              growth={stats.revenueGrowth}
              icon={<DollarSign className="w-6 h-6 text-indigo-600" />}
              prefix="$"
            />
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            {/* Recent Orders */}
            <div className="bg-white rounded-lg shadow">
              <div className="px-6 py-4 border-b border-gray-200">
                <h2 className="text-lg font-medium text-gray-900">Recent Orders</h2>
              </div>
              <div className="divide-y divide-gray-200">
                {recentOrders.map((order) => (
                  <div key={order.id} className="px-6 py-4">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm font-medium text-gray-900">{order.orderNumber}</p>
                        <p className="text-sm text-gray-600">{order.customer}</p>
                      </div>
                      <div className="text-right">
                        <p className="text-sm font-medium text-gray-900">${order.total.toFixed(2)}</p>
                        <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                          order.status === 'DELIVERED' ? 'bg-green-100 text-green-800' :
                          order.status === 'SHIPPED' ? 'bg-blue-100 text-blue-800' :
                          order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                          'bg-gray-100 text-gray-800'
                        }`}>
                          {order.status}
                        </span>
                      </div>
                    </div>
                    <div className="mt-2 flex items-center justify-between">
                      <div className="flex items-center text-sm text-gray-600">
                        <Calendar className="w-4 h-4 mr-1" />
                        {new Date(order.date).toLocaleDateString()}
                      </div>
                      <button className="text-indigo-600 hover:text-indigo-500 text-sm font-medium">
                        <Eye className="w-4 h-4 inline mr-1" />
                        View
                      </button>
                    </div>
                  </div>
                ))}
              </div>
              <div className="px-6 py-3 border-t border-gray-200">
                <a href="/admin/orders" className="text-indigo-600 hover:text-indigo-500 text-sm font-medium">
                  View all orders →
                </a>
              </div>
            </div>

            {/* Quick Actions */}
            <div className="bg-white rounded-lg shadow">
              <div className="px-6 py-4 border-b border-gray-200">
                <h2 className="text-lg font-medium text-gray-900">Quick Actions</h2>
              </div>
              <div className="p-6 space-y-4">
                <a
                  href="/admin/products"
                  className="block w-full text-left px-4 py-3 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-center">
                    <Package className="w-5 h-5 text-gray-400 mr-3" />
                    <div>
                      <p className="text-sm font-medium text-gray-900">Manage Products</p>
                      <p className="text-sm text-gray-600">Add, edit, or remove products</p>
                    </div>
                  </div>
                </a>
                
                <a
                  href="/admin/orders"
                  className="block w-full text-left px-4 py-3 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-center">
                    <ShoppingCart className="w-5 h-5 text-gray-400 mr-3" />
                    <div>
                      <p className="text-sm font-medium text-gray-900">Manage Orders</p>
                      <p className="text-sm text-gray-600">View and process orders</p>
                    </div>
                  </div>
                </a>
                
                <a
                  href="/admin/users"
                  className="block w-full text-left px-4 py-3 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-center">
                    <Users className="w-5 h-5 text-gray-400 mr-3" />
                    <div>
                      <p className="text-sm font-medium text-gray-900">Manage Users</p>
                      <p className="text-sm text-gray-600">View and manage user accounts</p>
                    </div>
                  </div>
                </a>
                
                <a
                  href="/admin/analytics"
                  className="block w-full text-left px-4 py-3 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-center">
                    <TrendingUp className="w-5 h-5 text-gray-400 mr-3" />
                    <div>
                      <p className="text-sm font-medium text-gray-900">View Analytics</p>
                      <p className="text-sm text-gray-600">Detailed reports and insights</p>
                    </div>
                  </div>
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default AdminDashboard;
