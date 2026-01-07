# 🚀 API Quick Reference - Demo Guide

**Access Swagger:** http://localhost:8082/swagger-ui.html

---

## 👤 USER APIs

### Create User
**POST** `/api/users`
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "userType": "RETAIL"
}
```
**Response:** User object with `userId` (copy this!)

### Get All Users
**GET** `/api/users` → No payload needed

### Get User by ID
**GET** `/api/users/{userId}` → Paste userId in path

### Update User
**PUT** `/api/users/{userId}`
```json
{
  "firstName": "John Updated",
  "lastName": "Doe",
  "email": "john.new@example.com",
  "phone": "9999999999",
  "userType": "CORPORATE"
}
```

### Filter Users
**GET** `/api/users/type/RETAIL` → Get retail users  
**GET** `/api/users/status/ACTIVE` → Get active users

---

## 🔐 ROLE APIs

### Create Role
**POST** `/api/roles`
```json
{
  "name": "ADMIN",
  "description": "Administrator role"
}
```
**Response:** Role object with `roleId` (copy this!)

### Get All Roles
**GET** `/api/roles` → No payload needed

### Assign Role to User
**POST** `/api/roles/{roleId}/users/{userId}`  
→ No payload, just put IDs in path

### Remove Role from User
**DELETE** `/api/roles/{roleId}/users/{userId}`

---

## 💳 PAYMENT TYPE APIs

### Create Payment Type
**POST** `/api/payment-types`
```json
{
  "name": "NEFT",
  "enabledChannels": "mobile,web,atm",
  "isActive": true
}
```
**Response:** Payment type with `paymentTypeId` (copy this!)

### Get All Payment Types
**GET** `/api/payment-types`

### Get Active Payment Types
**GET** `/api/payment-types/active`

### Update Payment Type
**PUT** `/api/payment-types/{id}`
```json
{
  "name": "NEFT Updated",
  "enabledChannels": "mobile,web",
  "isActive": true
}
```

---

## 💰 TRANSACTION APIs

### Create Transaction
**POST** `/api/transactions`
```json
{
  "userId": "user-uuid-here",
  "merchantId": "merchant-uuid-here",
  "paymentTypeId": "payment-type-uuid-here",
  "amount": 5000.00,
  "currency": "INR"
}
```
**Response:** Transaction with `transactionId` (copy this!)  
**Status:** INITIATED

### Get Transaction
**GET** `/api/transactions/{id}`

### Update Transaction Status
**PUT** `/api/transactions/{id}/status?status=SUCCESS`  
**Status values:** INITIATED | PROCESSING | SUCCESS | FAILED

---

## 💵 TRANSACTION LIMIT APIs

### Create Limit
**POST** `/api/transaction-limits`
```json
{
  "userType": "RETAIL",
  "paymentTypeId": "payment-type-uuid",
  "limitType": "DAILY",
  "limitAmount": 50000.00,
  "isActive": true
}
```

### Get All Limits
**GET** `/api/transaction-limits`

---

## 📊 REPORTING & ANALYTICS APIs

### Overall Summary
**GET** `/api/reports/summary`  
→ Total transactions, success/fail counts, amounts

### Today's Report
**GET** `/api/reports/daily/today`  
→ Today's transaction summary

### Last 7 Days
**GET** `/api/reports/daily/last7days`  
→ Daily reports for last week

### Last 30 Days
**GET** `/api/reports/daily/last30days`  
→ Daily reports for last month

### Current Month Report
**GET** `/api/reports/monthly/current`  
→ This month's summary

### Current Year Report
**GET** `/api/reports/yearly/current`  
→ This year with monthly breakdown

### User Report
**GET** `/api/reports/user/{userId}`  
→ User's complete transaction report

### Payment Type Analytics
**GET** `/api/reports/analytics/payment-types`  
→ Usage stats for all payment types

### Transaction Metrics
**GET** `/api/reports/metrics`  
→ Success rates, amounts, status breakdown

### Transaction History (Filtered)
**GET** `/api/reports/history?page=0&size=10`  
**Optional filters:**
- `userId=uuid`
- `status=SUCCESS`
- `startDate=2026-01-01`
- `endDate=2026-01-31`
- `minAmount=1000`
- `maxAmount=10000`

### Date Range Reports
**GET** `/api/reports/daily/range?startDate=2026-01-01&endDate=2026-01-10`  
**GET** `/api/reports/metrics/range?startDate=2026-01-01&endDate=2026-01-31`

---

## 🎬 DEMO FLOW (5 minutes)

### 1. Create Base Data (2 min)
```
1. POST /api/users → Create "Customer" user (RETAIL)
2. POST /api/users → Create "Merchant" user (CORPORATE)
3. POST /api/roles → Create "ADMIN" role
4. POST /api/roles/{roleId}/users/{userId} → Assign role
5. POST /api/payment-types → Create "NEFT" payment type
6. POST /api/payment-types → Create "UPI" payment type
```

### 2. Create Transactions (1 min)
```
7. POST /api/transactions → Amount: 5000, Status: INITIATED
8. PUT /api/transactions/{id}/status?status=SUCCESS
9. POST /api/transactions → Amount: 10000, Status: INITIATED
10. PUT /api/transactions/{id}/status?status=FAILED
11. POST /api/transactions → Amount: 3000, Status: INITIATED
12. PUT /api/transactions/{id}/status?status=SUCCESS
```

### 3. Show Reports (2 min)
```
13. GET /api/reports/summary → Show overall stats
14. GET /api/reports/daily/today → Today's report
15. GET /api/reports/metrics → Success/failure metrics
16. GET /api/reports/analytics/payment-types → Payment type usage
17. GET /api/reports/user/{userId} → User specific report
```

---

## 💡 SWAGGER TIPS

### How to Test in Swagger:

1. **Expand endpoint** → Click on the API row
2. **Click "Try it out"** → Enables input fields
3. **Enter data** → Modify JSON or enter parameters
4. **Click "Execute"** → Sends request
5. **View Response** → See result below

### Copy UUIDs:
- After creating User/Role/Payment Type
- Look for `userId`, `roleId`, `paymentTypeId` in response
- Copy and use in other APIs

### Date Format:
- Always use: `yyyy-MM-dd`
- Example: `2026-01-06`

---

## 🎯 KEY RESPONSES

### Success Response Format
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* your data */ },
  "timestamp": "2026-01-06T12:00:00"
}
```

### Error Response Format
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2026-01-06T12:00:00"
}
```

---

## 📋 QUICK CHECKLIST

### Before Demo:
- [ ] Start application: `./mvnw spring-boot:run`
- [ ] Open Swagger: http://localhost:8082/swagger-ui.html
- [ ] H2 Console ready: http://localhost:8082/h2-console

### During Demo:
- [ ] Create 2 users (Customer + Merchant)
- [ ] Create 1-2 roles
- [ ] Create 2-3 payment types
- [ ] Create 3-5 transactions (mix of SUCCESS/FAILED)
- [ ] Show reports and analytics

### What to Highlight:
✅ Complete CRUD operations  
✅ Relationships (User-Role, Transaction-PaymentType)  
✅ Filtering capabilities  
✅ Comprehensive reporting  
✅ Real-time analytics  
✅ Professional API documentation  

---

## 🔢 HTTP STATUS CODES

- **200** → Success (GET, PUT)
- **201** → Created (POST)
- **400** → Bad Request (validation error)
- **404** → Not Found
- **500** → Server Error

---

**Swagger UI:** http://localhost:8082/swagger-ui.html  
**H2 Console:** http://localhost:8082/h2-console  
**App Port:** 8082

