package com.kpmg.omnichannel.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequest {
    private UUID userId;
    private UUID merchantId;
    private UUID paymentTypeId;
    private BigDecimal amount;
    private String currency;
}
