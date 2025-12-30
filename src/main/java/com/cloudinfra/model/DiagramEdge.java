package com.cloudinfra.model;

public class DiagramEdge {
    private String from;
    private String to;
    private String label;
    private String type;

    public DiagramEdge() {}

    public DiagramEdge(String from, String to, String label) {
        this.from = from;
        this.to = to;
        this.label = label;
        this.type = "arrow";
    }

    public DiagramEdge(String from, String to, String label, String type) {
        this.from = from;
        this.to = to;
        this.label = label;
        this.type = type;
    }

    // Getters and Setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
}