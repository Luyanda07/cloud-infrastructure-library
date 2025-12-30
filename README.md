# Cloud Infrastructure Library - Web Application

A modern web application for browsing, searching, and accessing AWS CloudFormation and Terraform templates. This application provides a user-friendly interface to explore infrastructure-as-code templates from the [cloud-infrastructure-templates](https://github.com/Luyanda07/cloud-infrastructure-templates) repository.

## 🌟 Features

- **Template Library**: 11+ production-ready AWS infrastructure templates
- **Dual Format Support**: Both CloudFormation (YAML/JSON) and Terraform (HCL) versions
- **Web Interface**: Modern React frontend for easy template discovery
- **REST API**: Spring Boot backend with comprehensive template management
- **Search & Filter**: Find templates by category, type, or keywords
- **Direct GitHub Integration**: One-click access to template files
- **Well-Architected**: Templates follow AWS Well-Architected Framework principles

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────────────────────┐
│   React Frontend │    │ Spring Boot API │    │ GitHub Templates Repository     │
│   (Port 3000)   │◄──►│   (Port 8081)   │◄──►│ cloud-infrastructure-templates  │
└─────────────────┘    └─────────────────┘    └─────────────────────────────────┘
```

## 📋 Template Categories

### 🌐 Networking
- **VPC Multi-AZ**: Production-ready VPC with multiple availability zones
- **Security Groups**: Common security group configurations

### ⚡ Serverless
- **API Gateway + Lambda + DynamoDB**: Complete serverless API stack
- **Lambda Functions**: Event-driven compute templates

### 🗄️ Databases
- **RDS Multi-AZ**: High-availability relational database setup
- **DynamoDB Global Tables**: Global NoSQL database with replication

### 📦 Containers
- **EKS Cluster**: Managed Kubernetes cluster with worker nodes
- **Container Services**: ECS and Fargate configurations

### 🌍 Static Websites
- **S3 + CloudFront**: Global content delivery for static sites
- **Route 53**: DNS and domain management

### 🖥️ Web Applications
- **Auto Scaling Web App**: Load-balanced web application infrastructure
- **Application Load Balancer**: Advanced load balancing configurations

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Node.js 18+
- Git

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/cloud-infrastructure-library.git
   cd cloud-infrastructure-library
   ```

2. **Start the backend**
   ```bash
   ./apache-maven-3.9.6/bin/mvn spring-boot:run
   ```

3. **Start the frontend** (in a new terminal)
   ```bash
   cd frontend
   npm install
   npm start
   ```

4. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8081

## 🛠️ Technology Stack

### Backend
- **Java 21**: Latest LTS version with modern language features
- **Spring Boot 3.2**: Enterprise-grade framework
- **Spring Data JPA**: Database abstraction layer
- **H2 Database**: In-memory database for development
- **Maven 3.9.6**: Dependency management and build tool

### Frontend
- **React 18**: Modern UI library with hooks
- **Axios**: HTTP client for API communication
- **CSS3**: Modern styling with flexbox and grid
- **Responsive Design**: Mobile-first approach

### Infrastructure Templates
- **CloudFormation**: AWS native infrastructure-as-code
- **Terraform**: Multi-cloud infrastructure provisioning
- **YAML/JSON**: Human-readable configuration formats

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/templates` | Get all templates |
| GET | `/api/templates/search?query={query}` | Search templates |
| GET | `/api/templates/category/{category}` | Filter by category |
| GET | `/api/templates/type/{type}` | Filter by type (CloudFormation/Terraform) |

## 📁 Project Structure

```
cloud-infrastructure-library/
├── src/main/java/                 # Spring Boot backend
│   └── com/cloudinfra/
│       ├── controller/            # REST controllers
│       ├── model/                 # Data models
│       ├── repository/            # Data access layer
│       ├── service/               # Business logic
│       └── config/                # Configuration classes
├── frontend/                      # React frontend
│   ├── src/
│   │   ├── components/            # React components
│   │   ├── services/              # API services
│   │   └── App.js                 # Main application
│   └── public/                    # Static assets
├── apache-maven-3.9.6/           # Local Maven installation
├── README.md                     # Project documentation
├── TECHNICAL_DOCUMENTATION.md    # Technical details
├── SETUP_GUIDE.md               # Local setup guide
└── pom.xml                       # Maven configuration
```

## 🔗 Related Repositories

- **Templates Repository**: [cloud-infrastructure-templates](https://github.com/Luyanda07/cloud-infrastructure-templates) - Contains all the CloudFormation and Terraform templates

## 🔧 Configuration

### Backend Configuration
- **Port**: 8081 (configurable in `application.yml`)
- **Database**: H2 in-memory (auto-configured)
- **CORS**: Enabled for localhost:3000

### Frontend Configuration
- **Port**: 3000 (configurable in `package.json`)
- **API Base URL**: http://localhost:8081
- **Build Output**: `frontend/build/`

## 🚀 Deployment

### Backend Deployment
```bash
./apache-maven-3.9.6/bin/mvn clean package
java -jar target/cloud-infra-library-1.0.0.jar
```

### Frontend Deployment
```bash
cd frontend
npm run build
# Serve the build/ directory with any web server
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- AWS CloudFormation documentation
- Terraform AWS provider documentation
- Spring Boot community
- React community

