import React from 'react';

const categories = [
  'Networking',
  'Web Applications', 
  'Serverless',
  'Containers',
  'Static Websites',
  'Databases'
];

const types = [
  { value: 'cloudformation', label: 'CloudFormation' },
  { value: 'terraform', label: 'Terraform' }
];

export default function FilterSidebar({ 
  selectedCategory, 
  selectedType, 
  onCategoryChange, 
  onTypeChange 
}) {
  return (
    <div className="w-64 bg-white rounded-lg shadow-sm border border-gray-200 p-6">
      <h3 className="text-lg font-semibold text-gray-900 mb-4">Filters</h3>
      
      {/* Category Filter */}
      <div className="mb-6">
        <h4 className="text-sm font-medium text-gray-700 mb-3">Category</h4>
        <div className="space-y-2">
          <label className="flex items-center">
            <input
              type="radio"
              name="category"
              value="all"
              checked={selectedCategory === 'all'}
              onChange={(e) => onCategoryChange(e.target.value)}
              className="text-orange-500 focus:ring-orange-500"
            />
            <span className="ml-2 text-sm text-gray-600">All Categories</span>
          </label>
          {categories.map((category) => (
            <label key={category} className="flex items-center">
              <input
                type="radio"
                name="category"
                value={category}
                checked={selectedCategory === category}
                onChange={(e) => onCategoryChange(e.target.value)}
                className="text-orange-500 focus:ring-orange-500"
              />
              <span className="ml-2 text-sm text-gray-600">{category}</span>
            </label>
          ))}
        </div>
      </div>

      {/* Type Filter */}
      <div className="mb-6">
        <h4 className="text-sm font-medium text-gray-700 mb-3">Template Type</h4>
        <div className="space-y-2">
          <label className="flex items-center">
            <input
              type="radio"
              name="type"
              value="all"
              checked={selectedType === 'all'}
              onChange={(e) => onTypeChange(e.target.value)}
              className="text-orange-500 focus:ring-orange-500"
            />
            <span className="ml-2 text-sm text-gray-600">All Types</span>
          </label>
          {types.map((type) => (
            <label key={type.value} className="flex items-center">
              <input
                type="radio"
                name="type"
                value={type.value}
                checked={selectedType === type.value}
                onChange={(e) => onTypeChange(e.target.value)}
                className="text-orange-500 focus:ring-orange-500"
              />
              <span className="ml-2 text-sm text-gray-600">{type.label}</span>
            </label>
          ))}
        </div>
      </div>
    </div>
  );
}