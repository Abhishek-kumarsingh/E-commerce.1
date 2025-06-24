import { render, screen } from '@testing-library/react';
import FilterSidebar from '../components/FilterSidebar';
import { FilterOptions } from '../types';

describe('FilterSidebar', () => {
  const filters: FilterOptions = {};
  it('renders filter sidebar and is responsive', () => {
    render(
      <FilterSidebar filters={filters} onFiltersChange={() => {}} isOpen={true} onClose={() => {}} />
    );
    expect(screen.getByText('Filters')).toBeInTheDocument();
    expect(screen.getByText('Categories')).toBeInTheDocument();
    // Responsive class check
    const sidebar = screen.getByText('Filters').closest('div');
    expect(sidebar?.className).toMatch(/w-80|lg:w-64/);
  });
});
