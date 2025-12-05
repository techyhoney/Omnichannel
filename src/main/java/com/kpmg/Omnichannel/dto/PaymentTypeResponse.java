package com.kpmg.omnichannel.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentTypeResponse {
    private UUID paymentTypeId;
    private String name;
    private String enabledChannels;
    private Boolean isActive;
}
