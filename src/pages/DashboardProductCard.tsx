import React from 'react';
import { Product } from '../../types/Product';
import { Pencil, Trash2, CheckCircle } from 'lucide-react';

interface Props {
    product: Product;
    onRemove: (id: string) => void;
    onMarkSold: (id: string) => void;
    onEdit: (id: string) => void;
}

const DashboardProductCard: React.FC<Props> = ({ product, onRemove, onMarkSold, onEdit }) => {
    return (
        <div className="border rounded-xl shadow-sm overflow-hidden bg-white flex flex-col">
            <img
                src={product.images[0]}
                alt={product.name}
                className="h-48 object-cover w-full"
            />
            <div className="p-4 flex flex-col flex-grow">
                <h2 className="text-lg font-semibold text-gray-900">{product.name}</h2>
                <p className="text-sm text-gray-600 mt-1 mb-2 line-clamp-2">{product.description}</p>
                <p className="text-blue-600 font-bold text-lg mb-4">${product.price.toFixed(2)}</p>

                <div className="mt-auto flex flex-wrap gap-2">
                    <button
                        onClick={() => onEdit(product.id)}
                        className="flex items-center gap-1 px-3 py-1 text-sm bg-blue-100 text-blue-700 rounded hover:bg-blue-200"
                    >
                        <Pencil size={16} /> Edit
                    </button>
                    <button
                        onClick={() => onMarkSold(product.id)}
                        className="flex items-center gap-1 px-3 py-1 text-sm bg-green-100 text-green-700 rounded hover:bg-green-200"
                    >
                        <CheckCircle size={16} /> Mark as Sold
                    </button>
                    <button
                        onClick={() => onRemove(product.id)}
                        className="flex items-center gap-1 px-3 py-1 text-sm bg-red-100 text-red-700 rounded hover:bg-red-200"
                    >
                        <Trash2 size={16} /> Remove
                    </button>
                </div>
            </div>
        </div>
    );
};

export default DashboardProductCard;
