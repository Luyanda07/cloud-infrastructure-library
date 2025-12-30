package com.cloudinfra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "templates")
public class Template {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private TemplateType type;
    
    @NotBlank
    private String category;
    
    @ElementCollection
    @CollectionTable(name = "template_tags", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "tag")
    private List<String> tags;
    
    @NotBlank
    private String downloadUrl;
    
    private String githubUrl;
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private Complexity complexity;
    
    @ElementCollection
    @CollectionTable(name = "template_pillars", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "pillar")
    private List<String> wellArchitectedPillars;
    
    @Column(columnDefinition = "TEXT")
    private String templateContent;
    
    private String version;
    
    private Integer downloadCount = 0;
    
    // Constructors
    public Template() {}
    
    public Template(String name, String description, TemplateType type, String category) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.category = category;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public TemplateType getType() { return type; }
    public void setType(TemplateType type) { this.type = type; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public Complexity getComplexity() { return complexity; }
    public void setComplexity(Complexity complexity) { this.complexity = complexity; }
    
    public List<String> getWellArchitectedPillars() { return wellArchitectedPillars; }
    public void setWellArchitectedPillars(List<String> wellArchitectedPillars) { 
        this.wellArchitectedPillars = wellArchitectedPillars; 
    }
    
    public String getTemplateContent() { return templateContent; }
    public void setTemplateContent(String templateContent) { this.templateContent = templateContent; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
    
    public void incrementDownloadCount() {
        this.downloadCount = (this.downloadCount == null ? 0 : this.downloadCount) + 1;
    }
}