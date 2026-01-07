package com.kpmg.omnichannel.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeAnalyticsDTO {
    private UUID paymentTypeId;
    private String paymentTypeName;
    private Long totalTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    private BigDecimal totalAmount;
    private BigDecimal successfulAmount;
    private Double successRate;
    private Double usagePercentage;
    private BigDecimal averageTransactionAmount;
}

