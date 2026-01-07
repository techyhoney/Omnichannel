package com.kpmg.omnichannel.dto.report;

import com.kpmg.omnichannel.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryRequest {
    private UUID userId;
    private UUID merchantId;
    private UUID paymentTypeId;
    private TransactionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String currency;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}

