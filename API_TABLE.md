# 📊 API Endpoints - Quick Lookup Table

## USER APIs
| Method | Endpoint | Body Example | Returns |
|--------|----------|--------------|---------|
| POST | `/api/users` | `{"firstName":"John","lastName":"Doe","email":"john@ex.com","phone":"1234567890","userType":"RETAIL"}` | User + userId |
| GET | `/api/users` | - | List of all users |
| GET | `/api/users/{userId}` | - | Single user |
| PUT | `/api/users/{userId}` | `{"firstName":"John","lastName":"Doe","email":"john@ex.com","phone":"9999","userType":"CORPORATE"}` | Updated user |
| DELETE | `/api/users/{userId}` | - | Success message |
| GET | `/api/users/type/{userType}` | - | Users of type |
| GET | `/api/users/status/{status}` | - | Users by status |

## ROLE APIs
| Method | Endpoint | Body Example | Returns |
|--------|----------|--------------|---------|
| POST | `/api/roles` | `{"name":"ADMIN","description":"Admin role"}` | Role + roleId |
| GET | `/api/roles` | - | List of all roles |
| GET | `/api/roles/{roleId}` | - | Single role |
| PUT | `/api/roles/{roleId}` | `{"name":"ADMIN","description":"Updated"}` | Updated role |
| DELETE | `/api/roles/{roleId}` | - | Success message |
| POST | `/api/roles/{roleId}/users/{userId}` | - | Success message |
| DELETE | `/api/roles/{roleId}/users/{userId}` | - | Success message |

## PAYMENT TYPE APIs
| Method | Endpoint | Body Example | Returns |
|--------|----------|--------------|---------|
| POST | `/api/payment-types` | `{"name":"NEFT","enabledChannels":"mobile,web","isActive":true}` | PaymentType + id |
| GET | `/api/payment-types` | - | List of all types |
| GET | `/api/payment-types/{id}` | - | Single type |
| PUT | `/api/payment-types/{id}` | `{"name":"NEFT","enabledChannels":"mobile","isActive":true}` | Updated type |
| DELETE | `/api/payment-types/{id}` | - | Success message |
| GET | `/api/payment-types/active` | - | Active types only |

## TRANSACTION APIs
| Method | Endpoint | Body Example | Returns |
|--------|----------|--------------|---------|
| POST | `/api/transactions` | `{"userId":"uuid","merchantId":"uuid","paymentTypeId":"uuid","amount":5000,"currency":"INR"}` | Transaction + id |
| GET | `/api/transactions/{id}` | - | Single transaction |
| PUT | `/api/transactions/{id}/status?status=SUCCESS` | - | Updated transaction |

## TRANSACTION LIMIT APIs
| Method | Endpoint | Body Example | Returns |
|--------|----------|--------------|---------|
| POST | `/api/transaction-limits` | `{"userType":"RETAIL","paymentTypeId":"uuid","limitType":"DAILY","limitAmount":50000,"isActive":true}` | Limit + id |
| GET | `/api/transaction-limits` | - | All limits |
| GET | `/api/transaction-limits/{id}` | - | Single limit |
| PUT | `/api/transaction-limits/{id}` | Same as POST | Updated limit |
| DELETE | `/api/transaction-limits/{id}` | - | Success message |

## REPORTING APIs - Summary & History
| Method | Endpoint | Parameters | Returns |
|--------|----------|------------|---------|
| GET | `/api/reports/summary` | - | Overall summary |
| GET | `/api/reports/history` | `?page=0&size=10&status=SUCCESS&startDate=2026-01-01` | Paginated history |
| GET | `/api/reports/history/all` | - | All transactions |

## REPORTING APIs - Daily
| Method | Endpoint | Parameters | Returns |
|--------|----------|------------|---------|
| GET | `/api/reports/daily` | `?date=2026-01-06` | Report for date |
| GET | `/api/reports/daily/today` | - | Today's report |
| GET | `/api/reports/daily/range` | `?startDate=2026-01-01&endDate=2026-01-10` | Range reports |
| GET | `/api/reports/daily/last7days` | - | Last 7 days |
| GET | `/api/reports/daily/last30days` | - | Last 30 days |

## REPORTING APIs - Monthly/Yearly
| Method | Endpoint | Parameters | Returns |
|--------|----------|------------|---------|
| GET | `/api/reports/monthly` | `?year=2026&month=1` | Month report |
| GET | `/api/reports/monthly/current` | - | Current month |
| GET | `/api/reports/monthly/year/{year}` | - | All months |
| GET | `/api/reports/yearly/{year}` | - | Year + breakdown |
| GET | `/api/reports/yearly/current` | - | Current year |

## REPORTING APIs - User & Analytics
| Method | Endpoint | Parameters | Returns |
|--------|----------|------------|---------|
| GET | `/api/reports/user/{userId}` | - | User report |
| GET | `/api/reports/user/{userId}/range` | `?startDate=2026-01-01&endDate=2026-01-31` | User range report |
| GET | `/api/reports/analytics/payment-types` | - | All payment analytics |
| GET | `/api/reports/analytics/payment-types/{id}` | - | Single type analytics |
| GET | `/api/reports/metrics` | - | Overall metrics |
| GET | `/api/reports/metrics/range` | `?startDate=2026-01-01&endDate=2026-01-31` | Range metrics |

## ENUM VALUES

### UserType
- `RETAIL`
- `CORPORATE`
- `ADMIN`

### UserStatus
- `ACTIVE`
- `INACTIVE`
- `LOCKED`

### TransactionStatus
- `INITIATED`
- `PROCESSING`
- `SUCCESS`
- `FAILED`

### LimitType
- `DAILY`
- `PER_TRANSACTION`
- `MONTHLY`

## DATE FORMAT
Always use: `yyyy-MM-dd` (e.g., `2026-01-06`)

## RESPONSE FORMAT
```json
{
  "success": true/false,
  "message": "Description",
  "data": { /* result */ },
  "timestamp": "2026-01-06T12:00:00"
}
```

## ACCESS URLS
- **Swagger:** http://localhost:8082/swagger-ui.html
- **H2 Console:** http://localhost:8082/h2-console
- **App:** http://localhost:8082

