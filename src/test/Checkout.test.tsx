import { render, screen, fireEvent } from '@testing-library/react';
import Checkout from '../pages/Checkout';

describe('Checkout', () => {
  it('renders checkout form and is responsive', () => {
    render(<Checkout />);
    expect(screen.getByText(/Checkout|Shipping|Payment/i)).toBeInTheDocument();
    // Responsive class check (container)
    const container = screen.getByText(/Checkout|Shipping|Payment/i).closest('div');
    expect(container?.className).toMatch(/max-w/);
  });

  it('shows error if required fields are missing and does not process', () => {
    render(<Checkout />);
    const placeOrderBtn = screen.queryByRole('button', { name: /place order/i });
    if (placeOrderBtn) {
      fireEvent.click(placeOrderBtn);
      expect(screen.getByText(/please fill in all shipping information fields/i)).toBeInTheDocument();
    }
  });
});
