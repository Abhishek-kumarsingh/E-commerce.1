import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Home from './pages/Home';
import Products from './pages/Products';
import ProductDetail from './pages/ProductDetail';
import Login from './pages/Login';
import Checkout from './pages/Checkout';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="products" element={<Products />} />
          <Route path="product/:id" element={<ProductDetail />} />
          <Route path="category/:slug" element={<Products />} />
          <Route path="search" element={<Products />} />
          <Route path="login" element={<Login />} />
          <Route path="checkout" element={<Checkout />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;