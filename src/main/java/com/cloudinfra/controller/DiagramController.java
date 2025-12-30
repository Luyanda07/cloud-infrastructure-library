package com.cloudinfra.controller;

import com.cloudinfra.model.DiagramData;
import com.cloudinfra.model.Template;
import com.cloudinfra.service.DiagramService;
import com.cloudinfra.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/diagrams")
@CrossOrigin(origins = "http://localhost:3000")
public class DiagramController {

    @Autowired
    private DiagramService diagramService;

    @Autowired
    private TemplateService templateService;

    @GetMapping("/template/{id}")
    public ResponseEntity<DiagramData> generateDiagram(@PathVariable Long id) {
        try {
            Optional<Template> templateOpt = templateService.getTemplateById(id);
            if (templateOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Template template = templateOpt.get();
            DiagramData diagram = diagramService.generateDiagram(template);
            return ResponseEntity.ok(diagram);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/preview")
    public ResponseEntity<DiagramData> previewDiagram(@RequestBody DiagramPreviewRequest request) {
        try {
            // Create a temporary template for preview
            Template tempTemplate = new Template();
            tempTemplate.setName(request.getName());
            tempTemplate.setType(request.getType());
            
            DiagramData diagram = diagramService.generateDiagram(tempTemplate);
            return ResponseEntity.ok(diagram);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public static class DiagramPreviewRequest {
        private String name;
        private com.cloudinfra.model.TemplateType type;
        private String content;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public com.cloudinfra.model.TemplateType getType() {
            return type;
        }

        public void setType(com.cloudinfra.model.TemplateType type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}