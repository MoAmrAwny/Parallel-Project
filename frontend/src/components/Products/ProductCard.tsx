import React from 'react';

interface Product {
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
  imageData: string;
}

interface ProductCardProps {
  product: Product;
}

const ProductCard: React.FC<ProductCardProps> = ({ product }) => {
  const imageSrc = product.imageData
    ? `data:${product.imageType};base64,${product.imageData}`
    : '/images/placeholder.png';

  const isOutOfStock = product.quantity <= 0;

  return (
    <div className="max-w-sm rounded overflow-hidden shadow-lg bg-white">
      <img
        className="w-full h-48 object-cover"
        src={imageSrc}
        alt={product.name}
        onError={(e) => {
          (e.target as HTMLImageElement).src = '/images/placeholder.png';
        }}
      />
      <div className="px-6 py-4">
        <div className="font-bold text-xl mb-2">{product.name}</div>
        <p className="text-gray-700 text-base">{product.description}</p>
      </div>
      <div className="px-6 py-4">
        <span className="inline-block bg-blue-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2">
          ${product.price.toFixed(2)}
        </span>
        <span
          className={`inline-block ${isOutOfStock ? 'bg-red-200' : 'bg-green-200'
            } rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2`}
        >
          {isOutOfStock ? 'Out of Stock' : 'Available'}
        </span>
        <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700">
          {product.category}
        </span>
      </div>
    </div>
  );
};

export default ProductCard;
