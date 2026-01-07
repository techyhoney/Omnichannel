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
public class MonthlyReportDTO {
    private Integer year;
    private Integer month;
    private String monthName;
    private Long totalTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    private BigDecimal totalAmount;
    private BigDecimal successfulAmount;
    private Double successRate;
    private BigDecimal averageTransactionAmount;
}

