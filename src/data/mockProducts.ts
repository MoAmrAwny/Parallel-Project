import { Product } from '../types/Product';

export const mockProducts: Product[] = [
  {
    id: '1',
    name: 'Apple MacBook Pro 2023',
    description: 'Apple M2 chip, 16GB RAM, 512GB SSD, Space Gray. Perfect for students and professionals.',
    price: 1499.99,
    category: 'electronics',
    condition: 'new',
    seller: {
      id: '101',
      name: 'TechStore',
      rating: 4.8
    },
    images: [
      'https://images.pexels.com/photos/249538/pexels-photo-249538.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
      'https://images.pexels.com/photos/3766120/pexels-photo-3766120.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
      'https://images.pexels.com/photos/3766117/pexels-photo-3766117.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'
    ],
    location: 'New York, NY',
    createdAt: '2023-09-15T10:30:00Z',
    features: ['M2 Chip', '16GB RAM', '512GB SSD', 'macOS Ventura']
  },{
    id: '1',
    name: "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
    price: 109.95,
    description: "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
    category: "men's clothing",
    condition: 'new',
    images:[ "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"],
    "seller": {
      id:"1",
      name:"asfdsdf",
      rating: 3.9
      
  },
    location: 'New York, NY',
    createdAt: '2023-09-15T10:30:00Z',
    features: ['M2 Chip', '16GB RAM', '512GB SSD', 'macOS Ventura']
},
  {
    id: '2',
    name: 'Sony WH-1000XM4 Headphones',
    description: 'Industry-leading noise cancellation wireless headphones with 30-hour battery life.',
    price: 249.99,
    category: 'electronics',
    condition: 'like new',
    seller: {
      id: '102',
      name: 'AudioGear',
      rating: 4.6
    },
    images: [
      'https://images.pexels.com/photos/3394666/pexels-photo-3394666.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
      'https://images.pexels.com/photos/3394665/pexels-photo-3394665.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'
    ],
    location: 'Los Angeles, CA',
    createdAt: '2023-09-12T14:45:00Z',
    features: ['Noise Cancellation', 'Bluetooth 5.0', '30-hour Battery', 'Touch Controls']
  },
  {
    id: '3',
    name: 'Ikea MALM Desk',
    description: 'White desk with pull-out panel. Assembled but in excellent condition.',
    price: 89.99,
    category: 'furniture',
    condition: 'good',
    seller: {
      id: '103',
      name: 'HomeStyles',
      rating: 4.4
    },
    images: [
      'https://images.pexels.com/photos/245219/pexels-photo-245219.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
      'https://images.pexels.com/photos/6489113/pexels-photo-6489113.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'
    ],
    location: 'Chicago, IL',
    createdAt: '2023-09-10T09:15:00Z'
  },
  {
    id: '4',
    name: 'The Psychology of Money',
    description: 'Timeless lessons on wealth, greed, and happiness by Morgan Housel.',
    price: 12.99,
    category: 'books',
    condition: 'like new',
    seller: {
      id: '104',
      name: 'BookWorm',
      rating: 4.9
    },
    images: [
      'https://images.pexels.com/photos/590493/pexels-photo-590493.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'
    ],
    location: 'Boston, MA',
    createdAt: '2023-09-08T16:20:00Z'
  },
  {
    id: '5',
    name: 'Levi\'s 501 Original Fit Jeans',
    description: 'Classic straight leg jeans in dark wash, barely worn.',
    price: 39.99,
    category: 'clothing',
    condition: 'like new',
    seller: {
      id: '105',
      name: 'FashionFind',
      rating: 4.7
    },
    images: [
      'https://images.pexels.com/photos/52518/jeans-pants-blue-shop-52518.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
      'https://images.pexels.com/photos/1082529/pexels-photo-1082529.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'
    ],
    location: 'Austin, TX',
    createdAt: '2023-09-07T11:10:00Z'
  },
  {
    id: '6',
    name: 'Samsung 55" QLED 4K Smart TV',
    description: 'Quantum HDR, built-in Alexa, bought last year. Moving and need to sell.',
    price: 699.99,
    category: 'electronics',
    condition: 'good',
    seller: {
      id: '106',
      name: 'ElectroDeals',
      rating: 4.5
    },
    images: [
      'https://images.pexels.com/photos/5552789/pexels-photo-5552789.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
      'https://images.pexels.com/photos/5552788/pexels-photo-5552788.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'
    ],
    location: 'Seattle, WA',
    createdAt: '2023-09-05T15:30:00Z',
    features: ['4K UHD', 'Smart TV', 'QLED', 'Voice Control']
  },
  {
    id: '7',
    name: 'Specialized Allez Road Bike',
    description: 'E5 Premium Aluminum, Shimano Claris 8-speed. Minor scratches but mechanically perfect.',
    price: 599.99,
    category: 'sports',
    condition: 'good',
    seller: {
      id: '107',
      name: 'CycleMaster',
      rating: 4.8
    },
    images: [
      'https://images.pexels.com/photos/276517/pexels-photo-276517.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
      'https://images.pexels.com/photos/5462425/pexels-photo-5462425.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'
    ],
    location: 'Portland, OR',
    createdAt: '2023-09-03T12:45:00Z'
  },
  {
    id: '8',
    name: 'IKEA BILLY Bookcase',
    description: 'White bookcase with glass doors. Some assembly required, all parts included.',
    price: 129.99,
    category: 'furniture',
    condition: 'new',
    seller: {
      id: '108',
      name: 'FurnitureDepot',
      rating: 4.6
    },
    images: [
      'https://images.pexels.com/photos/696218/pexels-photo-696218.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
      'https://images.pexels.com/photos/1090638/pexels-photo-1090638.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1'
    ],
    location: 'Denver, CO',
    createdAt: '2023-09-01T09:00:00Z'
  }
];

export const getProductById = (id: string): Product | undefined => {
  return mockProducts.find(product => product.id === id);
};

export const getRelatedProducts = (categoryId: string, currentProductId: string, limit = 4): Product[] => {
  return mockProducts
    .filter(product => product.category === categoryId && product.id !== currentProductId)
    .slice(0, limit);
};

export const searchProducts = (query: string, filters: Record<string, any> = {}): Product[] => {
  let filtered = [...mockProducts];
  
  // Text search
  if (query) {
    const lowercaseQuery = query.toLowerCase();
    filtered = filtered.filter(product => 
      product.name.toLowerCase().includes(lowercaseQuery) || 
      product.description.toLowerCase().includes(lowercaseQuery)
    );
  }
  
  // Category filter
  if (filters.category) {
    filtered = filtered.filter(product => product.category === filters.category);
  }
  
  // Condition filter
  if (filters.condition) {
    filtered = filtered.filter(product => product.condition === filters.condition);
  }
  
  // Price range filter
  if (filters.minPrice !== undefined) {
    filtered = filtered.filter(product => product.price >= filters.minPrice);
  }
  
  if (filters.maxPrice !== undefined) {
    filtered = filtered.filter(product => product.price <= filters.maxPrice);
  }
  
  return filtered;
};