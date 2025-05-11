import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ShoppingCart, Heart, User, Menu, X, Search, PlusCircle } from 'lucide-react';
import Logo from './Logo';
import { useAuth } from '../../contexts/AuthContext';
import { useCart } from '../../contexts/CartContext';
import { Package } from 'lucide-react'; // Add to your imports

const Navbar: React.FC = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const { user, logout } = useAuth();
  const { cartItems } = useCart();
  const navigate = useNavigate();
  const loggedInUser = localStorage.getItem('user');

  const handleProfileClick = () => {
    setIsMenuOpen(!isMenuOpen); // Toggle the menu visibility on click
  };

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery)}`);
      setSearchQuery('');
    }
  };

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const logoutHandle = () => {
    localStorage.removeItem('user')
    navigate('/login')
  }

  


  return (
    <nav className="bg-white shadow-md sticky top-0 z-50">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center">
            <Logo />
            <span className="ml-2 text-xl font-bold text-blue-600">PopNShop</span>
          </Link>

          {/* Search - Hidden on mobile */}
          <div className="hidden md:block flex-grow max-w-md mx-4">
            <form onSubmit={handleSearchSubmit} className="relative">
              <input
                type="text"
                placeholder="Search products..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <Search className="absolute left-3 top-2.5 w-5 h-5 text-gray-400" />
            </form>
          </div>

          {/* Desktop Navigation Links */}
          <div className="hidden md:flex items-center space-x-4">
            {loggedInUser && (
              <Link 
                to="/sell" 
                className="flex items-center px-4 py-2 text-sm font-medium text-white bg-yellow-400 rounded-md hover:bg-yellow-500 transition-colors"
              >
                <PlusCircle size={16} className="mr-2" />
                Sell
              </Link>
            )}
          {/* Desktop Navigation Links */}
          
            {loggedInUser && (
                <Link
                  to="/myproducts"
                  className="flex items-center gap-2 text-gray-700 hover:text-blue-600 transition-colors"
                >
                  <Package size={18} />
                  My Products
                </Link>
            )}
            <Link 
              to="/cart" 
              className="relative p-2 text-gray-700 hover:text-blue-600 transition-colors"
            >
              <ShoppingCart size={20} />
              {cartItems.length > 0 && (
                <span className="absolute -top-1 -right-1 bg-yellow-400 text-xs w-5 h-5 flex items-center justify-center rounded-full font-bold">
                  {cartItems.length}
                </span>
              )}
            </Link>
            {/* <Link 
              to="/wishlist" 
              className="p-2 text-gray-700 hover:text-blue-600 transition-colors"
            >
              <Heart size={20} />
            </Link> */}
            {loggedInUser ? 
            // (
            //   <div className="relative group">
            //     <button onClick={handleProfile} className="flex items-center space-x-1 text-gray-700 hover:text-blue-600 transition-colors">
            //       <User size={20} />
            //       <span className="font-medium">{user.name}</span>
            //     </button>
            //     <div className="absolute right-0 w-48 mt-2 bg-white rounded-md shadow-lg overflow-hidden z-10 hidden group-hover:block">
            //       <Link 
            //         to="/profile" 
            //         className="block px-4 py-2 text-sm text-gray-700 hover:bg-blue-50"
            //       >
            //         Profile
            //       </Link>
            //       <button 
            //         onClick={logout}
            //         className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-blue-50"
            //       >
            //         Logout
            //       </button>
            //     </div>
            //   </div>
            // )
              (
                <div className="relative">
                  <button
                    onClick={handleProfileClick}
                    className="flex items-center space-x-1 text-gray-700 hover:text-blue-600 transition-colors"
                  >
                    <User size={20} />
                    {/* <span className="font-medium">{loggedInUser.email}</span> */}
                  </button>
                  {isMenuOpen && (
                    <div className="absolute right-0 w-48 mt-2 bg-white rounded-md shadow-lg overflow-hidden z-10">
                      <Link
                        to="/profile"
                        className="block px-4 py-2 text-sm text-gray-700 hover:bg-blue-50"
                        onClick={() => setIsMenuOpen(false)} // Close menu after clicking
                      >
                        Profile
                      </Link>
                      <button
                        onClick={logoutHandle}
                        className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-blue-50"
                      >
                        Logout
                      </button>
                    </div>
                  )}
                </div>
              ) 
            : (
              <Link 
                to="/login" 
                className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 transition-colors"
              >
                Sign In
              </Link>
            )}
          </div>

          {/* Mobile Menu Button */}
          <button 
            className="md:hidden p-2" 
            onClick={toggleMenu}
            aria-label="Toggle menu"
          >
            {isMenuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>

        {/* Mobile Search */}
        <div className="md:hidden pb-3">
          <form onSubmit={handleSearchSubmit} className="relative">
            <input
              type="text"
              placeholder="Search products..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <Search className="absolute left-3 top-2.5 w-5 h-5 text-gray-400" />
          </form>
        </div>
      </div>

      {/* Mobile Menu */}
      {isMenuOpen && (
        <div className="md:hidden bg-white shadow-lg py-4 px-6 absolute w-full z-50 animate-pop">
          <div className="flex flex-col space-y-4">
            {user && (
              <Link 
                to="/sell" 
                className="flex items-center space-x-2 py-2 text-gray-700"
                onClick={() => setIsMenuOpen(false)}
              >
                <PlusCircle size={20} />
                <span>Sell an Item</span>
              </Link>
            )}
            {user && (
              <Link 
                to="/sell" 
                className="flex items-center space-x-2 py-2 text-gray-700"
                onClick={() => setIsMenuOpen(false)}
              >
                <PlusCircle size={20} />
                <span>Sell an Item</span>
              </Link>
            )}
            <Link 
              to="/cart" 
              className="flex items-center space-x-2 py-2 text-gray-700"
              onClick={() => setIsMenuOpen(false)}
            >
              <ShoppingCart size={20} />
              <span>Cart</span>
              {cartItems.length > 0 && (
                <span className="bg-yellow-400 text-xs px-2 py-1 rounded-full font-bold">
                  {cartItems.length}
                </span>
              )}
            </Link>
            {/* <Link 
              to="/wishlist" 
              className="flex items-center space-x-2 py-2 text-gray-700"
              onClick={() => setIsMenuOpen(false)}
            >
              <Heart size={20} />
              <span>Wishlist</span>
            </Link> */}
            {user ? (
              <>
                <Link 
                  to="/profile" 
                  className="flex items-center space-x-2 py-2 text-gray-700"
                  onClick={() => setIsMenuOpen(false)}
                >
                  <User size={20} />
                  <span>Profile</span>
                </Link>
                <button 
                  onClick={() => {
                    logout();
                    setIsMenuOpen(false);
                  }}
                  className="flex items-center space-x-2 py-2 text-gray-700"
                >
                  <span>Logout</span>
                </button>
              </>
            ) : (
              <Link 
                to="/login" 
                className="flex items-center space-x-2 py-2 text-gray-700"
                onClick={() => setIsMenuOpen(false)}
              >
                <span>Sign In</span>
              </Link>
            )}
          </div>
        </div>
      )}
    </nav>
  );
};

export default Navbar;