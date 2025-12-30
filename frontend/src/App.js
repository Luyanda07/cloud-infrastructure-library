import React, { useState, useEffect } from 'react';
import { Search, Filter, Cloud, Shield, Cpu, HardDrive } from 'lucide-react';
import TemplateCard from './components/TemplateCard';
import FilterSidebar from './components/FilterSidebar';
import { templateService } from './services/templateService';

function App() {
  const [templates, setTemplates] = useState([]);
  const [filteredTemplates, setFilteredTemplates] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [selectedType, setSelectedType] = useState('all');
  const [showFilters, setShowFilters] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadTemplates();
  }, []);

  useEffect(() => {
    filterTemplates();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [templates, searchTerm, selectedCategory, selectedType]);

  const loadTemplates = async () => {
    try {
      setLoading(true);
      const data = await templateService.getAllTemplates();
      setTemplates(data);
    } catch (error) {
      console.error('Error loading templates:', error);
    } finally {
      setLoading(false);
    }
  };

  const filterTemplates = async () => {
    try {
      const params = {};
      if (searchTerm) params.search = searchTerm;
      if (selectedCategory !== 'all') params.category = selectedCategory;
      if (selectedType !== 'all') params.type = selectedType.toUpperCase();

      const data = await templateService.searchTemplates(params);
      setFilteredTemplates(data);
    } catch (error) {
      console.error('Error filtering templates:', error);
      setFilteredTemplates(templates);
    }
  };

  const getTemplateCountText = () => {
    const hasActiveFilters = searchTerm || selectedCategory !== 'all' || selectedType !== 'all';
    
    if (hasActiveFilters) {
      return `${filteredTemplates.length} Templates Found`;
    } else {
      return `${filteredTemplates.length} Available Templates`;
    }
  };

  const getEmptyStateTitle = () => {
    const hasActiveFilters = searchTerm || selectedCategory !== 'all' || selectedType !== 'all';
    
    if (hasActiveFilters) {
      return 'No templates found';
    } else {
      return 'No templates available';
    }
  };

  const getEmptyStateMessage = () => {
    const hasActiveFilters = searchTerm || selectedCategory !== 'all' || selectedType !== 'all';
    
    if (hasActiveFilters) {
      return 'Try adjusting your search or filter criteria';
    } else {
      return 'Templates are being loaded or none are currently available';
    }
  };

  const getActiveFiltersCount = () => {
    let count = 0;
    if (searchTerm) count++;
    if (selectedCategory !== 'all') count++;
    if (selectedType !== 'all') count++;
    return count;
  };

  const clearAllFilters = () => {
    setSearchTerm('');
    setSelectedCategory('all');
    setSelectedType('all');
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <Cloud className="h-12 w-12 text-orange-500 mx-auto mb-4 animate-pulse" />
          <p className="text-gray-600">Loading templates...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div className="flex items-center">
              <Cloud className="h-8 w-8 text-orange-500 mr-3" />
              <h1 className="text-2xl font-bold text-gray-900">Cloud Infrastructure Library</h1>
            </div>
            <div className="text-sm text-gray-600">
              AWS Well-Architected Templates
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="bg-gray-900 text-white py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-4xl font-bold mb-4">
            Production-Ready AWS Infrastructure Templates
          </h2>
          <p className="text-xl mb-8 text-gray-300">
            Curated CloudFormation and Terraform templates following AWS Well-Architected Framework principles
          </p>
          <div className="flex justify-center space-x-4">
            <div className="flex items-center text-orange-500">
              <Shield className="h-5 w-5 mr-2" />
              <span>Security</span>
            </div>
            <div className="flex items-center text-orange-500">
              <Cpu className="h-5 w-5 mr-2" />
              <span>Performance</span>
            </div>
            <div className="flex items-center text-orange-500">
              <HardDrive className="h-5 w-5 mr-2" />
              <span>Cost Optimized</span>
            </div>
          </div>
        </div>
      </section>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Search and Filter Bar */}
        <div className="mb-8">
          <div className="flex flex-col sm:flex-row gap-4 items-center justify-between">
            <div className="relative flex-1 max-w-md">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-5 w-5" />
              <input
                type="text"
                placeholder="Search templates..."
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
            <div className="flex items-center gap-2">
              {getActiveFiltersCount() > 0 && (
                <span className="px-2 py-1 bg-orange-100 text-orange-800 text-xs rounded-full">
                  {getActiveFiltersCount()} filter{getActiveFiltersCount() > 1 ? 's' : ''} active
                </span>
              )}
              <button
                onClick={() => setShowFilters(!showFilters)}
                className={`flex items-center px-4 py-2 border rounded-lg transition-colors ${
                  showFilters 
                    ? 'bg-orange-50 border-orange-200 text-orange-700' 
                    : 'bg-white border-gray-300 hover:bg-gray-50'
                }`}
              >
                <Filter className="h-4 w-4 mr-2" />
                Filters
              </button>
            </div>
          </div>
        </div>

        <div className="flex gap-8">
          {/* Sidebar */}
          {showFilters && (
            <FilterSidebar
              selectedCategory={selectedCategory}
              selectedType={selectedType}
              onCategoryChange={setSelectedCategory}
              onTypeChange={setSelectedType}
            />
          )}

          {/* Main Content */}
          <div className="flex-1">
            <div className="mb-6 flex items-center justify-between">
              <h3 className="text-lg font-semibold text-gray-900">
                {getTemplateCountText()}
              </h3>
              {getActiveFiltersCount() > 0 && (
                <button
                  onClick={clearAllFilters}
                  className="text-sm text-orange-600 hover:text-orange-800 underline"
                >
                  Clear all filters
                </button>
              )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredTemplates.map((template) => (
                <TemplateCard key={template.id} template={template} />
              ))}
            </div>

            {filteredTemplates.length === 0 && (
              <div className="text-center py-12">
                <Cloud className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">
                  {getEmptyStateTitle()}
                </h3>
                <p className="text-gray-600">{getEmptyStateMessage()}</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;