package com.cloudinfra.config;

import com.cloudinfra.model.Template;
import com.cloudinfra.model.TemplateType;
import com.cloudinfra.model.Complexity;
import com.cloudinfra.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private TemplateRepository templateRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (templateRepository.count() == 0) {
            initializeTemplates();
        }
    }
    
    private void initializeTemplates() {
        List<Template> templates = Arrays.asList(
            // Networking Templates
            createTemplate(
                "VPC with Public and Private Subnets",
                "Production-ready VPC with public and private subnets across multiple AZs, NAT gateways, and proper routing. Includes VPC Flow Logs and security groups.",
                TemplateType.CLOUDFORMATION,
                "Networking",
                Arrays.asList("vpc", "subnets", "nat-gateway", "routing", "multi-az"),
                Arrays.asList("Security", "Reliability", "Cost Optimization"),
                Complexity.INTERMEDIATE,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/networking/vpc-multi-az",
                "v1.2.0"
            ),
            
            createTemplate(
                "VPC with Public and Private Subnets (Terraform)",
                "Production-ready VPC with public and private subnets across multiple AZs, NAT gateways, and proper routing. Terraform implementation with modules.",
                TemplateType.TERRAFORM,
                "Networking",
                Arrays.asList("vpc", "subnets", "nat-gateway", "routing", "multi-az"),
                Arrays.asList("Security", "Reliability", "Cost Optimization"),
                Complexity.INTERMEDIATE,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/networking/vpc-multi-az",
                "v1.2.0"
            ),
            
            // Serverless Templates
            createTemplate(
                "Serverless API with Lambda and DynamoDB",
                "Complete serverless REST API using API Gateway, Lambda functions, and DynamoDB with proper IAM roles, monitoring, and X-Ray tracing.",
                TemplateType.CLOUDFORMATION,
                "Serverless",
                Arrays.asList("lambda", "api-gateway", "dynamodb", "iam", "serverless", "rest-api"),
                Arrays.asList("Cost Optimization", "Performance", "Security", "Operational Excellence"),
                Complexity.INTERMEDIATE,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/serverless/api-lambda-dynamodb",
                "v2.0.1"
            ),
            
            createTemplate(
                "Serverless API with Lambda and DynamoDB (Terraform)",
                "Complete serverless REST API using API Gateway, Lambda functions, and DynamoDB. Terraform implementation with proper resource management.",
                TemplateType.TERRAFORM,
                "Serverless",
                Arrays.asList("lambda", "api-gateway", "dynamodb", "iam", "serverless", "rest-api"),
                Arrays.asList("Cost Optimization", "Performance", "Security", "Operational Excellence"),
                Complexity.INTERMEDIATE,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/serverless/api-lambda-dynamodb",
                "v2.0.1"
            ),
            
            // Static Website Templates
            createTemplate(
                "S3 Static Website with CloudFront",
                "Static website hosting with S3, CloudFront CDN, Route 53 DNS, and SSL certificate. Includes logging and monitoring.",
                TemplateType.CLOUDFORMATION,
                "Static Websites",
                Arrays.asList("s3", "cloudfront", "route53", "ssl", "static-website", "cdn"),
                Arrays.asList("Cost Optimization", "Performance", "Security"),
                Complexity.BEGINNER,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/static-websites/s3-cloudfront",
                "v1.3.0"
            ),
            
            createTemplate(
                "S3 Static Website with CloudFront (Terraform)",
                "Static website hosting with S3, CloudFront CDN, Route 53 DNS, and SSL certificate. Terraform implementation with advanced features.",
                TemplateType.TERRAFORM,
                "Static Websites",
                Arrays.asList("s3", "cloudfront", "route53", "ssl", "static-website", "cdn"),
                Arrays.asList("Cost Optimization", "Performance", "Security"),
                Complexity.BEGINNER,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/static-websites/s3-cloudfront",
                "v1.3.0"
            ),
            
            // Web Application Templates
            createTemplate(
                "Auto Scaling Web Application",
                "Highly available web application with Application Load Balancer, Auto Scaling Group, and RDS database across multiple AZs.",
                TemplateType.CLOUDFORMATION,
                "Web Applications",
                Arrays.asList("alb", "asg", "rds", "web-app", "auto-scaling", "load-balancer"),
                Arrays.asList("Reliability", "Performance", "Security"),
                Complexity.ADVANCED,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/web-applications/auto-scaling-web-app",
                "v2.1.0"
            ),
            
            createTemplate(
                "Auto Scaling Web Application (Terraform)",
                "Highly available web application with Application Load Balancer, Auto Scaling Group, and RDS database. Terraform implementation with advanced monitoring.",
                TemplateType.TERRAFORM,
                "Web Applications",
                Arrays.asList("alb", "asg", "rds", "web-app", "auto-scaling", "load-balancer"),
                Arrays.asList("Reliability", "Performance", "Security"),
                Complexity.ADVANCED,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/web-applications/auto-scaling-web-app",
                "v2.1.0"
            ),
            
            // Container Templates
            createTemplate(
                "EKS Cluster with Managed Node Groups",
                "Production-ready EKS cluster with managed node groups, RBAC, monitoring, and security best practices.",
                TemplateType.TERRAFORM,
                "Containers",
                Arrays.asList("eks", "kubernetes", "node-groups", "rbac", "containers"),
                Arrays.asList("Security", "Reliability", "Performance", "Operational Excellence"),
                Complexity.ADVANCED,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/containers/eks-cluster",
                "v3.0.1"
            ),
            
            // Database Templates
            createTemplate(
                "RDS Multi-AZ with Read Replicas",
                "Production RDS setup with Multi-AZ deployment, read replicas, automated backups, and comprehensive monitoring.",
                TemplateType.TERRAFORM,
                "Databases",
                Arrays.asList("rds", "read-replica", "backup", "monitoring", "multi-az", "mysql"),
                Arrays.asList("Reliability", "Security", "Performance"),
                Complexity.ADVANCED,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/databases/rds-multi-az",
                "v1.5.2"
            ),
            
            createTemplate(
                "DynamoDB with Global Tables",
                "DynamoDB setup with Global Tables for multi-region replication, point-in-time recovery, and monitoring.",
                TemplateType.CLOUDFORMATION,
                "Databases",
                Arrays.asList("dynamodb", "global-tables", "backup", "monitoring", "nosql"),
                Arrays.asList("Reliability", "Performance", "Cost Optimization"),
                Complexity.INTERMEDIATE,
                "https://github.com/Luyanda07/cloud-infrastructure-templates/tree/main/databases/dynamodb-global",
                "v1.0.5"
            )
        );
        
        templateRepository.saveAll(templates);
    }
    
    private Template createTemplate(String name, String description, TemplateType type, 
                                  String category, List<String> tags, 
                                  List<String> pillars, Complexity complexity,
                                  String githubUrl, String version) {
        Template template = new Template(name, description, type, category);
        template.setTags(tags);
        template.setWellArchitectedPillars(pillars);
        template.setComplexity(complexity);
        template.setGithubUrl(githubUrl);
        template.setVersion(version);
        template.setDownloadUrl("/api/templates/" + name.toLowerCase().replace(" ", "-") + "/download");
        template.setLastUpdated(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
        template.setDownloadCount((int) (Math.random() * 1000));
        
        return template;
    }
}