import React, { useState, useEffect, useRef } from 'react';
import { ExternalLink, Clock, ChevronDown, FolderOpen, BarChart3 } from 'lucide-react';
import { templateService } from '../services/templateService';
import { downloadService } from '../services/downloadService';

export default function TemplateCard({ template }) {
  const [showViewOptions, setShowViewOptions] = useState(false);
  const [loading, setLoading] = useState(false);
  const dropdownRef = useRef(null);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowViewOptions(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleViewFiles = async (fileInfo = null) => {
    try {
      setLoading(true);
      
      // Track view in backend
      await templateService.downloadTemplate(template.id);
      
      if (fileInfo) {
        // Use the navigation service for specific actions
        await downloadService.downloadSpecificFiles(template, fileInfo.action);
      } else {
        // Default: open main template directory
        await downloadService.downloadTemplateFiles(template);
      }
      
      setShowViewOptions(false);
    } catch (error) {
      console.error('Error opening template:', error);
      // Error handling is done in downloadService
    } finally {
      setLoading(false);
    }
  };

  const handleQuickView = async () => {
    await handleViewFiles();
  };

  const toggleViewOptions = (e) => {
    e.stopPropagation();
    setShowViewOptions(!showViewOptions);
  };

  const getComplexityColor = (complexity) => {
    switch (complexity?.toLowerCase()) {
      case 'beginner': return 'bg-green-100 text-green-800';
      case 'intermediate': return 'bg-yellow-100 text-yellow-800';
      case 'advanced': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getTypeColor = (type) => {
    return type === 'CLOUDFORMATION' 
      ? 'bg-orange-500 text-white' 
      : 'bg-purple-600 text-white';
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString();
  };

  return (
    <div className="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow duration-200 border border-gray-200">
      <div className="p-6">
        {/* Header */}
        <div className="flex justify-between items-start mb-4">
          <div className="flex-1">
            <h3 className="text-lg font-semibold text-gray-900 mb-2">{template.name}</h3>
            <div className="flex items-center space-x-2 mb-2">
              <span className={`px-2 py-1 rounded-full text-xs font-medium ${getTypeColor(template.type)}`}>
                {template.type === 'CLOUDFORMATION' ? 'CloudFormation' : 'Terraform'}
              </span>
              <span className={`px-2 py-1 rounded-full text-xs font-medium ${getComplexityColor(template.complexity)}`}>
                {template.complexity?.toLowerCase()}
              </span>
            </div>
          </div>
        </div>

        {/* Description */}
        <p className="text-gray-600 text-sm mb-4 line-clamp-3">{template.description}</p>

        {/* Well-Architected Pillars */}
        {template.wellArchitectedPillars && template.wellArchitectedPillars.length > 0 && (
          <div className="mb-4">
            <div className="flex flex-wrap gap-1">
              {template.wellArchitectedPillars.map((pillar) => (
                <span
                  key={pillar}
                  className="px-2 py-1 bg-blue-50 text-blue-700 text-xs rounded-md"
                >
                  {pillar}
                </span>
              ))}
            </div>
          </div>
        )}

        {/* Tags */}
        {template.tags && template.tags.length > 0 && (
          <div className="mb-4">
            <div className="flex flex-wrap gap-1">
              {template.tags.slice(0, 3).map((tag) => (
                <span
                  key={tag}
                  className="px-2 py-1 bg-gray-100 text-gray-700 text-xs rounded-md"
                >
                  {tag}
                </span>
              ))}
              {template.tags.length > 3 && (
                <span className="px-2 py-1 bg-gray-100 text-gray-700 text-xs rounded-md">
                  +{template.tags.length - 3} more
                </span>
              )}
            </div>
          </div>
        )}

        {/* Footer */}
        <div className="flex items-center justify-between pt-4 border-t border-gray-100">
          <div className="flex items-center text-xs text-gray-500">
            <Clock className="h-3 w-3 mr-1" />
            {formatDate(template.lastUpdated)}
          </div>
          <div className="flex space-x-2">
            {template.githubUrl && (
              <button
                onClick={() => window.open(template.githubUrl, '_blank')}
                className="p-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-md transition-colors"
                title="View on GitHub"
              >
                <ExternalLink className="h-4 w-4" />
              </button>
            )}

            {/* Generate Diagram Button - Coming Soon */}
            <button
              disabled
              className="p-2 text-gray-400 bg-gray-100 rounded-md cursor-not-allowed opacity-50"
              title="Architecture Diagrams - Coming Soon!"
            >
              <BarChart3 className="h-4 w-4" />
            </button>
            
            {/* View Files Button with Dropdown */}
            <div className="relative" ref={dropdownRef}>
              <div className="flex">
                <button
                  onClick={handleQuickView}
                  disabled={loading}
                  className="flex items-center px-3 py-2 bg-blue-500 text-white text-sm rounded-l-md hover:bg-blue-600 transition-colors disabled:opacity-50"
                  title="View template files on GitHub"
                >
                  <FolderOpen className="h-4 w-4 mr-1" />
                  {loading ? 'Opening...' : 'View Files'}
                </button>
                <button
                  onClick={toggleViewOptions}
                  className="px-2 py-2 bg-blue-500 text-white border-l border-blue-400 rounded-r-md hover:bg-blue-600 transition-colors"
                  title="More view options"
                >
                  <ChevronDown className="h-3 w-3" />
                </button>
              </div>
              
              {/* View Options Dropdown */}
              {showViewOptions && (
                <div className="absolute right-0 top-full mt-1 w-64 bg-white border border-gray-200 rounded-md shadow-lg z-10">
                  <div className="py-1">
                    <div className="px-3 py-2 text-xs font-medium text-gray-500 border-b">
                      Browse Files
                    </div>
                    {downloadService.getTemplateFiles(template).map((file, index) => (
                      <button
                        key={index}
                        onClick={() => handleViewFiles(file)}
                        disabled={loading}
                        className="w-full text-left px-3 py-2 text-sm text-gray-700 hover:bg-gray-50 disabled:opacity-50"
                      >
                        <div className="font-medium">{file.name}</div>
                        <div className="text-xs text-gray-500">{file.filename}</div>
                      </button>
                    ))}
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}