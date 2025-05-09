import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout/Layout';
import HomePage from './pages/HomePage';
import ProductPage from './pages/ProductPage';
import ProfilePage from './pages/ProfilePage';
import CartPage from './pages/CartPage';
import WishlistPage from './pages/WishlistPage';
import SearchPage from './pages/SearchPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import SellPage from './pages/SellPage';
import { AuthProvider } from './contexts/AuthContext';
import { CartProvider } from './contexts/CartContext';
import { WishlistProvider } from './contexts/WishlistContext';
import ProtectedRoute from './components/Auth/ProtectedRoute';
import MyProductsPage from './pages/MyProductsPage';
import PaymentPage from './pages/PaymentPage';



function App() {
  return (
    <Router>
      <AuthProvider>
        <CartProvider>
          <WishlistProvider>
            <Routes>
              <Route path="/" element={<Layout />}>
                <Route index element={<HomePage />} />
                <Route path="product/:id" element={<ProductPage />} />
                <Route path="search" element={<SearchPage />} />
                <Route path="login" element={<LoginPage />} />
                <Route path="register" element={<RegisterPage />} />
                <Route path="cart" element={<CartPage />} />
                <Route path="wishlist" element={<WishlistPage />} />
                <Route path="my-products" element={<MyProductsPage />} />
                <Route path="payment" element={<PaymentPage />} />
                <Route 
                  path="sell" 
                  element={
                    <ProtectedRoute>
                      <SellPage />
                    </ProtectedRoute>
                  } 
                />
                <Route 
                  path="profile" 
                  element={
                    <ProfilePage />
                    // <ProtectedRoute>
                    // </ProtectedRoute>
                  } 
                />
              </Route>
            </Routes>
          </WishlistProvider>
        </CartProvider>
      </AuthProvider>
    </Router>
  );
}

export default App;