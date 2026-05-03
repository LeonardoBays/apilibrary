# Library - User Management System

A Spring Boot REST API for user management with PostgreSQL database integration.

## Project Structure

```
library/
├── src/main/kotlin/br/leo/library/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST API endpoints
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # JPA entities
│   ├── exceptions/      # Custom exceptions and exception handlers
│   ├── repository/      # Data access layer
│   ├── security/        # Security configuration
│   └── service/         # Business logic layer
├── build.gradle.kts     # Dependencies and build configuration
└── src/main/resources/application.yaml  # Application configuration
```

## Features

- User Registration with email validation
- User CRUD operations (Create, Read, Update, Delete)
- Password encryption using BCrypt
- Email uniqueness constraint
- Global exception handling
- API documentation with Swagger/OpenAPI
- PostgreSQL database integration

## Prerequisites

- Java 21+
- Kotlin 2.2.21+
- PostgreSQL 12+
- Gradle 8.x

## Setup Instructions

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE library_db;
```

Update database credentials in `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/library_db
    username: postgres
    password: postgres  # Change to your password
```

### 2. Build the Project

```bash
./gradlew clean build
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`

## API Endpoints

### User Registration
- **POST** `/api/users/register`
  - Register a new user
  - Body: `UserRegistrationDTO`
  - Returns: `UserResponseDTO` (HTTP 201 CREATED)

### Get User
- **GET** `/api/users/{id}`
  - Get user by ID
  - Returns: `UserResponseDTO`

- **GET** `/api/users/email/{email}`
  - Get user by email
  - Returns: `UserResponseDTO`

### Get All Users
- **GET** `/api/users`
  - Retrieve all users
  - Returns: List of `UserResponseDTO`

### Update User
- **PUT** `/api/users/{id}`
  - Update user information (name and email)
  - Body: `UserUpdateDTO`
  - Returns: `UserResponseDTO`

### Delete User
- **DELETE** `/api/users/{id}`
  - Permanently delete a user
  - Returns: HTTP 204 NO CONTENT

### Deactivate User
- **PUT** `/api/users/{id}/deactivate`
  - Soft delete - deactivate a user account
  - Returns: `UserResponseDTO`

### Change Password
- **POST** `/api/users/{id}/change-password`
  - Change password for a user
  - Body: `ChangePasswordRequest` {oldPassword, newPassword}
  - Returns: `UserResponseDTO`

## DTOs

### UserRegistrationDTO
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "passwordConfirmation": "password123"
}
```

### UserUpdateDTO
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

### UserResponseDTO
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "createdAt": "2026-05-03 10:30:45",
  "updatedAt": "2026-05-03 10:30:45",
  "active": true
}
```

## Swagger/OpenAPI Documentation

Access the interactive API documentation at:
`http://localhost:8080/swagger-ui.html`

## Exception Handling

The API includes global exception handling with proper HTTP status codes:

- **400 Bad Request**: Validation errors, password mismatch
- **404 Not Found**: User not found
- **409 Conflict**: Email already exists
- **500 Internal Server Error**: Unexpected errors

## Security Features

- Password hashing with BCrypt
- Email validation
- Password confirmation validation
- Unique email constraint at database level
- CSRF protection disabled for REST API
- Public registration endpoint, other endpoints require authentication

## Next Steps

Future enhancements can include:
- JWT token authentication
- Role-based access control (RBAC)
- Email verification
- Password reset functionality
- User profile management
- Audit logging
- Rate limiting

