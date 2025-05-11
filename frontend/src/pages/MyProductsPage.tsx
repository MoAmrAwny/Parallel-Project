import React, { useEffect, useState } from 'react';
import DashboardProductCard from './DashboardProductCard';

type Product = {
    itemId: number;
    name: string;
    description: string;
    price: number;
    category: string;
    available: boolean;
    condition: string;
    quantity: number;
    imageType: string; // New field
    imageData: string; // New field
};

const MyProductsPage: React.FC = () => {
    const [notSoldProducts, setNotSoldProducts] = useState<Product[]>([]);
    const [soldProducts, setSoldProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const raw = localStorage.getItem('user');
        const user = raw ? JSON.parse(raw) as { accId?: number } : {};
        const userIdStr = user.accId != null ? String(user.accId) : "";

        if (!userIdStr) {
            setError('User ID missing. Please log in.');
            setLoading(false);
            return; // Exit early if userId is missing
        }

        const fetchProducts = async () => {
            try {
                // Fetch Not Sold Products
                const notSoldResponse = await fetch(`http://localhost:8080/account/${userIdStr}/items/notsold`);
                if (!notSoldResponse.ok) throw new Error('Failed to fetch not sold products');
                const notSoldData: Product[] = await notSoldResponse.json();
                setNotSoldProducts(notSoldData);

                // Fetch Sold Products
                const soldResponse = await fetch(`http://localhost:8080/account/${userIdStr}/items/sold`);
                if (!soldResponse.ok) throw new Error('Failed to fetch sold products');
                const soldData: Product[] = await soldResponse.json();
                setSoldProducts(soldData);
            } catch (error) {
                console.error('Error fetching products:', error);
                setError('Failed to load products');
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, []);

    const handleRemove = (id: number, isSold: boolean) => {
        if (isSold) {
            setSoldProducts(prev => prev.filter(p => p.itemId !== id));
        } else {
            setNotSoldProducts(prev => prev.filter(p => p.itemId !== id));
        }
    };

    const handleMarkSold = (id: number) => {
        alert(`Product ${id} marked as sold.`);
    };

    const handleEdit = (id: number) => {
        alert(`Redirect to edit product ${id}`);
    };

    if (loading) {
        return <div className="text-center">Loading products...</div>;
    }

    if (error) {
        return <div className="text-center text-red-500">{error}</div>;
    }

    return (
        <div className="bg-white min-h-screen py-16">
            <div className="container mx-auto px-4">
                <h1 className="text-3xl font-bold text-gray-900 mb-6 text-center">My Dashboard</h1>

                {/* Not Sold Products Section */}
                <div className="mb-12">
                    <h2 className="text-2xl font-semibold text-gray-800 mb-4">Not Sold Items</h2>
                    {notSoldProducts.length > 0 ? (
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                            {notSoldProducts.map(product => (
                                <DashboardProductCard
                                    key={product.itemId}
                                    product={product}
                                    onRemove={() => handleRemove(product.itemId, false)}
                                    onMarkSold={() => handleMarkSold(product.itemId)}
                                    onEdit={() => handleEdit(product.itemId)}
                                />
                            ))}
                        </div>
                    ) : (
                        <p className="text-center text-gray-500 text-lg">No products available.</p>
                    )}
                </div>

                {/* Sold Products Section */}
                <div>
                    <h2 className="text-2xl font-semibold text-gray-800 mb-4">Sold Items</h2>
                    {soldProducts.length > 0 ? (
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                            {soldProducts.map(product => (
                                <DashboardProductCard
                                    key={product.itemId}
                                    product={product}
                                    onRemove={() => handleRemove(product.itemId, true)}
                                    onMarkSold={() => handleMarkSold(product.itemId)}
                                    onEdit={() => handleEdit(product.itemId)}
                                />
                            ))}
                        </div>
                    ) : (
                        <p className="text-center text-gray-500 text-lg">No sold products available.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default MyProductsPage;
