import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, Zap, Shield, Star, TrendingUp } from 'lucide-react';
import ProductCard from '../components/Products/ProductCard';
import { mockProducts } from '../data/mockProducts';
import CategoryCard from '../components/Products/CategoryCard';
// import { useEffect, useState } from "react";

const categories = [
  { id: 'electronics', name: 'Electronics', icon: 'laptop', count: 243 },
  { id: 'furniture', name: 'Furniture', icon: 'sofa', count: 156 },
  { id: 'clothing', name: 'Clothing', icon: 'shirt', count: 389 },
  { id: 'books', name: 'Books', icon: 'book', count: 217 },
  { id: 'sports', name: 'Sports', icon: 'bike', count: 194 },
  { id: 'collectibles', name: 'Collectibles', icon: 'diamond', count: 132 }
];

const HomePage: React.FC = () => {

  // const [message, setMessage] = useState("");


  return (
    <div className="bg-white">
      {/* Hero Section */}
      <section className="relative bg-blue-600 text-white">
        <div className="container mx-auto px-4 py-24 flex flex-col lg:flex-row items-center">
          <div className="lg:w-1/2 mb-12 lg:mb-0">
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold mb-6 leading-tight">
              Click, Pop, and Shop!
            </h1>
            <p className="text-xl mb-8 text-blue-100">
              The marketplace designed for university students and young adults.
              Buy and sell items easily, securely, and hassle-free.
            </p>
            <div className="flex flex-wrap gap-4">
              <Link 
                to="/products" 
                className="px-6 py-3 bg-yellow-400 text-blue-950 font-semibold rounded-full hover:bg-yellow-300 transition-colors"
              >
                Start Shopping
              </Link>
              <Link 
                to="/sell" 
                className="px-6 py-3 bg-white text-blue-600 font-semibold rounded-full hover:bg-blue-50 transition-colors"
              >
                Sell an Item
              </Link>
            </div>
          </div>
          <div className="lg:w-1/2 relative">
            {/* Abstract shape backgrounds */}
            <div className="absolute -top-10 -left-10 w-40 h-40 bg-yellow-400 rounded-full opacity-20"></div>
            <div className="absolute top-40 -right-5 w-20 h-20 bg-blue-300 rounded-full opacity-30"></div>
            
            {/* Hero image */}
            <img 
              src="https://images.pexels.com/photos/5632402/pexels-photo-5632402.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" 
              alt="Young adult shopping online" 
              className="relative z-10 rounded-lg shadow-xl w-full" 
            />
          </div>
        </div>
        
        {/* Curved shape at bottom */}
        <div className="absolute bottom-0 left-0 w-full overflow-hidden">
          <svg 
            className="relative block w-full h-16" 
            viewBox="0 0 1200 120" 
            preserveAspectRatio="none"
          >
            <path 
              d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z" 
              fill="#ffffff" 
              opacity="1"
            ></path>
          </svg>
        </div>
      </section>

      {/* Categories Section */}
      <section className="py-16">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">Browse Categories</h2>
            <p className="text-lg text-gray-600 max-w-2xl mx-auto">
              Find exactly what you're looking for from our wide range of categories
            </p>
          </div>
          
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
            {categories.map(category => (
              <CategoryCard key={category.id} category={category} />
            ))}
          </div>
        </div>
      </section>

      {/* Featured Products
      <section className="py-16 bg-gray-50">
        <div className="container mx-auto px-4">
          <div className="flex justify-between items-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900">Featured Products</h2>
            <Link 
              to="/search" 
              className="flex items-center text-blue-600 font-semibold hover:text-blue-700 transition-colors"
            >
              View all <ArrowRight size={18} className="ml-1" />
            </Link>
          </div>
          
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {mockProducts.slice(0, 4).map(product => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        </div>
      </section> */}

      {/* Features Section */}
      <section className="py-20">
        <div className="container mx-auto px-4">
          <div className="text-center mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">Why Choose PopNShop?</h2>
            <p className="text-lg text-gray-600 max-w-2xl mx-auto">
              The smart way to buy and sell items in your community
            </p>
          </div>
          
          <div className="grid md:grid-cols-3 gap-8">
            <div className="bg-white p-8 rounded-xl shadow-md text-center">
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6">
                <Zap className="text-blue-600" size={28} />
              </div>
              <h3 className="text-xl font-semibold mb-4">Fast & Easy</h3>
              <p className="text-gray-600">
                List items in minutes and find what you're looking for with our powerful search filters.
              </p>
            </div>
            
            <div className="bg-white p-8 rounded-xl shadow-md text-center">
              <div className="w-16 h-16 bg-yellow-100 rounded-full flex items-center justify-center mx-auto mb-6">
                <Shield className="text-yellow-600" size={28} />
              </div>
              <h3 className="text-xl font-semibold mb-4">Secure Transactions</h3>
              <p className="text-gray-600">
                Our secure payment system and user verification keeps you protected.
              </p>
            </div>
            
            <div className="bg-white p-8 rounded-xl shadow-md text-center">
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
                <Star className="text-green-600" size={28} />
              </div>
              <h3 className="text-xl font-semibold mb-4">Trusted Community</h3>
              <p className="text-gray-600">
                Verified reviews and ratings help you buy and sell with confidence.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Call to Action */}
      <section className="py-20 bg-blue-950 text-white">
        <div className="container mx-auto px-4 text-center">
          <h2 className="text-3xl font-bold mb-6">Ready to start selling?</h2>
          <p className="text-xl mb-8 max-w-2xl mx-auto">
            Turn your unused items into cash! Join thousands of students making extra money on PopNShop.
          </p>
          <Link 
            to="/sell" 
            className="inline-flex items-center px-6 py-3 bg-yellow-400 text-blue-950 font-semibold rounded-full hover:bg-yellow-300 transition-colors"
          >
            <TrendingUp size={20} className="mr-2" />
            Start Selling Now
          </Link>
        </div>
      </section>
    </div>
  );
};

export default HomePage;