package com.kpmg.omnichannel.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionLimitRequest {
    private UUID userId;
    private UUID roleId;
    private UUID paymentTypeId;
    private BigDecimal perTxnLimit;
    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;
}
