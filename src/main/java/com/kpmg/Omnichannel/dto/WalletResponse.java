package com.kpmg.omnichannel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletResponse {
    private UUID userId;
    private String userName;
    private String email;
    private BigDecimal walletBalance;
    private BigDecimal previousBalance;
    private BigDecimal amountChanged;
    private String operation;
}
