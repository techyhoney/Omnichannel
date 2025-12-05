package com.kpmg.omnichannel.dto;

import lombok.Data;

@Data
public class PaymentTypeRequest {
    private String name;
    private String enabledChannels;
    private Boolean isActive;
}
