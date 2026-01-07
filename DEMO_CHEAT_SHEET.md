# 📝 Demo Cheat Sheet - Copy & Paste Ready

**Swagger:** http://localhost:8082/swagger-ui.html

---

## 🎯 DEMO SCRIPT (Copy-Paste These)

### 1️⃣ Create Customer
**POST** `/api/users`
```json
{"firstName":"Alice","lastName":"Customer","email":"alice@example.com","phone":"1111111111","userType":"RETAIL"}
```
**→ Copy `userId`**

---

### 2️⃣ Create Merchant
**POST** `/api/users`
```json
{"firstName":"Bob","lastName":"Merchant","email":"bob@merchant.com","phone":"2222222222","userType":"CORPORATE"}
```
**→ Copy `userId`**

---

### 3️⃣ Create Admin Role
**POST** `/api/roles`
```json
{"name":"ADMIN","description":"Administrator role"}
```
**→ Copy `roleId`**

---

### 4️⃣ Create Manager Role
**POST** `/api/roles`
```json
{"name":"MANAGER","description":"Manager role"}
```

---

### 5️⃣ Assign Role to User
**POST** `/api/roles/{roleId}/users/{userId}`  
→ Paste IDs in path, no body needed

---

### 6️⃣ Create NEFT Payment
**POST** `/api/payment-types`
```json
{"name":"NEFT","enabledChannels":"mobile,web,atm","isActive":true}
```
**→ Copy `paymentTypeId`**

---

### 7️⃣ Create UPI Payment
**POST** `/api/payment-types`
```json
{"name":"UPI","enabledChannels":"mobile,web","isActive":true}
```
**→ Copy `paymentTypeId`**

---

### 8️⃣ Create Transaction #1
**POST** `/api/transactions`
```json
{"userId":"customer-id","merchantId":"merchant-id","paymentTypeId":"payment-id","amount":5000.00,"currency":"INR"}
```
**→ Copy `transactionId`**

---

### 9️⃣ Mark Transaction SUCCESS
**PUT** `/api/transactions/{transactionId}/status?status=SUCCESS`

---

### 🔟 Create Transaction #2
**POST** `/api/transactions`
```json
{"userId":"customer-id","merchantId":"merchant-id","paymentTypeId":"payment-id","amount":10000.00,"currency":"INR"}
```

---

### 1️⃣1️⃣ Mark Transaction FAILED
**PUT** `/api/transactions/{transactionId}/status?status=FAILED`

---

### 1️⃣2️⃣ Create Transaction #3
**POST** `/api/transactions`
```json
{"userId":"customer-id","merchantId":"merchant-id","paymentTypeId":"payment-id","amount":3000.00,"currency":"INR"}
```
**PUT** `/api/transactions/{transactionId}/status?status=SUCCESS`

---

## 📊 SHOW REPORTS

### Overall Summary
**GET** `/api/reports/summary`

### Today's Report
**GET** `/api/reports/daily/today`

### Transaction Metrics
**GET** `/api/reports/metrics`

### Payment Analytics
**GET** `/api/reports/analytics/payment-types`

### User Report
**GET** `/api/reports/user/{customerId}`

### Transaction History
**GET** `/api/reports/history?page=0&size=10`

---

## 📋 VERIFY IN H2

http://localhost:8082/h2-console  
**JDBC:** `jdbc:h2:mem:omnidb`  
**User:** `sa`  
**Pass:** *(empty)*

```sql
-- Check everything
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM roles;
SELECT COUNT(*) FROM payment_types;
SELECT COUNT(*) FROM transactions;

-- View transactions
SELECT t.amount, t.status, u.first_name as customer, 
       m.first_name as merchant, pt.name as payment
FROM transactions t
JOIN users u ON t.user_id = u.user_id
JOIN users m ON t.merchant_id = m.user_id
JOIN payment_types pt ON t.payment_type_id = pt.payment_type_id;
```

---

## 🎤 TALKING POINTS

**User Management:**
- ✅ "Created retail customer and corporate merchant"
- ✅ "Role-based access control with ADMIN and MANAGER roles"
- ✅ "Can filter users by type and status"

**Payment Types:**
- ✅ "Configured multiple payment types: NEFT, UPI"
- ✅ "Can enable/disable payment channels"
- ✅ "Track which channels support each payment type"

**Transactions:**
- ✅ "Complete transaction lifecycle: INITIATED → PROCESSING → SUCCESS/FAILED"
- ✅ "Track customer, merchant, payment type, and amount"
- ✅ "Real-time status updates"

**Reports & Analytics:**
- ✅ "Daily, monthly, yearly reports"
- ✅ "User-specific transaction reports"
- ✅ "Payment type usage analytics"
- ✅ "Success/failure rate metrics"
- ✅ "Transaction history with advanced filters"

---

## ⚡ QUICK WINS TO SHOW

1. **Live API Testing** → "No Postman needed, test directly in browser"
2. **Data Validation** → Try creating user without email → See error
3. **Relationships** → Show user with assigned roles in response
4. **Filtering** → Filter by status, type, date range
5. **Pagination** → Show page 1, then page 2
6. **H2 Console** → "Real-time database inspection"
7. **Success Rates** → Show calculation in metrics
8. **Payment Analytics** → Show most-used payment type

---

## 🚨 TROUBLESHOOTING

**No data in reports?**  
→ Create at least 3 transactions first

**404 Error?**  
→ Check UUID is copied correctly

**Validation Error?**  
→ Check all required fields are present

**Application not running?**  
→ `./mvnw spring-boot:run`

---

## ✅ PRE-DEMO CHECKLIST

- [ ] Application running
- [ ] Swagger UI accessible
- [ ] H2 Console accessible  
- [ ] This cheat sheet open
- [ ] Browser zoom at 100%
- [ ] Clear any old data (restart app)

---

**Time:** ~5-7 minutes for full demo  
**Focus:** Show breadth of features, not depth  
**Energy:** Be confident, it's working code! 🚀

