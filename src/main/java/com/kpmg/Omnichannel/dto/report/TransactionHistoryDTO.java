package com.kpmg.omnichannel.dto.report;

import com.kpmg.omnichannel.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryDTO {
    private UUID transactionId;
    private String userName;
    private String merchantName;
    private String paymentTypeName;
    private BigDecimal amount;
    private String currency;
    private TransactionStatus status;
    private LocalDateTime transactionDate;
}

