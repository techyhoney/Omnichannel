package com.kpmg.omnichannel.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryDTO {
    private Long totalTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    private Long pendingTransactions;
    private BigDecimal totalAmount;
    private BigDecimal successfulAmount;
    private BigDecimal failedAmount;
    private Double successRate;
    private Double failureRate;
    private BigDecimal averageTransactionAmount;
}

