package com.cloudinfra.service;

import com.cloudinfra.model.DiagramData;
import com.cloudinfra.model.DiagramNode;
import com.cloudinfra.model.DiagramEdge;
import com.cloudinfra.model.Template;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Base64;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class DiagramService {

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public DiagramData generateDiagram(Template template) {
        try {
            if (template.getType().toString().equals("CLOUDFORMATION")) {
                return generateCloudFormationDiagram(template);
            } else {
                return generateTerraformDiagram(template);
            }
        } catch (Exception e) {
            // Return a basic diagram if parsing fails
            return createBasicDiagram(template);
        }
    }

    private DiagramData generateCloudFormationDiagram(Template template) {
        try {
            String content = fetchTemplateContent(template);
            JsonNode root;
            
            if (content.trim().startsWith("{")) {
                root = jsonMapper.readTree(content);
            } else {
                root = yamlMapper.readTree(content);
            }

            List<DiagramNode> nodes = new ArrayList<>();
            List<DiagramEdge> edges = new ArrayList<>();

            // Parse Resources section
            JsonNode resources = root.get("Resources");
            if (resources != null) {
                resources.fields().forEachRemaining(entry -> {
                    String resourceId = entry.getKey();
                    JsonNode resource = entry.getValue();
                    String resourceType = resource.get("Type").asText();
                    
                    nodes.add(new DiagramNode(resourceId, resourceId, resourceType));
                });

                // Create relationships based on common patterns
                createCloudFormationRelationships(resources, edges);
            }

            // Generate both Mermaid and Draw.io formats
            String mermaidSyntax = generateMermaidSyntax(nodes, edges, template.getName());
            String drawioXml = generateDrawioXml(nodes, edges, template.getName());
            
            DiagramData diagramData = new DiagramData(mermaidSyntax, "CloudFormation");
            diagramData.setNodes(nodes);
            diagramData.setEdges(edges);
            
            // Add Draw.io data
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("drawioXml", drawioXml);
            metadata.put("drawioUrl", generateDrawioUrl(drawioXml, template.getName()));
            diagramData.setMetadata(metadata);
            
            return diagramData;
        } catch (Exception e) {
            return createBasicDiagram(template);
        }
    }

    private DiagramData generateTerraformDiagram(Template template) {
        try {
            String content = fetchTemplateContent(template);
            List<DiagramNode> nodes = new ArrayList<>();
            List<DiagramEdge> edges = new ArrayList<>();

            // Parse Terraform resources using regex
            Pattern resourcePattern = Pattern.compile("resource\\s+\"([^\"]+)\"\\s+\"([^\"]+)\"\\s*\\{");
            Matcher matcher = resourcePattern.matcher(content);

            while (matcher.find()) {
                String resourceType = matcher.group(1);
                String resourceName = matcher.group(2);
                nodes.add(new DiagramNode(resourceName, resourceName, resourceType));
            }

            // Create basic relationships for Terraform
            createTerraformRelationships(content, nodes, edges);

            String mermaidSyntax = generateMermaidSyntax(nodes, edges, template.getName());
            String drawioXml = generateDrawioXml(nodes, edges, template.getName());
            
            DiagramData diagramData = new DiagramData(mermaidSyntax, "Terraform");
            diagramData.setNodes(nodes);
            diagramData.setEdges(edges);
            
            // Add Draw.io data
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("drawioXml", drawioXml);
            metadata.put("drawioUrl", generateDrawioUrl(drawioXml, template.getName()));
            diagramData.setMetadata(metadata);
            
            return diagramData;
        } catch (Exception e) {
            return createBasicDiagram(template);
        }
    }

    private String generateDrawioXml(List<DiagramNode> nodes, List<DiagramEdge> edges, String title) {
        StringBuilder xml = new StringBuilder();
        
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<mxfile host=\"app.diagrams.net\" modified=\"2025-12-29\" agent=\"CloudInfraLibrary\" version=\"21.6.5\">\n");
        xml.append("  <diagram name=\"").append(escapeXml(title)).append("\" id=\"arch\">\n");
        xml.append("    <mxGraphModel dx=\"1422\" dy=\"794\" grid=\"1\" gridSize=\"10\" guides=\"1\" tooltips=\"1\" connect=\"1\" arrows=\"1\" fold=\"1\" page=\"1\" pageScale=\"1\" pageWidth=\"827\" pageHeight=\"1169\">\n");
        xml.append("      <root>\n");
        xml.append("        <mxCell id=\"0\" />\n");
        xml.append("        <mxCell id=\"1\" parent=\"0\" />\n");

        // Add title
        xml.append("        <mxCell id=\"title\" value=\"").append(escapeXml(title)).append("\" style=\"text;html=1;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;whiteSpace=wrap;rounded=0;fontSize=16;fontStyle=1;\" vertex=\"1\" parent=\"1\">\n");
        xml.append("          <mxGeometry x=\"20\" y=\"20\" width=\"400\" height=\"30\" as=\"geometry\" />\n");
        xml.append("        </mxCell>\n");

        // Calculate layout positions
        int startX = 100;
        int startY = 80;
        int nodeWidth = 80;
        int nodeHeight = 60;
        int spacingX = 150;
        int spacingY = 120;

        // Add nodes with simplified AWS styling
        for (int i = 0; i < nodes.size(); i++) {
            DiagramNode node = nodes.get(i);
            int x = startX + (i % 4) * spacingX;
            int y = startY + (i / 4) * spacingY;
            
            String awsIcon = getAwsIconStyle(node.getType());
            String nodeStyle = String.format("sketch=0;outlineConnect=0;fontColor=#232F3E;gradientColor=#F78E04;gradientDirection=north;fillColor=#D05C17;strokeColor=#ffffff;dashed=0;verticalLabelPosition=bottom;verticalAlign=top;align=center;html=1;fontSize=12;fontStyle=0;aspect=fixed;shape=mxgraph.aws4.%s", awsIcon);
            
            xml.append("        <mxCell id=\"").append(escapeXml(node.getId())).append("\" value=\"").append(escapeXml(node.getLabel())).append("\" style=\"").append(nodeStyle).append("\" vertex=\"1\" parent=\"1\">\n");
            xml.append("          <mxGeometry x=\"").append(x).append("\" y=\"").append(y).append("\" width=\"").append(nodeWidth).append("\" height=\"").append(nodeHeight).append("\" as=\"geometry\" />\n");
            xml.append("        </mxCell>\n");
        }

        // Add edges with simplified styling
        int edgeId = 1000;
        for (DiagramEdge edge : edges) {
            String edgeLabel = edge.getLabel() != null ? escapeXml(edge.getLabel()) : "";
            xml.append("        <mxCell id=\"edge").append(edgeId++).append("\" value=\"").append(edgeLabel).append("\" style=\"endArrow=classic;html=1;rounded=0;fontSize=11;fontColor=#232F3E;strokeColor=#FF9900;strokeWidth=2;\" edge=\"1\" parent=\"1\" source=\"").append(escapeXml(edge.getFrom())).append("\" target=\"").append(escapeXml(edge.getTo())).append("\">\n");
            xml.append("          <mxGeometry relative=\"1\" as=\"geometry\" />\n");
            xml.append("        </mxCell>\n");
        }

        xml.append("      </root>\n");
        xml.append("    </mxGraphModel>\n");
        xml.append("  </diagram>\n");
        xml.append("</mxfile>");

        return xml.toString();
    }

    private String getAwsIconStyle(String resourceType) {
        // Map AWS resource types to Draw.io AWS icon names (simplified)
        if (resourceType.contains("VPC")) return "vpc";
        if (resourceType.contains("Subnet")) return "vpc_subnet";
        if (resourceType.contains("InternetGateway")) return "vpc_internet_gateway";
        if (resourceType.contains("NatGateway")) return "vpc_nat_gateway";
        if (resourceType.contains("LoadBalancer") || resourceType.contains("ALB")) return "elastic_load_balancing";
        if (resourceType.contains("Instance") || resourceType.contains("EC2")) return "ec2_instance";
        if (resourceType.contains("AutoScaling")) return "ec2_auto_scaling";
        if (resourceType.contains("Lambda")) return "lambda_function";
        if (resourceType.contains("ApiGateway")) return "api_gateway";
        if (resourceType.contains("S3")) return "s3_bucket";
        if (resourceType.contains("RDS")) return "rds_db_instance";
        if (resourceType.contains("DynamoDB")) return "dynamodb_table";
        if (resourceType.contains("CloudFront")) return "cloudfront_distribution";
        if (resourceType.contains("Route53")) return "route_53";
        if (resourceType.contains("EKS")) return "eks_cluster";
        if (resourceType.contains("SecurityGroup")) return "vpc_security_group";
        
        // Default AWS resource icon
        return "generic_saml_token";
    }

    private String generateDrawioUrl(String xmlContent, String title) {
        try {
            // Create a simpler URL approach
            String encodedXml = Base64.getEncoder().encodeToString(xmlContent.getBytes(StandardCharsets.UTF_8));
            String encodedTitle = URLEncoder.encode(title.replaceAll("[^a-zA-Z0-9\\s]", ""), StandardCharsets.UTF_8);
            
            // Use a simpler Draw.io URL format
            return String.format("https://app.diagrams.net/?lightbox=1&edit=_blank&layers=1&nav=1&title=%s#R%s", 
                encodedTitle, encodedXml);
        } catch (Exception e) {
            // Fallback to basic Draw.io URL
            return "https://app.diagrams.net/";
        }
    }

    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    // Keep existing methods for Mermaid compatibility
    private void createCloudFormationRelationships(JsonNode resources, List<DiagramEdge> edges) {
        List<String> loadBalancers = new ArrayList<>();
        List<String> instances = new ArrayList<>();
        List<String> databases = new ArrayList<>();
        List<String> storage = new ArrayList<>();
        List<String> networks = new ArrayList<>();

        // Categorize resources
        resources.fields().forEachRemaining(entry -> {
            String resourceId = entry.getKey();
            String resourceType = entry.getValue().get("Type").asText();
            
            if (resourceType.contains("LoadBalancer") || resourceType.contains("ALB")) {
                loadBalancers.add(resourceId);
            } else if (resourceType.contains("Instance") || resourceType.contains("AutoScaling")) {
                instances.add(resourceId);
            } else if (resourceType.contains("RDS") || resourceType.contains("DynamoDB")) {
                databases.add(resourceId);
            } else if (resourceType.contains("S3") || resourceType.contains("EFS")) {
                storage.add(resourceId);
            } else if (resourceType.contains("VPC") || resourceType.contains("Subnet")) {
                networks.add(resourceId);
            }
        });

        // Create logical relationships
        for (String lb : loadBalancers) {
            for (String instance : instances) {
                edges.add(new DiagramEdge(lb, instance, "routes to"));
            }
        }

        for (String instance : instances) {
            for (String db : databases) {
                edges.add(new DiagramEdge(instance, db, "connects to"));
            }
            for (String s3 : storage) {
                edges.add(new DiagramEdge(instance, s3, "stores in"));
            }
        }
    }

    private void createTerraformRelationships(String content, List<DiagramNode> nodes, List<DiagramEdge> edges) {
        // Simple relationship detection based on common patterns
        for (DiagramNode node1 : nodes) {
            for (DiagramNode node2 : nodes) {
                if (!node1.getId().equals(node2.getId())) {
                    // Check if one resource references another
                    if (content.contains(node1.getId()) && content.contains(node2.getId())) {
                        String relationship = determineRelationship(node1.getType(), node2.getType());
                        if (relationship != null) {
                            edges.add(new DiagramEdge(node1.getId(), node2.getId(), relationship));
                        }
                    }
                }
            }
        }
    }

    private String determineRelationship(String type1, String type2) {
        if (type1.contains("aws_lb") && type2.contains("aws_instance")) {
            return "routes to";
        }
        if (type1.contains("aws_instance") && type2.contains("aws_db")) {
            return "connects to";
        }
        if (type1.contains("aws_instance") && type2.contains("aws_s3")) {
            return "stores in";
        }
        if (type1.contains("aws_api_gateway") && type2.contains("aws_lambda")) {
            return "invokes";
        }
        return null;
    }

    private String generateMermaidSyntax(List<DiagramNode> nodes, List<DiagramEdge> edges, String title) {
        StringBuilder mermaid = new StringBuilder();
        mermaid.append("graph TD\n");
        mermaid.append("    %% ").append(title).append(" Architecture\n\n");

        // Add nodes
        for (DiagramNode node : nodes) {
            String shape = node.getShape();
            mermaid.append("    ").append(node.getId()).append(shape).append("\n");
        }

        mermaid.append("\n");

        // Add edges
        for (DiagramEdge edge : edges) {
            mermaid.append("    ").append(edge.getFrom())
                   .append(" --> ")
                   .append(edge.getTo());
            if (edge.getLabel() != null && !edge.getLabel().isEmpty()) {
                mermaid.append("|").append(edge.getLabel()).append("|");
            }
            mermaid.append("\n");
        }

        // Add styling
        mermaid.append("\n    %% Styling\n");
        for (int i = 0; i < nodes.size(); i++) {
            DiagramNode node = nodes.get(i);
            if (node.getColor() != null) {
                mermaid.append("    classDef class").append(i).append(" fill:").append(node.getColor()).append("\n");
                mermaid.append("    class ").append(node.getId()).append(" class").append(i).append("\n");
            }
        }

        return mermaid.toString();
    }

    private DiagramData createBasicDiagram(Template template) {
        String basicMermaid = String.format("""
            graph TD
                A[User] --> B[%s]
                B --> C[AWS Resources]
                C --> D[Output]
                
                %% Basic diagram for %s
                classDef aws fill:#FF9900
                class B,C aws
            """, template.getName(), template.getName());

        DiagramData diagramData = new DiagramData(basicMermaid, template.getType().toString());
        
        // Add basic Draw.io diagram
        Map<String, Object> metadata = new HashMap<>();
        String basicDrawio = generateBasicDrawioXml(template.getName());
        metadata.put("drawioXml", basicDrawio);
        metadata.put("drawioUrl", generateDrawioUrl(basicDrawio, template.getName()));
        diagramData.setMetadata(metadata);

        return diagramData;
    }

    private String generateBasicDrawioXml(String templateName) {
        return String.format("""
            <?xml version="1.0" encoding="UTF-8"?>
            <mxfile host="app.diagrams.net">
              <diagram name="%s" id="basic">
                <mxGraphModel dx="1422" dy="794" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="827" pageHeight="1169">
                  <root>
                    <mxCell id="0" />
                    <mxCell id="1" parent="0" />
                    <mxCell id="user" value="User" style="sketch=0;outlineConnect=0;fontColor=#232F3E;gradientColor=none;fillColor=#232F3D;strokeColor=none;dashed=0;verticalLabelPosition=bottom;verticalAlign=top;align=center;html=1;fontSize=12;fontStyle=0;aspect=fixed;shape=mxgraph.aws4.user;" vertex="1" parent="1">
                      <mxGeometry x="100" y="100" width="60" height="60" as="geometry" />
                    </mxCell>
                    <mxCell id="template" value="%s" style="sketch=0;outlineConnect=0;fontColor=#232F3E;gradientColor=#F78E04;gradientDirection=north;fillColor=#D05C17;strokeColor=#ffffff;dashed=0;verticalLabelPosition=bottom;verticalAlign=top;align=center;html=1;fontSize=12;fontStyle=0;aspect=fixed;shape=mxgraph.aws4.generic_saml_token;" vertex="1" parent="1">
                      <mxGeometry x="250" y="100" width="60" height="60" as="geometry" />
                    </mxCell>
                    <mxCell id="edge1" style="endArrow=classic;html=1;rounded=0;fontSize=11;fontColor=#232F3E;strokeColor=#FF9900;strokeWidth=2;" edge="1" parent="1" source="user" target="template">
                      <mxGeometry relative="1" as="geometry" />
                    </mxCell>
                  </root>
                </mxGraphModel>
              </diagram>
            </mxfile>
            """, escapeXml(templateName), escapeXml(templateName));
    }

    private String fetchTemplateContent(Template template) {
        // In a real implementation, this would fetch from GitHub
        // For now, return a sample based on template type
        return getSampleContent(template);
    }

    private String getSampleContent(Template template) {
        // Return sample content based on template name for demonstration
        if (template.getName().contains("VPC")) {
            return """
                Resources:
                  VPC:
                    Type: AWS::EC2::VPC
                    Properties:
                      CidrBlock: 10.0.0.0/16
                  PublicSubnet:
                    Type: AWS::EC2::Subnet
                    Properties:
                      VpcId: !Ref VPC
                  PrivateSubnet:
                    Type: AWS::EC2::Subnet
                    Properties:
                      VpcId: !Ref VPC
                  InternetGateway:
                    Type: AWS::EC2::InternetGateway
                """;
        } else if (template.getName().contains("Lambda")) {
            return """
                Resources:
                  ApiGateway:
                    Type: AWS::ApiGateway::RestApi
                  LambdaFunction:
                    Type: AWS::Lambda::Function
                  DynamoDBTable:
                    Type: AWS::DynamoDB::Table
                """;
        }
        return "Resources:\n  BasicResource:\n    Type: AWS::CloudFormation::WaitConditionHandle";
    }
}