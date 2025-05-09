import React, { useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { Trash2, Plus, Minus, ArrowLeft, ShoppingCart, Loader2 } from 'lucide-react';
import { useCart } from '../contexts/CartContext';
import { loadStripe } from '@stripe/stripe-js';
import { Payment } from './../../node_modules/@stripe/stripe-js/types/api/orders.d';

const stripePromise = loadStripe('your_publishable_key');

const CartPage: React.FC = () => {
  const { cartItems, removeFromCart, updateQuantity, totalPrice, clearCart } = useCart();
  const [isProcessing, setIsProcessing] = useState(false);
  const navigate = useNavigate();

  const formatPrice = (price: number): string => {
    return price.toLocaleString('en-US', {
      style: 'currency',
      currency: 'USD',
    });
  };

  const handleIncreaseQuantity = (productId: string, currentQuantity: number) => {
    updateQuantity(productId, currentQuantity + 1);
  };

  const handleDecreaseQuantity = (productId: string, currentQuantity: number) => {
    if (currentQuantity > 1) {
      updateQuantity(productId, currentQuantity - 1);
    } else {
      removeFromCart(productId);
    }
  };

  const handleCheckout = async () => {
    setIsProcessing(true);
    try {
      // const stripe = await stripePromise;
      // if (!stripe) throw new Error('Stripe failed to load');
      

      // In a real app, this would be an API call to your backend
      // const response = await fetch('/api/create-checkout-session', {
      //   method: 'POST',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify({ items: cartItems }),
      // });
      // const session = await response.json();
      // await stripe.redirectToCheckout({ sessionId: session.id });

      // For demo purposes, we'll just show a success message
      await new Promise(resolve => setTimeout(resolve, 2000));
      alert('Checkout successful! This is a demo, no actual payment was processed.');
      navigate('/payment');

      // clearCart();
    } catch (error) {
      console.error('Error during checkout:', error);
      alert('Something went wrong during checkout. Please try again.');
    } finally {
      setIsProcessing(false);
    }
  };

  if (cartItems.length === 0) {
    return (
      <div className="container mx-auto px-4 py-16 text-center">
        <div className="max-w-md mx-auto">
          <ShoppingCart size={64} className="mx-auto text-gray-300 mb-6" />
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Your cart is empty</h2>
          <p className="text-gray-600 mb-8">Looks like you haven't added any products to your cart yet.</p>
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
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Your Shopping Cart</h1>
        
        <div className="flex flex-col lg:flex-row gap-8">
          {/* Cart Items */}
          <div className="lg:flex-grow">
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
              <ul className="divide-y divide-gray-200">
                {cartItems.map((item) => (
                  <li key={item.id} className="p-6">
                    <div className="flex flex-col sm:flex-row gap-4">
                      <div className="flex-shrink-0 w-24 h-24">
                        <img 
                          src={item.images[0]} 
                          alt={item.name} 
                          className="w-full h-full object-cover rounded-md"
                        />
                      </div>
                      
                      <div className="flex-grow">
                        <div className="flex justify-between mb-1">
                          <Link 
                            to={`/product/${item.id}`} 
                            className="text-lg font-medium text-gray-900 hover:text-blue-600"
                          >
                            {item.name}
                          </Link>
                          <span className="font-bold text-blue-600">
                            {formatPrice(item.price)}
                          </span>
                        </div>
                        
                        <p className="text-sm text-gray-500 mb-1">{item.seller.name}</p>
                        <p className="text-sm text-gray-500 mb-4 capitalize">{item.condition}</p>
                        
                        <div className="flex justify-between items-center">
                          <div className="flex items-center">
                            <button
                              onClick={() => handleDecreaseQuantity(item.id, item.quantity)}
                              className="h-8 w-8 border border-gray-300 flex items-center justify-center rounded-l-md text-gray-600 hover:bg-gray-100"
                              aria-label="Decrease quantity"
                            >
                              <Minus size={14} />
                            </button>
                            <input
                              type="text"
                              value={item.quantity}
                              readOnly
                              className="h-8 w-12 border-t border-b border-gray-300 text-center text-sm [-moz-appearance:_textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                            />
                            <button
                              onClick={() => handleIncreaseQuantity(item.id, item.quantity)}
                              className="h-8 w-8 border border-gray-300 flex items-center justify-center rounded-r-md text-gray-600 hover:bg-gray-100"
                              aria-label="Increase quantity"
                            >
                              <Plus size={14} />
                            </button>
                          </div>
                          
                          <div className="flex items-center">
                            <span className="text-sm text-gray-500 mr-2">Subtotal:</span>
                            <span className="font-medium">
                              {formatPrice(item.price * item.quantity)}
                            </span>
                          </div>
                          
                          <button
                            onClick={() => removeFromCart(item.id)}
                            className="p-2 text-gray-400 hover:text-red-500 transition-colors"
                            aria-label="Remove item"
                          >
                            <Trash2 size={18} />
                          </button>
                        </div>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
            
            <div className="mt-6 flex justify-between items-center">
              <Link 
                to="/" 
                className="inline-flex items-center text-blue-600 hover:text-blue-700"
              >
                <ArrowLeft size={16} className="mr-1" /> Continue Shopping
              </Link>
              
              <button
                onClick={clearCart}
                className="text-red-500 hover:text-red-700 font-medium"
              >
                Clear Cart
              </button>
            </div>
          </div>
          
          {/* Order Summary */}
          <div className="lg:w-80 flex-shrink-0">
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 sticky top-20">
              <h2 className="text-lg font-bold text-gray-900 mb-4">Order Summary</h2>
              
              <div className="space-y-3 mb-6">
                <div className="flex justify-between text-gray-600">
                  <span>Subtotal</span>
                  <span>{formatPrice(totalPrice)}</span>
                </div>
                <div className="flex justify-between text-gray-600">
                  <span>Shipping</span>
                  <span>Free</span>
                </div>
                <div className="flex justify-between text-gray-600">
                  <span>Tax</span>
                  <span>{formatPrice(totalPrice * 0.1)}</span>
                </div>
                <div className="border-t border-gray-200 pt-3 flex justify-between font-bold text-gray-900">
                  <span>Total</span>
                  <span>{formatPrice(totalPrice * 1.1)}</span>
                </div>
              </div>
              
              <button
                onClick={handleCheckout}
                disabled={isProcessing}
                className="w-full py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium disabled:bg-blue-300 flex items-center justify-center"
              >
                {isProcessing ? (
                  <>
                    <Loader2 size={18} className="animate-spin mr-2" />
                    Processing...
                  </>
                ) : (
                  'Proceed to Checkout'
                )}
              </button>
              
              <div className="mt-4 text-xs text-gray-500 text-center">
                <p>Secure payment processing</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartPage;