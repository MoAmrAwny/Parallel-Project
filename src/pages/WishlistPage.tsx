import React from 'react';
import { Link } from 'react-router-dom';
import { Heart, ArrowLeft, Trash2, ShoppingCart } from 'lucide-react';
import { useWishlist } from '../contexts/WishlistContext';
import { useCart } from '../contexts/CartContext';

const WishlistPage: React.FC = () => {
  const { wishlistItems, removeFromWishlist, clearWishlist } = useWishlist();
  const { addToCart } = useCart();

  const formatPrice = (price: number): string => {
    return price.toLocaleString('en-US', {
      style: 'currency',
      currency: 'USD',
    });
  };

  if (wishlistItems.length === 0) {
    return (
      <div className="container mx-auto px-4 py-16 text-center">
        <div className="max-w-md mx-auto">
          <Heart size={64} className="mx-auto text-gray-300 mb-6" />
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Your wishlist is empty</h2>
          <p className="text-gray-600 mb-8">Save items you like for later by clicking the heart icon.</p>
          <Link 
            to="/" 
            className="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
          >
            <ArrowLeft size={16} className="mr-2" />
            Continue Shopping
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white">
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Your Wishlist</h1>
        
        <div className="mb-6 flex justify-between items-center">
          <p className="text-gray-600">
            {wishlistItems.length} {wishlistItems.length === 1 ? 'item' : 'items'} saved
          </p>
          
          <button
            onClick={clearWishlist}
            className="text-red-500 hover:text-red-700 font-medium flex items-center"
          >
            <Trash2 size={16} className="mr-1" />
            Clear Wishlist
          </button>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {wishlistItems.map(item => (
            <div key={item.id} className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
              <Link to={`/product/${item.id}`} className="block">
                <img 
                  src={item.images[0]} 
                  alt={item.name} 
                  className="w-full h-48 object-cover"
                />
                
                <div className="p-4">
                  <div className="flex justify-between mb-1">
                    <h3 className="font-medium text-gray-900">{item.name}</h3>
                    <span className="font-bold text-blue-600">{formatPrice(item.price)}</span>
                  </div>
                  
                  <p className="text-sm text-gray-500 line-clamp-2 mb-2">
                    {item.description}
                  </p>
                  
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-500 capitalize">{item.condition}</span>
                    <span className="text-sm text-gray-500">{item.seller.name}</span>
                  </div>
                </div>
              </Link>
              
              <div className="border-t border-gray-100 p-4 flex gap-2">
                <button 
                  onClick={() => addToCart(item)}
                  className="flex-1 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium flex items-center justify-center"
                >
                  <ShoppingCart size={16} className="mr-2" />
                  Add to Cart
                </button>
                
                <button 
                  onClick={() => removeFromWishlist(item.id)}
                  className="p-2 text-gray-400 hover:text-red-500 transition-colors border border-gray-200 rounded-lg"
                  aria-label="Remove from wishlist"
                >
                  <Trash2 size={16} />
                </button>
              </div>
            </div>
          ))}
        </div>
        
        <div className="mt-6">
          <Link 
            to="/" 
            className="inline-flex items-center text-blue-600 hover:text-blue-700"
          >
            <ArrowLeft size={16} className="mr-1" /> Continue Shopping
          </Link>
        </div>
      </div>
    </div>
  );
};

export default WishlistPage;