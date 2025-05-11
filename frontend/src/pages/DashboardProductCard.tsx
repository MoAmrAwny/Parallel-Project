import React from 'react';

type Product = {
    itemId: number;
    name: string;
    description: string;
    price: number;
    category: string;
    available: boolean;
    condition: string;
    quantity: number;
    imageType: string; // New field for image type
    imageData: string; // New field for image data (Base64)
};

interface DashboardProductCardProps {
    product: Product;
    onRemove: () => void;
    onMarkSold: () => void;
    onEdit: () => void;
}

const DashboardProductCard: React.FC<DashboardProductCardProps> = ({ product, onRemove, onMarkSold, onEdit }) => {
    const imageUrl = `data:${product.imageType};base64,${product.imageData}`; // Convert the Base64 string into a valid image URL

    return (
        <div className="bg-white shadow-lg rounded-lg p-6">
            {/* Display the image */}
            <img src={imageUrl} alt={product.name} className="w-full h-48 object-cover mb-4 rounded-md" />

            <h2 className="text-xl font-semibold text-gray-900">{product.name}</h2>
            <p className="text-gray-700">{product.description}</p>
            <p className="text-gray-600">Category: {product.category}</p>
            <p className="text-gray-600">Condition: {product.condition}</p>
            <p className="text-gray-600">Quantity: {product.quantity}</p>
            <p className="text-lg font-bold text-gray-900">${product.price.toFixed(2)}</p>

            <div className="flex justify-between items-center mt-4">
                <button
                    className="bg-blue-500 text-white px-4 py-2 rounded-md"
                    onClick={onEdit}
                >
                    Edit
                </button>
                <button
                    className="bg-green-500 text-white px-4 py-2 rounded-md"
                    onClick={onMarkSold}
                >
                    Mark as Sold
                </button>
                <button
                    className="bg-red-500 text-white px-4 py-2 rounded-md"
                    onClick={onRemove}
                >
                    Remove
                </button>
            </div>
        </div>
    );
};

export default DashboardProductCard;
