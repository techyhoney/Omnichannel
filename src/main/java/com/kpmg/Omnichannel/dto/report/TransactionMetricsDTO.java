package com.kpmg.omnichannel.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMetricsDTO {
    // Overall Metrics
    private Long totalTransactions;
    private Long initiatedTransactions;
    private Long processingTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    
    // Success/Failure Rates
    private Double successRate;
    private Double failureRate;
    private Double processingRate;
    
    // Amount Metrics
    private BigDecimal totalAmount;
    private BigDecimal successfulAmount;
    private BigDecimal failedAmount;
    private BigDecimal averageTransactionAmount;
    private BigDecimal maxTransactionAmount;
    private BigDecimal minTransactionAmount;
    
    // Status breakdown
    private Map<String, Long> statusBreakdown;
    private Map<String, BigDecimal> statusAmountBreakdown;
}

