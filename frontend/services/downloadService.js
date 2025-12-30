// Service for navigating to template files on GitHub
export const downloadService = {
  
  // Main function - opens specific template directory on GitHub
  async downloadTemplateFiles(template) {
    try {
      console.log('Opening template on GitHub:', template.name);
      
      // Simply open the GitHub URL for the template
      window.open(template.githubUrl, '_blank');
      
      return true;
    } catch (error) {
      console.error('Failed to open GitHub:', error);
      throw error;
    }
  },

  // Get template files for dropdown - navigation only
  getTemplateFiles(template) {
    const files = [];
    
    // Extract path from GitHub URL to determine available files
    const urlMatch = template.githubUrl.match(/\/tree\/[^/]+\/(.+)/);
    const templatePath = urlMatch ? urlMatch[1] : '';
    
    // Add main template directory
    files.push({
      name: `Browse ${template.name}`,
      url: template.githubUrl,
      filename: `View all ${template.name} files`,
      action: 'template'
    });
    
    // Add individual format navigation based on template type and path
    if (templatePath.includes('cloudformation') || (!templatePath.includes('terraform') && template.type === 'CLOUDFORMATION')) {
      const cfUrl = template.githubUrl.includes('/cloudformation') 
        ? template.githubUrl 
        : `${template.githubUrl}/cloudformation`;
      files.push({
        name: 'CloudFormation Files',
        url: cfUrl,
        filename: 'Browse YAML and JSON templates',
        action: 'cloudformation'
      });
    }
    
    if (templatePath.includes('terraform') || (!templatePath.includes('cloudformation') && template.type === 'TERRAFORM')) {
      const tfUrl = template.githubUrl.includes('/terraform') 
        ? template.githubUrl 
        : `${template.githubUrl}/terraform`;
      files.push({
        name: 'Terraform Files',
        url: tfUrl,
        filename: 'Browse .tf configuration files',
        action: 'terraform'
      });
    }
    
    // Add README
    files.push({
      name: 'Documentation',
      url: `${template.githubUrl.replace('/tree/', '/blob/')}/README.md`,
      filename: 'Setup guide and documentation',
      action: 'readme'
    });
    
    return files;
  },

  // Handle specific file navigation - no downloads
  async downloadSpecificFiles(template, action) {
    const urlMatch = template.githubUrl.match(/github\.com\/([^/]+)\/([^/]+)\/tree\/([^/]+)\/(.+)/);
    
    if (!urlMatch) {
      window.open(template.githubUrl, '_blank');
      return;
    }
    
    const [, owner, repo, branch, basePath] = urlMatch;
    
    switch (action) {
      case 'cloudformation':
        // Open CloudFormation directory on GitHub
        const cfUrl = template.githubUrl.includes('/cloudformation') 
          ? template.githubUrl 
          : `https://github.com/${owner}/${repo}/tree/${branch}/${basePath}/cloudformation`;
        window.open(cfUrl, '_blank');
        break;
        
      case 'terraform':
        // Open Terraform directory on GitHub
        const tfUrl = template.githubUrl.includes('/terraform') 
          ? template.githubUrl 
          : `https://github.com/${owner}/${repo}/tree/${branch}/${basePath}/terraform`;
        window.open(tfUrl, '_blank');
        break;
        
      case 'readme':
        // Open README file on GitHub
        window.open(`https://github.com/${owner}/${repo}/blob/${branch}/${basePath}/README.md`, '_blank');
        break;
        
      case 'template':
      default:
        // Open main template directory
        window.open(template.githubUrl, '_blank');
    }
  }
};