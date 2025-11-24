# User Management System - 2-Day Progress Demo

## Overview
A user management REST API built with Spring Boot for managing users and roles in an omnichannel application.

## What's Implemented (2 Days Work)

### Day 1 - Database Design & Models
✅ **Database Schema**
- Users table with full user information
- Roles table for role management
- User-Role mapping table (many-to-many relationship)

✅ **Entity Models**
- User entity with enums (UserType, UserStatus, KycStatus)
- Role entity
- UserRole mapping entity
- Proper JPA relationships and UUID primary keys

### Day 2 - API Development
✅ **RESTful API Endpoints**
- Full CRUD operations for Users
- Full CRUD operations for Roles
- Role assignment/removal for users
- Filtering by user type and status

✅ **Features**
- Input validation
- Exception handling with proper HTTP status codes
- DTO pattern for clean API contracts
- Transaction management
- Automatic timestamps (created_at, updated_at)

## Technology Stack
- **Framework**: Spring Boot 3.5.7
- **Java**: 17
- **Database**: MySQL 8
- **ORM**: Spring Data JPA (Hibernate)
- **Build Tool**: Maven

## Database Tables

### 1. users
- user_id (UUID, PK)
- first_name, last_name, email (unique), phone
- user_type (RETAIL, CORPORATE, ADMIN)
- status (ACTIVE, INACTIVE, LOCKED)
- kyc_status (PENDING, VERIFIED, REJECTED)
- created_at, updated_at

### 2. roles
- role_id (UUID, PK)
- name (unique), description

### 3. user_roles
- id (UUID, PK)
- user_id (FK), role_id (FK)

## Quick Start

### 1. Setup Database
```sql
CREATE DATABASE omni_db;
```

### 2. Run Application
```bash
./mvnw spring-boot:run
```

Application will start on: `http://localhost:8082`

### 3. Test APIs

**Create a User:**
```bash
curl -X POST http://localhost:8082/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "userType": "RETAIL"
  }'
```

**Get All Users:**
```bash
curl http://localhost:8082/api/users
```

**Create a Role:**
```bash
curl -X POST http://localhost:8082/api/roles \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MANAGER",
    "description": "Manager role"
  }'
```

**Assign Role to User:**
```bash
curl -X POST http://localhost:8082/api/roles/{roleId}/users/{userId}
```

## API Endpoints

### User Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create new user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| GET | `/api/users/type/{type}` | Get users by type |
| GET | `/api/users/status/{status}` | Get users by status |

### Role Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/roles` | Create new role |
| GET | `/api/roles` | Get all roles |
| GET | `/api/roles/{id}` | Get role by ID |
| PUT | `/api/roles/{id}` | Update role |
| DELETE | `/api/roles/{id}` | Delete role |
| POST | `/api/roles/{roleId}/users/{userId}` | Assign role to user |
| DELETE | `/api/roles/{roleId}/users/{userId}` | Remove role from user |

## Sample Response Format

**Success Response:**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "userType": "RETAIL",
    "status": "ACTIVE",
    "kycStatus": "PENDING",
    "roles": ["MANAGER"],
    "createdAt": "2025-11-18T10:30:00",
    "updatedAt": "2025-11-18T10:30:00"
  },
  "timestamp": "2025-11-18T10:30:00"
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "User not found with id: xxx",
  "data": null,
  "timestamp": "2025-11-18T10:30:00"
}
```

## Key Features Demonstrated

1. **Clean Architecture**
   - Controller → Service → Repository layers
   - DTO pattern for API contracts
   - Proper separation of concerns

2. **Database Design**
   - Proper relationships (One-to-Many, Many-to-Many)
   - UUID for primary keys
   - Enums for status fields
   - Automatic timestamps

3. **Best Practices**
   - Input validation with Jakarta Bean Validation
   - Global exception handling
   - Transaction management
   - RESTful API design
   - Proper HTTP status codes

4. **Error Handling**
   - Custom exceptions (ResourceNotFoundException, ResourceAlreadyExistsException)
   - Validation error messages
   - Consistent error response format

## Project Structure
```
src/main/java/com/kpmg/Omnichannel/
├── controller/
│   ├── UserController.java
│   └── RoleController.java
├── dto/
│   ├── UserRequest.java
│   ├── UserResponse.java
│   ├── RoleRequest.java
│   ├── RoleResponse.java
│   └── ApiResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── ResourceAlreadyExistsException.java
├── model/
│   ├── User.java
│   ├── Role.java
│   └── UserRole.java
├── repository/
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   └── UserRoleRepository.java
└── service/
    ├── UserService.java
    └── RoleService.java
```

## Testing Examples

### Create Test Data
```bash
# Create users
curl -X POST http://localhost:8082/api/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Smith","email":"alice@example.com","phone":"1111111111","userType":"RETAIL"}'

curl -X POST http://localhost:8082/api/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Bob","lastName":"Johnson","email":"bob@example.com","phone":"2222222222","userType":"CORPORATE"}'

# Create roles
curl -X POST http://localhost:8082/api/roles \
  -H "Content-Type: application/json" \
  -d '{"name":"ADMIN","description":"Administrator role"}'

curl -X POST http://localhost:8082/api/roles \
  -H "Content-Type: application/json" \
  -d '{"name":"USER","description":"Standard user role"}'

# Get all users
curl http://localhost:8082/api/users

# Get all roles
curl http://localhost:8082/api/roles
```

## Next Steps (Future Enhancements)

Saved in `backup_full_implementation/` folder:
- Authentication & JWT
- Password management with BCrypt
- Multi-Factor Authentication (MFA)
- Spring Security integration
- Advanced authorization
- Session management

## Notes
- All complex authentication features are backed up in `backup_full_implementation/` folder
- This demo shows basic CRUD functionality and data modeling
- Database will auto-create tables on first run (DDL auto-update)
- All IDs are UUIDs for better scalability

---

**Demo Date**: November 18, 2025  
**Progress**: 2 Days  
**Status**: Ready for Demo ✅

