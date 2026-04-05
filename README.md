# Inventory Service - Multi-Tenant Dealer & Vehicle Management

A modular monolith microservice for managing dealers and their vehicles in a multi-tenant environment. Built with Spring Boot, following clean architecture principles.

## 📋 Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Security](#security)
- [Testing](#testing)
- [Architecture](#architecture)
- [Contributing](#contributing)

## ✨ Features

### Core Functionality
- **Dealer Management**: CRUD operations for dealers with subscription types (BASIC/PREMIUM)
- **Vehicle Management**: CRUD operations for vehicles linked to dealers
- **Multi-Tenant**: Complete tenant isolation using `X-Tenant-Id` header
- **Advanced Filtering**: Search vehicles by model, status, price range, and dealer subscription
- **Pagination & Sorting**: All list endpoints support pagination and sorting
- **Admin Analytics**: Global dealer counts by subscription type

### Technical Features
- **Clean Architecture**: Clear separation of controllers, services, repositories, and entities
- **Security**: Role-based access control (USER, GLOBAL_ADMIN)
- **Validation**: Comprehensive input validation with meaningful error messages
- **Documentation**: Complete Swagger/OpenAPI documentation
- **Database Migrations**: Flyway-managed schema evolution
- **Error Handling**: Global exception handling with consistent responses

## 🛠 Tech Stack

- **Framework**: Spring Boot 4.0.5
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA + Hibernate
- **Migration**: Flyway
- **Security**: Spring Security
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Maven
- **Testing**: JUnit + Spring Boot Test

## 📋 Prerequisites

- **Java**: JDK 21 or higher
- **Maven**: 3.6+ (or use included Maven wrapper)
- **PostgreSQL**: 12+ running locally
- **Database**: Create database `inventorydb`

```sql
CREATE DATABASE inventorydb;
```

## 🚀 Quick Start

### 1. Clone & Build
```bash
git clone <repository-url>
cd inventory-service
./mvnw clean install
```

### 2. Configure Database
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventorydb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run Application
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access Swagger UI
- **URL**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

## 📚 API Documentation

### Authentication
- **Basic Auth** required for all endpoints
- **Credentials**:
  - Admin: `admin:admin` (GLOBAL_ADMIN role)
  - User: `user:password` (USER role)

### Headers
- **X-Tenant-Id**: Required for all requests (e.g., `tenant1`)

### Endpoints Overview

#### Dealers
- `POST /dealers` - Create dealer
- `GET /dealers/{id}` - Get dealer by ID
- `GET /dealers` - List dealers (pagination, filters: name, email)
- `PATCH /dealers/{id}` - Update dealer
- `DELETE /dealers/{id}` - Delete dealer

#### Vehicles
- `POST /vehicles` - Create vehicle
- `GET /vehicles/{id}` - Get vehicle by ID
- `GET /vehicles` - List vehicles (pagination, filters: model, status, priceMin, priceMax)
- `GET /vehicles?subscription=PREMIUM` - List vehicles from PREMIUM dealers
- `PATCH /vehicles/{id}` - Update vehicle
- `DELETE /vehicles/{id}` - Delete vehicle

#### Admin (GLOBAL_ADMIN only)
- `GET /admin/dealers/countBySubscription` - Global dealer counts by subscription

### Response Codes
- `200` - Success
- `400` - Bad Request (missing X-Tenant-Id, validation errors)
- `403` - Forbidden (cross-tenant access, insufficient permissions)
- `404` - Not Found
- `500` - Internal Server Error

## 🗄 Database Schema

### Tables

#### dealers
```sql
CREATE TABLE dealers (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    subscription_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

#### vehicles
```sql
CREATE TABLE vehicles (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    dealer_id UUID NOT NULL REFERENCES dealers(id),
    model VARCHAR(255) NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### Indexes
- `dealers.tenant_id`
- `dealers.email` (unique)
- `vehicles.tenant_id`
- `vehicles.dealer_id`

## 🔒 Security

### Multi-Tenancy
- **Tenant Isolation**: All queries filtered by `tenant_id`
- **Header Required**: `X-Tenant-Id` must be present in all requests
- **Cross-Tenant Protection**: Users cannot access data from other tenants

### Authentication & Authorization
- **Basic Authentication**: Username/password required
- **Roles**:
  - `USER`: Access to dealer/vehicle endpoints
  - `GLOBAL_ADMIN`: Access to all endpoints including admin analytics
- **Admin Protection**: `/admin/**` requires GLOBAL_ADMIN role

### Security Configuration
- CSRF disabled for API usage
- Swagger endpoints publicly accessible
- All other endpoints require authentication

## 🧪 Testing

### Manual Testing with cURL

#### Create Dealer
```bash
curl -X POST http://localhost:8080/dealers \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: tenant1" \
  -u admin:admin \
  -d '{
    "name": "Acme Motors",
    "email": "acme@example.com",
    "subscriptionType": "BASIC"
  }'
```

#### List Dealers
```bash
curl -X GET "http://localhost:8080/dealers?page=0&size=10" \
  -H "X-Tenant-Id: tenant1" \
  -u admin:admin
```

#### Create Vehicle
```bash
curl -X POST http://localhost:8080/vehicles \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: tenant1" \
  -u admin:admin \
  -d '{
    "dealerId": "<dealer-id>",
    "model": "Tesla Model X",
    "price": 89999.99,
    "status": "AVAILABLE"
  }'
```

#### Admin Counts
```bash
curl -X GET http://localhost:8080/admin/dealers/countBySubscription \
  -H "X-Tenant-Id: tenant1" \
  -u admin:admin
```

### Automated Testing
```bash
./mvnw test
```

## 🏗 Architecture

### Clean Architecture Layers

```
┌─────────────────┐
│   Controllers   │  REST endpoints, validation
├─────────────────┤
│    Services     │  Business logic, tenant enforcement
├─────────────────┤
│  Repositories   │  Data access, JPA queries
├─────────────────┤
│    Entities     │  Domain models, JPA mappings
├─────────────────┤
│      DTOs       │  Data transfer objects
└─────────────────┘
```

### Key Components

- **TenantFilter**: Intercepts requests, validates X-Tenant-Id header
- **SecurityConfig**: Configures authentication and authorization
- **GlobalExceptionHandler**: Centralized error handling
- **SwaggerConfig**: API documentation setup

### Design Patterns
- **Repository Pattern**: Data access abstraction
- **Service Layer**: Business logic encapsulation
- **DTO Pattern**: Clean data transfer
- **Filter Pattern**: Cross-cutting concerns (tenant, security)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Ensure Swagger documentation is updated
5. Submit a pull request

### Code Standards
- Follow Spring Boot conventions
- Use meaningful commit messages
- Include unit tests for new features
- Update documentation for API changes

---