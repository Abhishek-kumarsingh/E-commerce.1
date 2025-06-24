import { render, screen } from '@testing-library/react';
import Checkout from '../pages/Checkout';

describe('Checkout', () => {
  it('renders checkout form and is responsive', () => {
    render(<Checkout />);
    expect(screen.getByText(/Checkout|Shipping|Payment/i)).toBeInTheDocument();
    // Responsive class check (container)
    const container = screen.getByText(/Checkout|Shipping|Payment/i).closest('div');
    expect(container?.className).toMatch(/max-w/);
  });
});
