package com.cloudinfra.model;

import java.util.List;
import java.util.Map;

public class DiagramData {
    private String mermaidSyntax;
    private String templateType;
    private List<DiagramNode> nodes;
    private List<DiagramEdge> edges;
    private Map<String, Object> metadata;

    public DiagramData() {}

    public DiagramData(String mermaidSyntax, String templateType) {
        this.mermaidSyntax = mermaidSyntax;
        this.templateType = templateType;
    }

    // Getters and Setters
    public String getMermaidSyntax() {
        return mermaidSyntax;
    }

    public void setMermaidSyntax(String mermaidSyntax) {
        this.mermaidSyntax = mermaidSyntax;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public List<DiagramNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<DiagramNode> nodes) {
        this.nodes = nodes;
    }

    public List<DiagramEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<DiagramEdge> edges) {
        this.edges = edges;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}