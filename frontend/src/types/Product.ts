export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  category: string;
  condition: 'new' | 'like new' | 'good' | 'fair' | 'poor';
  seller: {
    id: string;
    name: string;
    rating: number;
  };
  images: string[];
  location: string;
  createdAt: string;
  features?: string[];
}