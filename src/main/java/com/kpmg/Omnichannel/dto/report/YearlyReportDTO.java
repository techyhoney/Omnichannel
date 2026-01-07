package com.kpmg.omnichannel.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YearlyReportDTO {
    private Integer year;
    private Long totalTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    private BigDecimal totalAmount;
    private BigDecimal successfulAmount;
    private Double successRate;
    private BigDecimal averageTransactionAmount;
    private List<MonthlyReportDTO> monthlyBreakdown;
}

