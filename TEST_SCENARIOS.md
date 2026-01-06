# 🧪 Test Scenarios - Complete API Testing Checklist

## 📋 Test Scenario Checklist

### ✅ Scenario 1: User Management Flow (5 min)

#### Test Steps:
- [ ] **1.1** Start application: `./mvnw spring-boot:run`
- [ ] **1.2** Open Swagger: http://localhost:8082/swagger-ui.html
- [ ] **1.3** Create User (POST /api/users)
  ```json
  {
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice@example.com",
    "phone": "9876543210",
    "userType": "RETAIL"
  }
  ```
- [ ] **1.4** Copy `userId` from response
- [ ] **1.5** Get All Users (GET /api/users)
- [ ] **1.6** Get User by ID (GET /api/users/{userId})
- [ ] **1.7** Update User (PUT /api/users/{userId})
- [ ] **1.8** Verify in H2: `SELECT * FROM users;`

**Expected Results:**
✅ User created with status "ACTIVE"  
✅ KYC status is "PENDING"  
✅ User appears in GET all users  
✅ Update reflects immediately  

---

### ✅ Scenario 2: Role Management & Assignment (5 min)

#### Test Steps:
- [ ] **2.1** Create Admin Role (POST /api/roles)
  ```json
  {
    "name": "ADMIN",
    "description": "Administrator with full access"
  }
  ```
- [ ] **2.2** Create Manager Role (POST /api/roles)
  ```json
  {
    "name": "MANAGER",
    "description": "Manager role"
  }
  ```
- [ ] **2.3** Get All Roles (GET /api/roles)
- [ ] **2.4** Assign Role to User (POST /api/roles/{roleId}/users/{userId})
- [ ] **2.5** Get User - verify role is assigned (GET /api/users/{userId})
- [ ] **2.6** Verify in H2:
  ```sql
  SELECT u.first_name, r.name as role
  FROM users u
  JOIN user_roles ur ON u.user_id = ur.user_id
  JOIN roles r ON ur.role_id = r.role_id;
  ```

**Expected Results:**
✅ Roles created successfully  
✅ Role assigned to user  
✅ User response includes role name  
✅ Database shows correct mapping  

---

### ✅ Scenario 3: Payment Type Configuration (3 min)

#### Test Steps:
- [ ] **3.1** Create NEFT Payment Type (POST /api/payment-types)
  ```json
  {
    "name": "NEFT",
    "description": "National Electronic Funds Transfer",
    "isActive": true,
    "minAmount": 1.00,
    "maxAmount": 1000000.00
  }
  ```
- [ ] **3.2** Create RTGS Payment Type (POST /api/payment-types)
  ```json
  {
    "name": "RTGS",
    "description": "Real Time Gross Settlement",
    "isActive": true,
    "minAmount": 200000.00,
    "maxAmount": 10000000.00
  }
  ```
- [ ] **3.3** Create UPI Payment Type (POST /api/payment-types)
  ```json
  {
    "name": "UPI",
    "description": "Unified Payments Interface",
    "isActive": true,
    "minAmount": 1.00,
    "maxAmount": 100000.00
  }
  ```
- [ ] **3.4** Get All Payment Types (GET /api/payment-types)
- [ ] **3.5** Verify in H2: `SELECT * FROM payment_types;`

**Expected Results:**
✅ All payment types created  
✅ Min/Max amounts set correctly  
✅ All marked as active  

---

### ✅ Scenario 4: Complete Transaction Flow (7 min)

#### Prerequisites:
- Create 2 users (Customer & Merchant)
- Create at least 1 payment type

#### Test Steps:
- [ ] **4.1** Create Customer User
  ```json
  {
    "firstName": "Bob",
    "lastName": "Customer",
    "email": "bob@customer.com",
    "phone": "1111111111",
    "userType": "RETAIL"
  }
  ```
- [ ] **4.2** Create Merchant User
  ```json
  {
    "firstName": "Shop",
    "lastName": "Owner",
    "email": "shop@merchant.com",
    "phone": "2222222222",
    "userType": "CORPORATE"
  }
  ```
- [ ] **4.3** Create Payment Type (NEFT)
- [ ] **4.4** Initiate Transaction (POST /api/transactions)
  ```json
  {
    "userId": "customer-uuid",
    "merchantId": "merchant-uuid",
    "paymentTypeId": "payment-type-uuid",
    "amount": 5000.00,
    "currency": "INR"
  }
  ```
- [ ] **4.5** Copy `transactionId` from response
- [ ] **4.6** Get Transaction (GET /api/transactions/{id})
- [ ] **4.7** Update Status to COMPLETED (PUT /api/transactions/{id}/status?status=COMPLETED)
- [ ] **4.8** Verify in H2:
  ```sql
  SELECT t.transaction_id, t.amount, t.status,
         u1.first_name as customer,
         u2.first_name as merchant,
         pt.name as payment_type
  FROM transactions t
  JOIN users u1 ON t.user_id = u1.user_id
  JOIN users u2 ON t.merchant_id = u2.user_id
  JOIN payment_types pt ON t.payment_type_id = pt.payment_type_id;
  ```

**Expected Results:**
✅ Transaction created with status "INITIATED"  
✅ Status updates to "COMPLETED"  
✅ Customer and merchant linked correctly  
✅ Payment type association correct  

---

### ✅ Scenario 5: Transaction Limits Configuration (3 min)

#### Test Steps:
- [ ] **5.1** Create Daily Limit (POST /api/transaction-limits)
  ```json
  {
    "userType": "RETAIL",
    "paymentTypeId": "payment-type-uuid",
    "limitType": "DAILY",
    "limitAmount": 50000.00,
    "isActive": true
  }
  ```
- [ ] **5.2** Create Per-Transaction Limit (POST /api/transaction-limits)
  ```json
  {
    "userType": "RETAIL",
    "paymentTypeId": "payment-type-uuid",
    "limitType": "PER_TRANSACTION",
    "limitAmount": 25000.00,
    "isActive": true
  }
  ```
- [ ] **5.3** Get All Limits (GET /api/transaction-limits)
- [ ] **5.4** Verify in H2: `SELECT * FROM transaction_limits;`

**Expected Results:**
✅ Limits created successfully  
✅ Associated with user type  
✅ Associated with payment type  
✅ All marked as active  

---

### ✅ Scenario 6: Filter & Search Operations (3 min)

#### Test Steps:
- [ ] **6.1** Get Users by Type - RETAIL (GET /api/users/type/RETAIL)
- [ ] **6.2** Get Users by Type - CORPORATE (GET /api/users/type/CORPORATE)
- [ ] **6.3** Get Users by Status - ACTIVE (GET /api/users/status/ACTIVE)
- [ ] **6.4** Get Specific Role (GET /api/roles/{roleId})
- [ ] **6.5** Get Specific Payment Type (GET /api/payment-types/{id})

**Expected Results:**
✅ Filter returns only matching users  
✅ User types correctly filtered  
✅ Status filters working  

---

### ✅ Scenario 7: Validation Testing (5 min)

Test that validation works correctly:

#### Test Steps:
- [ ] **7.1** Create User with Missing Email
  ```json
  {
    "firstName": "Test",
    "lastName": "User",
    "phone": "1234567890",
    "userType": "RETAIL"
  }
  ```
  **Expected:** ❌ 400 Bad Request

- [ ] **7.2** Create User with Invalid Email
  ```json
  {
    "firstName": "Test",
    "lastName": "User",
    "email": "invalid-email",
    "userType": "RETAIL"
  }
  ```
  **Expected:** ❌ 400 Bad Request

- [ ] **7.3** Get Non-existent User
  ```
  GET /api/users/00000000-0000-0000-0000-000000000000
  ```
  **Expected:** ❌ 404 Not Found

- [ ] **7.4** Create Duplicate Email
  - Create user with email "test@example.com"
  - Try creating another user with same email
  **Expected:** ❌ 409 Conflict or 400 Bad Request

**Expected Results:**
✅ Missing required fields rejected  
✅ Invalid data rejected  
✅ Non-existent resources return 404  
✅ Duplicates prevented  

---

### ✅ Scenario 8: Update & Delete Operations (4 min)

#### Test Steps:
- [ ] **8.1** Create a test user
- [ ] **8.2** Update user details (PUT /api/users/{userId})
  ```json
  {
    "firstName": "Updated",
    "lastName": "Name",
    "email": "updated@example.com",
    "phone": "9999999999",
    "userType": "CORPORATE"
  }
  ```
- [ ] **8.3** Verify update (GET /api/users/{userId})
- [ ] **8.4** Remove role from user (DELETE /api/roles/{roleId}/users/{userId})
- [ ] **8.5** Delete user (DELETE /api/users/{userId})
- [ ] **8.6** Verify deletion (GET /api/users/{userId})
  **Expected:** ❌ 404 Not Found
- [ ] **8.7** Check H2: User should be deleted

**Expected Results:**
✅ Update reflects immediately  
✅ Role removed successfully  
✅ User deleted from database  
✅ Deleted user returns 404  

---

### ✅ Scenario 9: Complex Query Testing (3 min)

#### H2 Console Queries:

- [ ] **9.1** Users with their roles:
  ```sql
  SELECT u.first_name, u.last_name, u.user_type,
         STRING_AGG(r.name, ', ') as roles
  FROM users u
  LEFT JOIN user_roles ur ON u.user_id = ur.user_id
  LEFT JOIN roles r ON ur.role_id = r.role_id
  GROUP BY u.user_id, u.first_name, u.last_name, u.user_type;
  ```

- [ ] **9.2** Transaction summary:
  ```sql
  SELECT 
    COUNT(*) as total_transactions,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount,
    status
  FROM transactions
  GROUP BY status;
  ```

- [ ] **9.3** Users without roles:
  ```sql
  SELECT u.first_name, u.last_name, u.email
  FROM users u
  LEFT JOIN user_roles ur ON u.user_id = ur.user_id
  WHERE ur.id IS NULL;
  ```

- [ ] **9.4** Active payment types count:
  ```sql
  SELECT is_active, COUNT(*) as count
  FROM payment_types
  GROUP BY is_active;
  ```

**Expected Results:**
✅ Queries execute successfully  
✅ Data relationships correct  
✅ Aggregations accurate  

---

### ✅ Scenario 10: End-to-End Business Flow (10 min)

Complete business workflow:

#### Story:
*"A new customer registers, gets verified, assigned role, and makes a payment"*

#### Test Steps:
- [ ] **10.1** Create Customer
- [ ] **10.2** Assign "CUSTOMER" role
- [ ] **10.3** Update KYC status to "VERIFIED" (via database for now)
  ```sql
  UPDATE users SET kyc_status = 'VERIFIED' WHERE email = 'customer@example.com';
  ```
- [ ] **10.4** Create Merchant
- [ ] **10.5** Assign "MERCHANT" role
- [ ] **10.6** Create Payment Type (UPI)
- [ ] **10.7** Set Transaction Limits
- [ ] **10.8** Initiate Transaction
- [ ] **10.9** Check transaction status
- [ ] **10.10** Complete transaction
- [ ] **10.11** Verify final state in H2

**Expected Results:**
✅ Complete flow executes smoothly  
✅ All relationships maintained  
✅ Data consistency preserved  
✅ Business rules followed  

---

## 🎯 Demo Checklist for Manager

### Quick Demo (5 minutes):
- [ ] Show Swagger UI interface
- [ ] Create 1 user live
- [ ] Show H2 console with data
- [ ] Explain key features

### Detailed Demo (15 minutes):
- [ ] Run Scenario 1 (User Management)
- [ ] Run Scenario 2 (Role Assignment)
- [ ] Run Scenario 4 (Transaction Flow)
- [ ] Show H2 complex queries
- [ ] Explain architecture

---

## 📊 Expected Test Results Summary

| Scenario | Duration | APIs Tested | Expected Success Rate |
|----------|----------|-------------|----------------------|
| User Management | 5 min | 5 | 100% |
| Role Management | 5 min | 4 | 100% |
| Payment Types | 3 min | 3 | 100% |
| Transactions | 7 min | 3 | 100% |
| Limits | 3 min | 2 | 100% |
| Filters | 3 min | 5 | 100% |
| Validation | 5 min | 4 | 100% (all should fail correctly) |
| Updates | 4 min | 4 | 100% |
| Queries | 3 min | 4 SQL | 100% |
| End-to-End | 10 min | 10+ | 100% |

**Total Test Time:** ~45 minutes for complete testing

---

## 🐛 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| 404 on new endpoint | Restart application |
| Empty H2 tables | Create data via Swagger first |
| UUID format error | Use actual UUID from responses, not placeholders |
| Transaction validation error | Comment out validation for now (TODO in code) |
| Role not showing on user | Refresh or re-query user endpoint |

---

## ✅ Verification Commands

**Check Application Health:**
```bash
curl http://localhost:8082/swagger-ui.html
```

**Quick API Test:**
```bash
curl http://localhost:8082/api/users
```

**Check if H2 is running:**
```bash
curl http://localhost:8082/h2-console
```

---

**Test Status:** Ready for execution ✅

**Documentation:** Complete ✅

**Build Status:** Successful ✅

---

*Happy Testing! 🚀*

