package com.cloudinfra.controller;

import com.cloudinfra.model.Template;
import com.cloudinfra.model.TemplateType;
import com.cloudinfra.model.Complexity;
import com.cloudinfra.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "http://localhost:3000")
public class TemplateController {
    
    @Autowired
    private TemplateService templateService;
    
    @GetMapping
    public ResponseEntity<List<Template>> getAllTemplates(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) TemplateType type,
            @RequestParam(required = false) Complexity complexity) {
        
        List<Template> templates;
        if (search != null || category != null || type != null || complexity != null) {
            templates = templateService.searchTemplates(search, category, type, complexity);
        } else {
            templates = templateService.getAllTemplates();
        }
        
        return ResponseEntity.ok(templates);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Template> getTemplateById(@PathVariable Long id) {
        Optional<Template> template = templateService.getTemplateById(id);
        return template.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Template>> getTemplatesByCategory(@PathVariable String category) {
        List<Template> templates = templateService.getTemplatesByCategory(category);
        return ResponseEntity.ok(templates);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Template>> getTemplatesByType(@PathVariable TemplateType type) {
        List<Template> templates = templateService.getTemplatesByType(type);
        return ResponseEntity.ok(templates);
    }
    
    @GetMapping("/popular")
    public ResponseEntity<List<Template>> getPopularTemplates() {
        List<Template> templates = templateService.getPopularTemplates();
        return ResponseEntity.ok(templates);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = templateService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping("/{id}/download")
    public ResponseEntity<Template> downloadTemplate(@PathVariable Long id) {
        try {
            Template template = templateService.incrementDownloadCount(id);
            return ResponseEntity.ok(template);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Template> createTemplate(@RequestBody Template template) {
        Template savedTemplate = templateService.saveTemplate(template);
        return ResponseEntity.ok(savedTemplate);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Template> updateTemplate(@PathVariable Long id, @RequestBody Template template) {
        template.setId(id);
        Template updatedTemplate = templateService.saveTemplate(template);
        return ResponseEntity.ok(updatedTemplate);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}