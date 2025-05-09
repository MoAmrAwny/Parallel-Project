import React from 'react';
import { Link } from 'react-router-dom';
import { Facebook, Twitter, Instagram, Mail } from 'lucide-react';
import Logo from './Logo';

const Footer: React.FC = () => {
  return (
    <footer className="bg-blue-950 text-white pt-12 pb-8">
      <div className="container mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Company Info */}
          <div className="space-y-4">
            <div className="flex items-center">
              <Logo />
              <span className="ml-2 text-xl font-bold">PopNShop</span>
            </div>
            <p className="text-gray-300 text-sm">
              Click, Pop, and Shop! The marketplace designed for university students and young adults.
            </p>
            <div className="flex space-x-4">
              <a href="#" className="text-gray-300 hover:text-white transition-colors">
                <Facebook size={20} />
              </a>
              <a href="#" className="text-gray-300 hover:text-white transition-colors">
                <Twitter size={20} />
              </a>
              <a href="#" className="text-gray-300 hover:text-white transition-colors">
                <Instagram size={20} />
              </a>
              <a href="#" className="text-gray-300 hover:text-white transition-colors">
                <Mail size={20} />
              </a>
            </div>
          </div>
          
          {/* Shop */}
          <div>
            <h3 className="font-semibold text-lg mb-4">Shop</h3>
            <ul className="space-y-2">
              <li><Link to="/search?category=electronics" className="text-gray-300 hover:text-white transition-colors">Electronics</Link></li>
              <li><Link to="/search?category=clothing" className="text-gray-300 hover:text-white transition-colors">Clothing</Link></li>
              <li><Link to="/search?category=books" className="text-gray-300 hover:text-white transition-colors">Books</Link></li>
              <li><Link to="/search?category=furniture" className="text-gray-300 hover:text-white transition-colors">Furniture</Link></li>
              <li><Link to="/search" className="text-gray-300 hover:text-white transition-colors">All Categories</Link></li>
            </ul>
          </div>
          
          {/* Support */}
          <div>
            <h3 className="font-semibold text-lg mb-4">Support</h3>
            <ul className="space-y-2">
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">Help Center</a></li>
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">Safety Tips</a></li>
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">Returns & Refunds</a></li>
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">Contact Us</a></li>
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">FAQs</a></li>
            </ul>
          </div>
          
          {/* Legal */}
          <div>
            <h3 className="font-semibold text-lg mb-4">Legal</h3>
            <ul className="space-y-2">
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">Terms of Service</a></li>
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">Privacy Policy</a></li>
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">Cookie Policy</a></li>
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">Seller Guidelines</a></li>
              <li><a href="#" className="text-gray-300 hover:text-white transition-colors">User Agreement</a></li>
            </ul>
          </div>
        </div>
        
        <div className="border-t border-gray-700 mt-12 pt-8 text-center text-gray-400 text-sm">
          <p>&copy; {new Date().getFullYear()} PopNShop. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;