// import React from 'react';
// import ProductCard from '../components/Products/ProductCard';
// import { mockProducts } from '../data/mockProducts';

// const MyProductsPage: React.FC = () => {
//     return (
//         <div className="bg-white min-h-screen py-16">
//             <div className="container mx-auto px-4">
//                 <h1 className="text-3xl font-bold text-gray-900 mb-6 text-center">My Products</h1>
//                 {mockProducts.length > 0 ? (
//                     <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
//                         {mockProducts.map(product => (
//                             <ProductCard key={product.id} product={product} />
//                         ))}
//                     </div>
//                 ) : (
//                     <p className="text-center text-gray-500 text-lg">No products available.</p>
//                 )}
//             </div>
//         </div>
//     );
// };

// export default MyProductsPage;

import React, { useState } from 'react';
import { mockProducts as initialProducts } from '../data/mockProducts';
import DashboardProductCard from './DashboardProductCard';

const MyProductsPage: React.FC = () => {
    const [products, setProducts] = useState(initialProducts);

    const handleRemove = (id: string) => {
        setProducts(prev => prev.filter(p => p.id !== id));
    };

    const handleMarkSold = (id: string) => {
        alert(`Product ${id} marked as sold.`); // Replace with real logic
    };

    const handleEdit = (id: string) => {
        alert(`Redirect to edit product ${id}`); // Replace with routing to edit page
    };

    return (
        <div className="bg-white min-h-screen py-16">
            <div className="container mx-auto px-4">
                <h1 className="text-3xl font-bold text-gray-900 mb-6 text-center">My Dashboard</h1>
                {products.length > 0 ? (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                        {products.map(product => (
                            <DashboardProductCard
                                key={product.id}
                                product={product}
                                onRemove={handleRemove}
                                onMarkSold={handleMarkSold}
                                onEdit={handleEdit}
                            />
                        ))}
                    </div>
                ) : (
                    <p className="text-center text-gray-500 text-lg">No products available.</p>
                )}
            </div>
        </div>
    );
};

export default MyProductsPage;

