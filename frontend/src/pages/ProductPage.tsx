// import React, { useState } from 'react';
// import { useParams, Link } from 'react-router-dom';
// import { ChevronLeft, ChevronRight, MapPin, Star, ShoppingCart, Heart, Share2, ArrowLeft } from 'lucide-react';
// import { getProductById, getRelatedProducts } from '../data/mockProducts';
// import { useCart } from '../contexts/CartContext';
// import { useWishlist } from '../contexts/WishlistContext';
// import ProductCard from '../components/Products/ProductCard';
// import { motion } from 'framer-motion';

// const ProductPage: React.FC = () => {
//   const { id } = useParams<{ id: string }>();
//   const product = getProductById(id || '');
//   const relatedProducts = product ? getRelatedProducts(product.category, product.id) : [];
//   const [currentImageIndex, setCurrentImageIndex] = useState(0);
//   const [quantity, setQuantity] = useState(1);
//   const { addToCart } = useCart();
//   const { addToWishlist, removeFromWishlist, isInWishlist } = useWishlist();

//   if (!product) {
//     return (
//       <div className="container mx-auto px-4 py-16 text-center">
//         <h2 className="text-2xl font-bold text-gray-900 mb-4">Product not found</h2>
//         <Link 
//           to="/" 
//           className="inline-flex items-center text-blue-600 hover:text-blue-700"
//         >
//           <ArrowLeft size={16} className="mr-1" /> Back to Home
//         </Link>
//       </div>
//     );
//   }

//   const nextImage = () => {
//     setCurrentImageIndex((prevIndex) => 
//       prevIndex === product.images.length - 1 ? 0 : prevIndex + 1
//     );
//   };

//   const prevImage = () => {
//     setCurrentImageIndex((prevIndex) => 
//       prevIndex === 0 ? product.images.length - 1 : prevIndex - 1
//     );
//   };

//   const handleWishlistToggle = () => {
//     if (isInWishlist(product.id)) {
//       removeFromWishlist(product.id);
//     } else {
//       addToWishlist(product);
//     }
//   };

//   const formatPrice = (price: number): string => {
//     return price.toLocaleString('en-US', {
//       style: 'currency',
//       currency: 'USD',
//     });
//   };

//   const getConditionLabel = (condition: string) => {
//     const conditionLabels: Record<string, { color: string; label: string }> = {
//       'new': { color: 'bg-green-100 text-green-800', label: 'New' },
//       'like new': { color: 'bg-blue-100 text-blue-800', label: 'Like New' },
//       'good': { color: 'bg-yellow-100 text-yellow-800', label: 'Good' },
//       'fair': { color: 'bg-orange-100 text-orange-800', label: 'Fair' },
//       'poor': { color: 'bg-red-100 text-red-800', label: 'Poor' },
//     };
    
//     const { color, label } = conditionLabels[condition] || conditionLabels['good'];
    
//     return (
//       <span className={`px-3 py-1 rounded-full ${color} text-sm font-medium`}>
//         {label}
//       </span>
//     );
//   };

//   return (
//     <div className="bg-white">
//       <div className="container mx-auto px-4 py-8">
//         {/* Breadcrumbs */}
//         <nav className="mb-8">
//           <ol className="flex text-sm text-gray-500">
//             <li className="hover:text-blue-600">
//               <Link to="/">Home</Link>
//             </li>
//             <li className="mx-2">/</li>
//             <li className="hover:text-blue-600">
//               <Link to={`/search?category=${product.category}`}>
//                 {product.category.charAt(0).toUpperCase() + product.category.slice(1)}
//               </Link>
//             </li>
//             <li className="mx-2">/</li>
//             <li className="text-gray-900 font-medium truncate">{product.name}</li>
//           </ol>
//         </nav>

//         <div className="grid grid-cols-1 md:grid-cols-2 gap-12">
//           {/* Product Images */}
//           <div>
//             <div className="relative rounded-xl overflow-hidden mb-4 h-80 md:h-96 bg-gray-100">
//               <motion.img 
//                 key={currentImageIndex}
//                 initial={{ opacity: 0 }}
//                 animate={{ opacity: 1 }}
//                 transition={{ duration: 0.3 }}
//                 src={product.images[currentImageIndex]} 
//                 alt={product.name} 
//                 className="w-full h-full object-contain"
//               />
              
//               {product.images.length > 1 && (
//                 <>
//                   <button 
//                     onClick={prevImage}
//                     className="absolute left-2 top-1/2 transform -translate-y-1/2 bg-white p-2 rounded-full shadow-md text-gray-700 hover:text-blue-600 focus:outline-none"
//                     aria-label="Previous image"
//                   >
//                     <ChevronLeft size={20} />
//                   </button>
//                   <button 
//                     onClick={nextImage}
//                     className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-white p-2 rounded-full shadow-md text-gray-700 hover:text-blue-600 focus:outline-none"
//                     aria-label="Next image"
//                   >
//                     <ChevronRight size={20} />
//                   </button>
//                 </>
//               )}
//             </div>
            
//             {/* Thumbnail Images */}
//             {product.images.length > 1 && (
//               <div className="flex space-x-2 overflow-x-auto pb-2">
//                 {product.images.map((img, index) => (
//                   <button 
//                     key={index}
//                     onClick={() => setCurrentImageIndex(index)}
//                     className={`w-20 h-20 flex-shrink-0 rounded-md overflow-hidden border-2 ${
//                       currentImageIndex === index ? 'border-blue-600' : 'border-transparent'
//                     }`}
//                   >
//                     <img 
//                       src={img} 
//                       alt={`${product.name} thumbnail ${index + 1}`} 
//                       className="w-full h-full object-cover"
//                     />
//                   </button>
//                 ))}
//               </div>
//             )}
//           </div>
          
//           {/* Product Details */}
//           <div>
//             <h1 className="text-3xl font-bold text-gray-900 mb-2">{product.name}</h1>
            
//             <div className="flex items-center mb-4">
//               <div className="flex items-center text-sm text-gray-500 mr-4">
//                 <span className="font-medium mr-1">Seller:</span>
//                 <span>{product.seller.name}</span>
//               </div>
//               <div className="flex items-center">
//                 <div className="flex">
//                   {[...Array(5)].map((_, index) => (
//                     <Star 
//                       key={index}
//                       size={16} 
//                       className={index < Math.floor(product.seller.rating) ? 'text-yellow-400 fill-current' : 'text-gray-300'} 
//                     />
//                   ))}
//                 </div>
//                 <span className="ml-1 text-sm text-gray-600">{product.seller.rating}</span>
//               </div>
//             </div>
            
//             <div className="flex items-center mb-6">
//               <span className="text-3xl font-bold text-blue-600 mr-3">
//                 {formatPrice(product.price)}
//               </span>
//               <div className="ml-auto">
//                 {getConditionLabel(product.condition)}
//               </div>
//             </div>
            
//             <div className="mb-6">
//               <p className="text-gray-700 mb-4">
//                 {product.description}
//               </p>
              
//               {product.features && product.features.length > 0 && (
//                 <div className="mt-4">
//                   <h3 className="font-medium text-gray-900 mb-2">Features:</h3>
//                   <ul className="list-disc list-inside text-gray-700 space-y-1">
//                     {product.features.map((feature, index) => (
//                       <li key={index}>{feature}</li>
//                     ))}
//                   </ul>
//                 </div>
//               )}
//             </div>
            
//             <div className="flex items-center text-sm text-gray-700 mb-6">
//               <MapPin size={16} className="mr-1" />
//               <span>{product.location}</span>
//             </div>
            
//             {/* Quantity Selector */}
//             <div className="mb-6">
//               <label htmlFor="quantity" className="block text-sm font-medium text-gray-700 mb-2">
//                 Quantity
//               </label>
//               <div className="flex items-center">
//                 <button
//                   onClick={() => setQuantity(q => Math.max(1, q - 1))}
//                   className="h-10 w-10 border border-gray-300 flex items-center justify-center rounded-l-md text-gray-600 hover:bg-gray-100"
//                 >
//                   -
//                 </button>
//                 <input
//                   type="number"
//                   id="quantity"
//                   name="quantity"
//                   min="1"
//                   value={quantity}
//                   onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
//                   className="h-10 w-14 border-t border-b border-gray-300 text-center [-moz-appearance:_textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
//                 />
//                 <button
//                   onClick={() => setQuantity(q => q + 1)}
//                   className="h-10 w-10 border border-gray-300 flex items-center justify-center rounded-r-md text-gray-600 hover:bg-gray-100"
//                 >
//                   +
//                 </button>
//               </div>
//             </div>
            
//             {/* Action Buttons */}
//             <div className="flex flex-col sm:flex-row gap-4 mb-8">
//               <button
//                 onClick={() => addToCart(product, quantity)}
//                 className="flex-1 flex items-center justify-center gap-2 py-3 px-4 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
//               >
//                 <ShoppingCart size={18} />
//                 <span>Add to Cart</span>
//               </button>
              
//               <button
//                 onClick={handleWishlistToggle}
//                 className={`flex items-center justify-center gap-2 py-3 px-4 rounded-lg font-medium border ${
//                   isInWishlist(product.id)
//                     ? 'bg-red-50 text-red-600 border-red-200 hover:bg-red-100'
//                     : 'bg-gray-50 text-gray-700 border-gray-200 hover:bg-gray-100'
//                 } transition-colors`}
//               >
//                 <Heart 
//                   size={18} 
//                   fill={isInWishlist(product.id) ? 'currentColor' : 'none'} 
//                 />
//                 <span>
//                   {isInWishlist(product.id) ? 'Saved' : 'Save'}
//                 </span>
//               </button>
              
//               <button
//                 className="hidden sm:flex items-center justify-center gap-2 py-3 px-4 rounded-lg font-medium bg-gray-50 text-gray-700 border border-gray-200 hover:bg-gray-100 transition-colors"
//               >
//                 <Share2 size={18} />
//               </button>
//             </div>
//           </div>
//         </div>
        
//         {/* Related Products */}
//         {relatedProducts.length > 0 && (
//           <div className="mt-16">
//             <h2 className="text-2xl font-bold text-gray-900 mb-6">Similar Products</h2>
//             <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
//               {relatedProducts.map(relProduct => (
//                 <ProductCard key={relProduct.id} product={relProduct} />
//               ))}
//             </div>
//           </div>
//         )}
//       </div>
//     </div>
//   );
// };

// export default ProductPage;


import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

interface ItemDTO {
  itemId: number;
  name: string;
  description: string;
  price: number;
  category: string;
  available: boolean;
  condition: string;
  quantity: number;
  imageName: string;
  imageType: string;
  imageData: string; // base64 string
}

const ProductPage: React.FC = () => {
  const [products, setProducts] = useState<ItemDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const userId = localStorage.getItem('user');
        if (!userId) throw new Error('Not logged in');

        const res = await fetch(`http://localhost:8080/items`);
        if (!res.ok) throw new Error(`Server responded ${res.status}`);
        const data: ItemDTO[] = await res.json();
        setProducts(data);
      } catch (err: any) {
        setError(err.message || 'Failed to load products');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  const handleAddToCart = async (itemId: number) => {
    try {

      const raw = localStorage.getItem('user');

      // 2. Parse it (fallback to an empty object if it's not there)
      const user = raw ? JSON.parse(raw) as { accId?: number } : {};

      // 3. Extract loginOwnerId (might be undefined) and coerce to string
      const userIdStr = user.accId != null
        ? String(user.accId)    // e.g. "4"
        : '';                
        console.log(userIdStr);
        console.log(itemId);
      if (!userIdStr) throw new Error('Not logged in');

      const response = await fetch(`http://localhost:8080/account/${userIdStr}/cart/add`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          item_id: itemId,
          quantity: 1, // Default quantity of 1
        }),
      });

      if (!response.ok) throw new Error(`Failed to add item to cart: ${response.statusText}`);

      // Optionally, handle the response (e.g., show a success message)
      alert('Item added to cart!');
    } catch (err: any) {
      alert(err.message || 'Error adding item to cart');
    }
  };



  // if (loading) return <div className="text-center py-8">Loading your items…</div>;
  // if (error) return <div className="text-center text-red-500 py-8">Error: {error}</div>;
  // if (products.length === 0) return <div className="text-center py-8">No items found.</div>;

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 p-6">
      {products.map(item => (
        <div
          key={item.itemId}
          className="bg-white rounded-lg p-4 shadow hover:shadow-lg transition-shadow flex flex-col"
        >
          <div className="h-48 bg-gray-100 flex items-center justify-center mb-4">
            <img
              src={`data:${item.imageType};base64,${item.imageData}`}
              alt={item.name}
              className="object-contain max-h-full"
            />
          </div>

          <h3 className="text-lg font-semibold mb-1">{item.name}</h3>
          <p className="text-sm text-gray-600 mb-2">
            {item.category} • {item.condition}
          </p>
          <p className="font-bold text-xl mb-4">${item.price.toFixed(2)}</p>
          <p className="text-sm text-gray-700 flex-grow">{item.description}</p>

          <div className="mt-4 space-y-2">
            <button
              onClick={() => navigate(`/items/${item.itemId}`)}
              className="w-full py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
            >
              View Details
            </button>
            <button
              onClick={() => handleAddToCart(item.itemId)}
              className="w-full py-2 border border-blue-600 text-blue-600 rounded hover:bg-blue-50 transition-colors"
            >
              Add to Cart
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default ProductPage;
