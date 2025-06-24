import { render, screen } from '@testing-library/react';
import Footer from '../components/Footer';

describe('Footer', () => {
  it('renders footer links and is responsive', () => {
    render(<Footer />);
    expect(screen.getByText('Shop')).toBeInTheDocument();
    expect(screen.getByText('Support')).toBeInTheDocument();
    expect(screen.getByText('Company')).toBeInTheDocument();
    expect(screen.getByText('Legal')).toBeInTheDocument();
    // Responsive class check
    const footer = screen.getByRole('contentinfo');
    expect(footer.className).toMatch(/px-4/);
  });
});
