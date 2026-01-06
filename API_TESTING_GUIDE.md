# 🚀 API Testing Guide - Omnichannel Banking Platform

## 📋 Table of Contents
1. [Quick Start](#quick-start)
2. [Accessing Swagger UI](#accessing-swagger-ui)
3. [Accessing H2 Database Console](#accessing-h2-database-console)
4. [Testing APIs with Swagger](#testing-apis-with-swagger)
5. [Sample API Workflows](#sample-api-workflows)

---

## 🎯 Quick Start

### 1. Start the Application

```bash
# Using Maven Wrapper (Recommended)
./mvnw spring-boot:run

# OR using Maven directly
mvn spring-boot:run
```

**Application will start on:** `http://localhost:8082`

### 2. Wait for Startup
Look for this message in the console:
```
Started OmnichannelProjectApplication in X.XXX seconds
```

---

## 🌐 Accessing Swagger UI

### Swagger UI URLs

| Resource | URL |
|----------|-----|
| **Swagger UI (Interactive)** | http://localhost:8082/swagger-ui.html |
| **API Docs (JSON)** | http://localhost:8082/api-docs |
| **Alternative Swagger URL** | http://localhost:8082/swagger-ui/index.html |

### What is Swagger UI?

Swagger UI provides an **interactive interface** to:
- ✅ View all available API endpoints
- ✅ See request/response schemas
- ✅ Test APIs directly from the browser
- ✅ No need for Postman or curl commands

### Swagger UI Features

1. **Explore Endpoints**: All APIs are grouped by controllers
2. **Try It Out**: Click "Try it out" to test any endpoint
3. **Request Bodies**: Auto-generated sample JSON for POST/PUT requests
4. **Response Codes**: See all possible HTTP response codes
5. **Models**: View all DTOs and data structures

---

## 🗄️ Accessing H2 Database Console

### H2 Console URL
**Direct Access:** http://localhost:8082/h2-console

### H2 Console Login Credentials

| Field | Value |
|-------|-------|
| **Driver Class** | `org.h2.Driver` |
| **JDBC URL** | `jdbc:h2:mem:omnidb` |
| **Username** | `sa` |
| **Password** | *(leave empty)* |

### H2 Console Features

1. **View Tables**: See all created tables (users, roles, transactions, etc.)
2. **Run SQL Queries**: Execute any SQL directly
3. **Data Inspection**: Check inserted data in real-time
4. **Schema Exploration**: Understand table structures

### Sample SQL Queries for H2

```sql
-- View all users
SELECT * FROM users;

-- View all roles
SELECT * FROM roles;

-- View all payment types
SELECT * FROM payment_types;

-- View all transactions
SELECT * FROM transactions;

-- View user-role mappings
SELECT * FROM user_roles;

-- Count records
SELECT COUNT(*) FROM users;

-- Join query - Users with their roles
SELECT u.first_name, u.last_name, r.name as role_name
FROM users u
JOIN user_roles ur ON u.user_id = ur.user_id
JOIN roles r ON ur.role_id = r.role_id;
```

---

## 🧪 Testing APIs with Swagger

### Step-by-Step Guide

#### 1️⃣ Open Swagger UI
Navigate to: http://localhost:8082/swagger-ui.html

#### 2️⃣ Expand API Group
Click on any controller (e.g., **user-controller**)

#### 3️⃣ Select an Endpoint
Click on the endpoint you want to test (e.g., `POST /api/users`)

#### 4️⃣ Click "Try it out"
This enables the input fields

#### 5️⃣ Enter Request Data
Modify the sample JSON or enter your own data

#### 6️⃣ Click "Execute"
The API will be called and you'll see the response

#### 7️⃣ View Response
Check the response body, status code, and headers

---

## 📝 Sample API Workflows

### Workflow 1: Complete User Management

```
1. Create a User (POST /api/users)
   ↓
2. Get All Users (GET /api/users)
   ↓
3. Get User by ID (GET /api/users/{userId})
   ↓
4. Update User (PUT /api/users/{userId})
   ↓
5. Delete User (DELETE /api/users/{userId})
```

#### 1. Create a User via Swagger

**Endpoint:** `POST /api/users`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "userType": "RETAIL"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "userType": "RETAIL",
    "status": "ACTIVE",
    "kycStatus": "PENDING",
    "roles": [],
    "createdAt": "2025-01-05T10:30:00",
    "updatedAt": "2025-01-05T10:30:00"
  },
  "timestamp": "2025-01-05T10:30:00"
}
```

#### 2. Get All Users

**Endpoint:** `GET /api/users`

No request body needed - just click "Execute"

---

### Workflow 2: Role Management

```
1. Create Role (POST /api/roles)
   ↓
2. Create User (POST /api/users)
   ↓
3. Assign Role to User (POST /api/roles/{roleId}/users/{userId})
   ↓
4. Verify in H2 Console
```

#### 1. Create a Role

**Endpoint:** `POST /api/roles`

**Request Body:**
```json
{
  "name": "MANAGER",
  "description": "Manager role with elevated permissions"
}
```

#### 2. Assign Role to User

**Endpoint:** `POST /api/roles/{roleId}/users/{userId}`

**Parameters:**
- `roleId`: (UUID from step 1)
- `userId`: (UUID from user creation)

---

### Workflow 3: Payment Type Configuration

#### Create Payment Type

**Endpoint:** `POST /api/payment-types`

**Request Body:**
```json
{
  "name": "NEFT",
  "description": "National Electronic Funds Transfer",
  "isActive": true,
  "minAmount": 1.00,
  "maxAmount": 1000000.00
}
```

---

### Workflow 4: Transaction Processing

```
1. Create User (Customer)
   ↓
2. Create User (Merchant)
   ↓
3. Create Payment Type
   ↓
4. Initiate Transaction
   ↓
5. Check Transaction Status
   ↓
6. Update Transaction Status
```

#### Initiate Transaction

**Endpoint:** `POST /api/transactions`

**Request Body:**
```json
{
  "userId": "user-uuid-here",
  "merchantId": "merchant-uuid-here",
  "paymentTypeId": "payment-type-uuid-here",
  "amount": 1500.00,
  "currency": "INR"
}
```

#### Update Transaction Status

**Endpoint:** `PUT /api/transactions/{id}/status`

**Query Parameter:**
- `status`: COMPLETED | FAILED | PENDING

---

## 🔍 Verifying Data in H2 Console

After creating data via Swagger:

1. Open H2 Console: http://localhost:8082/h2-console
2. Login with credentials
3. Run queries to verify:

```sql
-- Check latest user
SELECT * FROM users ORDER BY created_at DESC LIMIT 5;

-- Check user with roles
SELECT u.first_name, u.last_name, r.name as role 
FROM users u 
LEFT JOIN user_roles ur ON u.user_id = ur.user_id 
LEFT JOIN roles r ON ur.role_id = r.role_id;

-- Check transactions
SELECT t.transaction_id, t.amount, t.status, 
       u1.first_name as customer, u2.first_name as merchant
FROM transactions t
JOIN users u1 ON t.user_id = u1.user_id
JOIN users u2 ON t.merchant_id = u2.user_id;
```

---

## 🎨 Swagger UI Tips

### 1. **Use "Authorize" Button** (When Security is Added)
- Click the lock icon or "Authorize" button
- Enter JWT token or credentials
- All subsequent requests will include authentication

### 2. **Copy curl Commands**
- Each request shows a curl command
- Copy it to use in terminal or scripts

### 3. **Download API Specification**
- Click on `/api-docs` link
- Save the JSON file
- Import into Postman or other tools

### 4. **Explore Models/Schemas**
- Scroll to bottom of Swagger UI
- Click "Schemas" section
- View all request/response models

---

## 🔄 Switching Between H2 and MySQL

### Current Setup: H2 (In-Memory)
- **Pros**: Easy setup, no installation needed
- **Cons**: Data is lost on application restart

### To Switch to MySQL:

1. **Edit `application.properties`:**
   - Comment out H2 configuration
   - Uncomment MySQL configuration

2. **Ensure MySQL is running:**
   ```bash
   mysql -u root -p
   CREATE DATABASE omni_db;
   ```

3. **Restart the application**

---

## 📊 API Endpoint Summary

| Module | Endpoints | Base Path |
|--------|-----------|-----------|
| **Users** | 7 endpoints | `/api/users` |
| **Roles** | 7 endpoints | `/api/roles` |
| **Payment Types** | 5 endpoints | `/api/payment-types` |
| **Transactions** | 3 endpoints | `/api/transactions` |
| **Transaction Limits** | 5 endpoints | `/api/transaction-limits` |

---

## 🐛 Troubleshooting

### Issue: Swagger UI Not Loading
**Solution:**
- Check application is running on port 8082
- Try alternative URL: http://localhost:8082/swagger-ui/index.html
- Clear browser cache

### Issue: H2 Console Login Fails
**Solution:**
- Verify JDBC URL is exactly: `jdbc:h2:mem:omnidb`
- Username: `sa`
- Password: (leave blank)
- Ensure application is running

### Issue: No Data in H2 Console
**Solution:**
- H2 is in-memory - data is cleared on restart
- Use Swagger to create test data
- Check table names are correct (lowercase)

### Issue: API Returns 404
**Solution:**
- Check the base path includes `/api/`
- Verify endpoint path in Swagger UI
- Ensure application started successfully

---

## 🎓 Learning Resources

### Understanding HTTP Status Codes
- `200 OK` - Successful GET/PUT request
- `201 Created` - Successful POST request
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Validation error
- `404 Not Found` - Resource doesn't exist
- `500 Internal Server Error` - Server error

### Sample Test Scenarios

1. **Happy Path**: Create → Read → Update → Delete
2. **Validation Test**: Send invalid data (missing fields)
3. **Not Found Test**: Use non-existent UUID
4. **Duplicate Test**: Create same resource twice
5. **Filter Test**: Use query parameters to filter results

---

## 📞 Quick Reference

| What | Where |
|------|-------|
| **Application** | http://localhost:8082 |
| **Swagger UI** | http://localhost:8082/swagger-ui.html |
| **H2 Console** | http://localhost:8082/h2-console |
| **API Docs JSON** | http://localhost:8082/api-docs |
| **Health Check** | http://localhost:8082/actuator/health (if actuator added) |

---

## ✅ Next Steps

1. ✅ Start the application
2. ✅ Access Swagger UI
3. ✅ Create test data using Swagger
4. ✅ Verify data in H2 Console
5. ✅ Test all CRUD operations
6. ✅ Export Postman collection from Swagger
7. ✅ Share API documentation with team

---

**Happy Testing! 🎉**

For more information, refer to:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Swagger/OpenAPI Documentation](https://swagger.io/docs/)
- [H2 Database Documentation](https://www.h2database.com/)

