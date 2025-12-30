package com.cloudinfra.service;

import com.cloudinfra.model.Template;
import com.cloudinfra.model.TemplateType;
import com.cloudinfra.model.Complexity;
import com.cloudinfra.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TemplateService {
    
    @Autowired
    private TemplateRepository templateRepository;
    
    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }
    
    public Optional<Template> getTemplateById(Long id) {
        return templateRepository.findById(id);
    }
    
    public List<Template> searchTemplates(String searchTerm, String category, 
                                        TemplateType type, Complexity complexity) {
        return templateRepository.findByFilters(
            category != null && !category.equals("all") ? category : null,
            type,
            complexity,
            searchTerm != null && !searchTerm.trim().isEmpty() ? searchTerm : null
        );
    }
    
    public List<Template> getTemplatesByCategory(String category) {
        return templateRepository.findByCategory(category);
    }
    
    public List<Template> getTemplatesByType(TemplateType type) {
        return templateRepository.findByType(type);
    }
    
    public List<Template> getPopularTemplates() {
        return templateRepository.findTop10ByOrderByDownloadCountDesc();
    }
    
    public List<String> getAllCategories() {
        return templateRepository.findAllCategories();
    }
    
    public Template saveTemplate(Template template) {
        return templateRepository.save(template);
    }
    
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }
    
    @Transactional
    public Template incrementDownloadCount(Long id) {
        Optional<Template> templateOpt = templateRepository.findById(id);
        if (templateOpt.isPresent()) {
            Template template = templateOpt.get();
            template.incrementDownloadCount();
            return templateRepository.save(template);
        }
        throw new RuntimeException("Template not found with id: " + id);
    }
}