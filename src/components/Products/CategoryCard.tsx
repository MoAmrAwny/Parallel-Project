import React from 'react';
import { Link } from 'react-router-dom';
import { Laptop, Sofa, Shirt, Book, Bike, Diamond } from 'lucide-react';

interface CategoryProps {
  category: {
    id: string;
    name: string;
    icon: string;
    count: number;
  };
}

const CategoryCard: React.FC<CategoryProps> = ({ category }) => {
  const getIcon = (iconName: string) => {
    switch (iconName) {
      case 'laptop':
        return <Laptop size={28} className="text-blue-600" />;
      case 'sofa':
        return <Sofa size={28} className="text-blue-600" />;
      case 'shirt':
        return <Shirt size={28} className="text-blue-600" />;
      case 'book':
        return <Book size={28} className="text-blue-600" />;
      case 'bike':
        return <Bike size={28} className="text-blue-600" />;
      case 'diamond':
        return <Diamond size={28} className="text-blue-600" />;
      default:
        return <Laptop size={28} className="text-blue-600" />;
    }
  };

  return (
    <Link 
      to={`/search?category=${category.id}`}
      className="bg-white flex flex-col items-center p-6 rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition-shadow"
    >
      <div className="mb-3">
        {getIcon(category.icon)}
      </div>
      <h3 className="font-medium text-gray-900">{category.name}</h3>
      <span className="text-sm text-gray-500">{category.count} items</span>
    </Link>
  );
};

export default CategoryCard;