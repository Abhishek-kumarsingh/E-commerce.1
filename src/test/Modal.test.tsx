import { render, screen } from '@testing-library/react';
import Modal from '../components/Modal';

describe('Modal', () => {
  it('renders children and is responsive', () => {
    render(
      <Modal isOpen={true} onClose={() => {}} title="Test Modal">
        <div>Modal Content</div>
      </Modal>
    );
    expect(screen.getByText('Test Modal')).toBeInTheDocument();
    expect(screen.getByText('Modal Content')).toBeInTheDocument();
    // Responsive class check
    const modal = screen.getByText('Modal Content').closest('div');
    expect(modal?.className).toMatch(/max-h-\[90vh\]/);
  });
});
