package com.kpmg.omnichannel.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTransactionReportDTO {
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userType;
    
    // Transaction Summary
    private Long totalTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    private BigDecimal totalAmount;
    private BigDecimal successfulAmount;
    private Double successRate;
    
    // Most used payment types
    private List<PaymentTypeUsageDTO> paymentTypeUsage;
    
    // Recent transactions
    private List<TransactionHistoryDTO> recentTransactions;
}

