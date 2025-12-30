package com.cloudinfra.model;

public enum TemplateType {
    CLOUDFORMATION("CloudFormation"),
    TERRAFORM("Terraform");
    
    private final String displayName;
    
    TemplateType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}