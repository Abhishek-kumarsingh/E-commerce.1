import { render, screen } from '@testing-library/react';
import ProductCard from '../components/ProductCard';
import { Product } from '../types';

describe('ProductCard', () => {
  const product: Product = {
    id: '1',
    name: 'Test Product',
    brand: 'TestBrand',
    description: 'A great product',
    image: '/test.jpg',
    images: ['/test.jpg'],
    price: 99.99,
    originalPrice: 129.99,
    rating: 4.5,
    reviewCount: 10,
    inStock: true,
    featured: true,
    tags: ['tag1'],
    stockQuantity: 10,
    specifications: { Color: 'Red' },
  };

  it('renders product info and is responsive', () => {
    render(<ProductCard product={product} />);
    expect(screen.getByText('Test Product')).toBeInTheDocument();
    expect(screen.getByText('$99.99')).toBeInTheDocument();
    expect(screen.getByText('Add to Cart')).toBeInTheDocument();
    // Responsive class check
    const addToCartBtn = screen.getByText('Add to Cart').closest('button');
    expect(addToCartBtn).toHaveClass('md:hidden');
  });

  it('shows out of stock message when not in stock', () => {
    render(<ProductCard product={{ ...product, inStock: false }} />);
    expect(screen.getByText('Out of Stock')).toBeInTheDocument();
  });
});
