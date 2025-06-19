import React from 'react';
import { X, Filter } from 'lucide-react';
import { FilterOptions } from '../types';
import { categories } from '../data/mockData';

interface FilterSidebarProps {
  filters: FilterOptions;
  onFiltersChange: (filters: FilterOptions) => void;
  isOpen: boolean;
  onClose: () => void;
}

const FilterSidebar: React.FC<FilterSidebarProps> = ({
  filters,
  onFiltersChange,
  isOpen,
  onClose,
}) => {
  const brands = ['AudioTech', 'FitTech', 'EcoWear', 'LensCraft', 'HomeFurn', 'Literary Press', 'ZenFit', 'BeautyLux'];

  const handlePriceRangeChange = (min: number, max: number) => {
    onFiltersChange({ ...filters, priceRange: [min, max] });
  };

  const handleCategoryChange = (category: string) => {
    onFiltersChange({ ...filters, category: category === filters.category ? undefined : category });
  };

  const handleBrandChange = (brand: string) => {
    onFiltersChange({ ...filters, brand: brand === filters.brand ? undefined : brand });
  };

  const handleRatingChange = (rating: number) => {
    onFiltersChange({ ...filters, rating: rating === filters.rating ? undefined : rating });
  };

  const handleInStockChange = (inStock: boolean) => {
    onFiltersChange({ ...filters, inStock: inStock === filters.inStock ? undefined : inStock });
  };

  const clearFilters = () => {
    onFiltersChange({});
  };

  return (
    <>
      {/* Mobile backdrop */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-40 lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar */}
      <div
        className={`fixed lg:relative top-0 left-0 h-full lg:h-auto w-80 lg:w-64 bg-white dark:bg-gray-900 shadow-lg lg:shadow-none z-50 lg:z-auto transform ${
          isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
        } transition-transform duration-300 ease-in-out overflow-y-auto`}
      >
        <div className="p-4">
          {/* Header */}
          <div className="flex items-center justify-between mb-6">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white flex items-center">
              <Filter className="h-5 w-5 mr-2" />
              Filters
            </h3>
            <div className="flex items-center space-x-2">
              <button
                onClick={clearFilters}
                className="text-sm text-primary-600 hover:text-primary-700 dark:text-primary-400"
              >
                Clear All
              </button>
              <button
                onClick={onClose}
                className="lg:hidden p-1 hover:bg-gray-100 dark:hover:bg-gray-800 rounded"
              >
                <X className="h-5 w-5" />
              </button>
            </div>
          </div>

          {/* Categories */}
          <div className="mb-6">
            <h4 className="font-medium text-gray-900 dark:text-white mb-3">Categories</h4>
            <div className="space-y-2">
              {categories.map((category) => (
                <label key={category.id} className="flex items-center">
                  <input
                    type="checkbox"
                    checked={filters.category === category.slug}
                    onChange={() => handleCategoryChange(category.slug)}
                    className="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                  />
                  <span className="ml-2 text-sm text-gray-700 dark:text-gray-300">
                    {category.name} ({category.productCount})
                  </span>
                </label>
              ))}
            </div>
          </div>

          {/* Price Range */}
          <div className="mb-6">
            <h4 className="font-medium text-gray-900 dark:text-white mb-3">Price Range</h4>
            <div className="space-y-2">
              {[
                { label: 'Under $50', min: 0, max: 50 },
                { label: '$50 - $100', min: 50, max: 100 },
                { label: '$100 - $200', min: 100, max: 200 },
                { label: '$200 - $500', min: 200, max: 500 },
                { label: 'Over $500', min: 500, max: 10000 },
              ].map((range) => (
                <label key={range.label} className="flex items-center">
                  <input
                    type="radio"
                    name="priceRange"
                    checked={
                      filters.priceRange?.[0] === range.min &&
                      filters.priceRange?.[1] === range.max
                    }
                    onChange={() => handlePriceRangeChange(range.min, range.max)}
                    className="text-primary-600 focus:ring-primary-500"
                  />
                  <span className="ml-2 text-sm text-gray-700 dark:text-gray-300">
                    {range.label}
                  </span>
                </label>
              ))}
            </div>
          </div>

          {/* Brands */}
          <div className="mb-6">
            <h4 className="font-medium text-gray-900 dark:text-white mb-3">Brands</h4>
            <div className="space-y-2">
              {brands.map((brand) => (
                <label key={brand} className="flex items-center">
                  <input
                    type="checkbox"
                    checked={filters.brand === brand}
                    onChange={() => handleBrandChange(brand)}
                    className="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                  />
                  <span className="ml-2 text-sm text-gray-700 dark:text-gray-300">
                    {brand}
                  </span>
                </label>
              ))}
            </div>
          </div>

          {/* Rating */}
          <div className="mb-6">
            <h4 className="font-medium text-gray-900 dark:text-white mb-3">Rating</h4>
            <div className="space-y-2">
              {[4, 3, 2, 1].map((rating) => (
                <label key={rating} className="flex items-center">
                  <input
                    type="radio"
                    name="rating"
                    checked={filters.rating === rating}
                    onChange={() => handleRatingChange(rating)}
                    className="text-primary-600 focus:ring-primary-500"
                  />
                  <span className="ml-2 text-sm text-gray-700 dark:text-gray-300">
                    {rating}+ Stars
                  </span>
                </label>
              ))}
            </div>
          </div>

          {/* Availability */}
          <div className="mb-6">
            <h4 className="font-medium text-gray-900 dark:text-white mb-3">Availability</h4>
            <label className="flex items-center">
              <input
                type="checkbox"
                checked={filters.inStock === true}
                onChange={() => handleInStockChange(true)}
                className="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
              />
              <span className="ml-2 text-sm text-gray-700 dark:text-gray-300">
                In Stock Only
              </span>
            </label>
          </div>
        </div>
      </div>
    </>
  );
};

export default FilterSidebar;