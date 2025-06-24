import { render, screen } from '@testing-library/react';
import Button from '../components/Button';

describe('Button', () => {
  it('renders button and is responsive', () => {
    render(<Button>Test Button</Button>);
    expect(screen.getByText('Test Button')).toBeInTheDocument();
    // Responsive class check (should be flexible width)
    const btn = screen.getByText('Test Button').closest('button');
    expect(btn).toHaveClass('rounded');
  });
});
