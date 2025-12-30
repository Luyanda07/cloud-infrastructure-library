package com.cloudinfra.model;

public class DiagramNode {
    private String id;
    private String label;
    private String type;
    private String shape;
    private String color;

    public DiagramNode() {}

    public DiagramNode(String id, String label, String type) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.shape = getShapeForType(type);
        this.color = getColorForType(type);
    }

    private String getShapeForType(String type) {
        if (type.contains("Lambda")) return "((Lambda))";
        if (type.contains("S3")) return "[S3 Bucket]";
        if (type.contains("RDS") || type.contains("DynamoDB")) return "[(Database)]";
        if (type.contains("VPC") || type.contains("Subnet")) return "{Network}";
        if (type.contains("LoadBalancer") || type.contains("ALB")) return "{{Load Balancer}}";
        if (type.contains("EC2")) return "[EC2 Instance]";
        if (type.contains("CloudFront")) return "((CDN))";
        return "[" + type + "]";
    }

    private String getColorForType(String type) {
        if (type.contains("Lambda")) return "#FF9900";
        if (type.contains("S3")) return "#569A31";
        if (type.contains("RDS") || type.contains("DynamoDB")) return "#3F48CC";
        if (type.contains("VPC")) return "#FF4B4B";
        if (type.contains("LoadBalancer")) return "#8C4FFF";
        if (type.contains("EC2")) return "#FF9900";
        if (type.contains("CloudFront")) return "#8C4FFF";
        return "#232F3E";
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}