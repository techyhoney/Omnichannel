# 🎯 Wallet Feature - Manager Demo Guide

## 📋 Feature Overview
**What's New:** User wallet system that automatically updates account balances when transactions are approved.

**Key Capabilities:**
- ✅ Every user has a wallet with balance tracking
- ✅ Money is debited from sender and credited to receiver when transaction is marked as SUCCESS
- ✅ Prevents transactions if insufficient balance
- ✅ Manual wallet operations (add/deduct money for testing)

---

## 🎬 Demo Flow (5 minutes)

### Step 1️⃣: Create Two Test Users (Sender & Receiver)

**API:** `POST /api/users`

**Create User 1 (Sender - Retail Customer):**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.sender@test.com",
  "phone": "9876543210",
  "userType": "RETAIL",
  "status": "ACTIVE",
  "kycStatus": "VERIFIED",
  "roleIds": []
}
```

**Create User 2 (Receiver - Merchant):**
```json
{
  "firstName": "Merchant",
  "lastName": "Store",
  "email": "merchant.store@test.com",
  "phone": "9876543211",
  "userType": "MERCHANT",
  "status": "ACTIVE",
  "kycStatus": "VERIFIED",
  "roleIds": []
}
```

**📝 Note:** Copy both `userId` values from responses - you'll need them!

---

### Step 2️⃣: Add Money to Sender's Wallet

**API:** `POST /api/wallet/{userId}/add`

**Request:** Replace `{userId}` with **Sender's userId** from Step 1

**Payload:**
```json
{
  "amount": 5000.00
}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Money added successfully",
  "data": {
    "userId": "...",
    "walletBalance": 5000.00,
    "transactionType": "CREDIT",
    "amount": 5000.00
  }
}
```

**🎤 Say to Manager:** "Now John has ₹5000 in his wallet"

---

### Step 3️⃣: Check Both Wallet Balances

**API:** `GET /api/wallet/{userId}/balance`

**Check Sender Balance:**
- Use Sender's `userId`
- **Expected:** `5000.00`

**Check Receiver Balance:**
- Use Receiver's `userId`
- **Expected:** `0.00` (no money yet)

**🎤 Say to Manager:** "Sender has ₹5000, Receiver has ₹0"

---

### Step 4️⃣: Create a Payment Type (if not already exists)

**API:** `POST /api/payment-types`

**Payload:**
```json
{
  "name": "UPI",
  "description": "Unified Payment Interface"
}
```

**📝 Note:** Copy `paymentTypeId` from response

---

### Step 5️⃣: Initiate Transaction (Sender pays Receiver ₹1500)

**API:** `POST /api/transactions`

**Payload:** Replace IDs with actual values from previous steps
```json
{
  "userId": "SENDER_USER_ID_HERE",
  "merchantId": "RECEIVER_USER_ID_HERE",
  "paymentTypeId": "PAYMENT_TYPE_ID_HERE",
  "amount": 1500.00
}
```

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Transaction created successfully",
  "data": {
    "transactionId": "...",
    "userId": "...",
    "merchantId": "...",
    "amount": 1500.00,
    "status": "PENDING",
    ...
  }
}
```

**📝 Note:** Copy `transactionId` from response

**🎤 Say to Manager:** "Transaction created for ₹1500, currently PENDING. Notice wallet balances haven't changed yet."

---

### Step 6️⃣: Approve Transaction (Mark as SUCCESS)

**API:** `PUT /api/transactions/{transactionId}/status?status=SUCCESS`

**Request:** Replace `{transactionId}` with value from Step 5

**✅ Expected Response:**
```json
{
  "success": true,
  "message": "Transaction status updated successfully to SUCCESS",
  "data": {
    "transactionId": "...",
    "status": "SUCCESS",
    ...
  }
}
```

**🎤 Say to Manager:** "Transaction approved! Now watch the wallet balances update automatically."

---

### Step 7️⃣: Verify Wallet Balances Updated ⭐ (KEY MOMENT)

**API:** `GET /api/wallet/{userId}/balance`

**Check Sender Balance:**
- Use Sender's `userId`
- **Expected:** `3500.00` (5000 - 1500)

**Check Receiver Balance:**
- Use Receiver's `userId`
- **Expected:** `1500.00` (0 + 1500)

**🎤 Say to Manager:** "Perfect! Money automatically moved: Sender now has ₹3500, Receiver has ₹1500"

---

### Step 8️⃣: BONUS - Test Insufficient Balance Protection

**API:** `POST /api/transactions`

**Try to send ₹5000 (more than sender's ₹3500 balance):**
```json
{
  "userId": "SENDER_USER_ID_HERE",
  "merchantId": "RECEIVER_USER_ID_HERE",
  "paymentTypeId": "PAYMENT_TYPE_ID_HERE",
  "amount": 5000.00
}
```

**✅ Expected Response:**
```json
{
  "success": false,
  "message": "Transaction not allowed: Insufficient wallet balance. Your balance: 3500.00, Required: 5000.00",
  "data": null,
  "timestamp": "..."
}
```

**🎤 Say to Manager:** "System prevents overdrafts - users can't spend more than they have!"

---

## 📊 Quick Summary for Manager

| Feature | Status |
|---------|--------|
| Automatic balance updates on transaction approval | ✅ Working |
| Debit from sender, credit to receiver | ✅ Working |
| Insufficient balance protection | ✅ Working |
| Manual wallet management (add/deduct) | ✅ Working |
| Real-time balance checking | ✅ Working |

---

## 🔧 Additional APIs (For Reference)

### Get All Users
**API:** `GET /api/users`
- Use this to see all users and their wallet balances

### Get Specific User Details
**API:** `GET /api/users/{userId}`
- Shows user info including `walletBalance`

### Deduct Money from Wallet (Admin operation)
**API:** `POST /api/wallet/{userId}/deduct`
```json
{
  "amount": 100.00
}
```

### Get Transaction History
**API:** `GET /api/transactions/history`
- Shows all transactions with amounts and statuses

---

## 💡 Pro Tips for Demo

1. **Keep it Simple:** Follow steps 1-7 exactly
2. **Pause at Step 7:** This is the "wow" moment - emphasize the automatic update
3. **Step 8 is Optional:** Only if manager asks about edge cases
4. **Have IDs Ready:** Write down userId values on paper during Step 1
5. **Clear Your Console:** Before demo, clear H2 database for clean slate

---

## 🚀 What This Enables

✅ Digital wallet for every user  
✅ Real-time money transfers  
✅ Automatic settlement on transaction approval  
✅ Fraud prevention (insufficient balance checks)  
✅ Audit trail (all balance changes tracked)  
✅ Foundation for future features (rewards, cashback, interest)

---

**Demo Time: ~5 minutes**  
**Complexity: Low** (just copy-paste payloads)  
**Impact: High** (shows end-to-end money flow)
