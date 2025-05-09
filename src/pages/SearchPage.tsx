import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Filter, Search, ChevronDown, X } from 'lucide-react';
import { searchProducts } from '../data/mockProducts';
import ProductCard from '../components/Products/ProductCard';
import { Product } from '../types/Product';

const SearchPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const queryParams = new URLSearchParams(location.search);
  
  const [searchQuery, setSearchQuery] = useState(queryParams.get('q') || '');
  const [category, setCategory] = useState(queryParams.get('category') || '');
  const [condition, setCondition] = useState(queryParams.get('condition') || '');
  const [minPrice, setMinPrice] = useState(queryParams.get('minPrice') || '');
  const [maxPrice, setMaxPrice] = useState(queryParams.get('maxPrice') || '');
  const [products, setProducts] = useState<Product[]>([]);
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  
  const categories = [
    { id: '', name: 'All Categories' },
    { id: 'electronics', name: 'Electronics' },
    { id: 'furniture', name: 'Furniture' },
    { id: 'clothing', name: 'Clothing' },
    { id: 'books', name: 'Books' },
    { id: 'sports', name: 'Sports' },
    { id: 'collectibles', name: 'Collectibles' }
  ];
  
  const conditions = [
    { id: '', name: 'All Conditions' },
    { id: 'new', name: 'New' },
    { id: 'like new', name: 'Like New' },
    { id: 'good', name: 'Good' },
    { id: 'fair', name: 'Fair' },
    { id: 'poor', name: 'Poor' }
  ];
  
  const performSearch = () => {
    const filters: Record<string, any> = {};
    
    if (category) filters.category = category;
    if (condition) filters.condition = condition;
    if (minPrice) filters.minPrice = parseFloat(minPrice);
    if (maxPrice) filters.maxPrice = parseFloat(maxPrice);
    
    const results = searchProducts(searchQuery, filters);
    setProducts(results);
    
    // Update URL with search parameters
    const params = new URLSearchParams();
    if (searchQuery) params.set('q', searchQuery);
    if (category) params.set('category', category);
    if (condition) params.set('condition', condition);
    if (minPrice) params.set('minPrice', minPrice);
    if (maxPrice) params.set('maxPrice', maxPrice);
    
    navigate({ search: params.toString() }, { replace: true });
  };
  
  useEffect(() => {
    performSearch();
  }, [location.search]);
  
  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    performSearch();
  };
  
  const clearFilters = () => {
    setCategory('');
    setCondition('');
    setMinPrice('');
    setMaxPrice('');
    
    // Keep only the search query
    const params = new URLSearchParams();
    if (searchQuery) params.set('q', searchQuery);
    navigate({ search: params.toString() }, { replace: true });
  };
  
  const toggleFilters = () => {
    setIsFilterOpen(!isFilterOpen);
  };
  
  return (
    <div className="bg-white min-h-screen">
      <div className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-4">
            {searchQuery ? `Search results for "${searchQuery}"` : 'Browse Products'}
          </h1>
          
          <form onSubmit={handleSearch} className="flex w-full max-w-2xl gap-2">
            <div className="relative flex-grow">
              <Search className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="Search products..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              Search
            </button>
            <button
              type="button"
              onClick={toggleFilters}
              className="lg:hidden px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors flex items-center justify-center"
              aria-expanded={isFilterOpen}
            >
              <Filter size={16} className="mr-2" />
              <span>Filters</span>
            </button>
          </form>
        </div>
        
        <div className="flex flex-col lg:flex-row gap-8">
          {/* Filters - Desktop */}
          <div className="hidden lg:block w-64 flex-shrink-0">
            <div className="bg-white p-6 border border-gray-200 rounded-lg shadow-sm">
              <div className="flex justify-between items-center mb-6">
                <h2 className="text-lg font-semibold text-gray-900">Filters</h2>
                {(category || condition || minPrice || maxPrice) && (
                  <button 
                    onClick={clearFilters}
                    className="text-sm text-blue-600 hover:text-blue-800"
                  >
                    Clear all
                  </button>
                )}
              </div>
              
              {/* Category Filter */}
              <div className="mb-6">
                <h3 className="font-medium text-gray-900 mb-3">Category</h3>
                <select
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  className="w-full border border-gray-300 rounded-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  {categories.map((cat) => (
                    <option key={cat.id} value={cat.id}>
                      {cat.name}
                    </option>
                  ))}
                </select>
              </div>
              
              {/* Condition Filter */}
              <div className="mb-6">
                <h3 className="font-medium text-gray-900 mb-3">Condition</h3>
                <select
                  value={condition}
                  onChange={(e) => setCondition(e.target.value)}
                  className="w-full border border-gray-300 rounded-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  {conditions.map((cond) => (
                    <option key={cond.id} value={cond.id}>
                      {cond.name}
                    </option>
                  ))}
                </select>
              </div>
              
              {/* Price Range Filter */}
              <div>
                <h3 className="font-medium text-gray-900 mb-3">Price Range</h3>
                <div className="flex items-center gap-2">
                  <input
                    type="number"
                    placeholder="Min"
                    value={minPrice}
                    onChange={(e) => setMinPrice(e.target.value)}
                    className="w-full border border-gray-300 rounded-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <span className="text-gray-500">to</span>
                  <input
                    type="number"
                    placeholder="Max"
                    value={maxPrice}
                    onChange={(e) => setMaxPrice(e.target.value)}
                    className="w-full border border-gray-300 rounded-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
              </div>
              
              <button
                onClick={performSearch}
                className="w-full mt-6 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
              >
                Apply Filters
              </button>
            </div>
          </div>
          
          {/* Filters - Mobile */}
          {isFilterOpen && (
            <div className="lg:hidden fixed inset-0 bg-gray-900 bg-opacity-50 z-50 flex items-end">
              <div className="bg-white rounded-t-xl w-full p-6 animate-pop">
                <div className="flex justify-between items-center mb-6">
                  <h2 className="text-lg font-semibold text-gray-900">Filters</h2>
                  <button 
                    onClick={toggleFilters}
                    className="p-2 text-gray-500"
                  >
                    <X size={20} />
                  </button>
                </div>
                
                {/* Category Filter */}
                <div className="mb-6">
                  <h3 className="font-medium text-gray-900 mb-3">Category</h3>
                  <select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    className="w-full border border-gray-300 rounded-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    {categories.map((cat) => (
                      <option key={cat.id} value={cat.id}>
                        {cat.name}
                      </option>
                    ))}
                  </select>
                </div>
                
                {/* Condition Filter */}
                <div className="mb-6">
                  <h3 className="font-medium text-gray-900 mb-3">Condition</h3>
                  <select
                    value={condition}
                    onChange={(e) => setCondition(e.target.value)}
                    className="w-full border border-gray-300 rounded-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    {conditions.map((cond) => (
                      <option key={cond.id} value={cond.id}>
                        {cond.name}
                      </option>
                    ))}
                  </select>
                </div>
                
                {/* Price Range Filter */}
                <div>
                  <h3 className="font-medium text-gray-900 mb-3">Price Range</h3>
                  <div className="flex items-center gap-2">
                    <input
                      type="number"
                      placeholder="Min"
                      value={minPrice}
                      onChange={(e) => setMinPrice(e.target.value)}
                      className="w-full border border-gray-300 rounded-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <span className="text-gray-500">to</span>
                    <input
                      type="number"
                      placeholder="Max"
                      value={maxPrice}
                      onChange={(e) => setMaxPrice(e.target.value)}
                      className="w-full border border-gray-300 rounded-md py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                </div>
                
                <div className="flex gap-3 mt-8">
                  {(category || condition || minPrice || maxPrice) && (
                    <button 
                      onClick={clearFilters}
                      className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors"
                    >
                      Clear all
                    </button>
                  )}
                  <button
                    onClick={() => {
                      performSearch();
                      toggleFilters();
                    }}
                    className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                  >
                    Apply Filters
                  </button>
                </div>
              </div>
            </div>
          )}
          
          {/* Search Results */}
          <div className="flex-grow">
            {/* Active Filters Display */}
            {(category || condition || minPrice || maxPrice) && (
              <div className="mb-6 flex flex-wrap gap-2">
                {category && (
                  <div className="flex items-center bg-blue-50 text-blue-700 px-3 py-1 rounded-full text-sm">
                    <span>Category: {categories.find(c => c.id === category)?.name}</span>
                    <button 
                      onClick={() => {
                        setCategory('');
                        performSearch();
                      }}
                      className="ml-2 text-blue-500 hover:text-blue-700"
                    >
                      <X size={14} />
                    </button>
                  </div>
                )}
                
                {condition && (
                  <div className="flex items-center bg-blue-50 text-blue-700 px-3 py-1 rounded-full text-sm">
                    <span>Condition: {conditions.find(c => c.id === condition)?.name}</span>
                    <button 
                      onClick={() => {
                        setCondition('');
                        performSearch();
                      }}
                      className="ml-2 text-blue-500 hover:text-blue-700"
                    >
                      <X size={14} />
                    </button>
                  </div>
                )}
                
                {(minPrice || maxPrice) && (
                  <div className="flex items-center bg-blue-50 text-blue-700 px-3 py-1 rounded-full text-sm">
                    <span>
                      Price: 
                      {minPrice ? ` $${minPrice}` : ' $0'} 
                      {' - '} 
                      {maxPrice ? `$${maxPrice}` : 'Any'}
                    </span>
                    <button 
                      onClick={() => {
                        setMinPrice('');
                        setMaxPrice('');
                        performSearch();
                      }}
                      className="ml-2 text-blue-500 hover:text-blue-700"
                    >
                      <X size={14} />
                    </button>
                  </div>
                )}
              </div>
            )}
            
            {/* Results Count */}
            <p className="text-gray-600 mb-6">
              {products.length} {products.length === 1 ? 'product' : 'products'} found
            </p>
            
            {/* Product Grid */}
            {products.length > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {products.map(product => (
                  <ProductCard key={product.id} product={product} />
                ))}
              </div>
            ) : (
              <div className="text-center py-12">
                <p className="text-gray-500 text-lg mb-4">No products found</p>
                <p className="text-gray-400">Try adjusting your search or filter criteria</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchPage;