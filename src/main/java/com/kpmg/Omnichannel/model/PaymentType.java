package com.kpmg.omnichannel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "payment_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_type_id", updatable = false, nullable = false)
    private UUID paymentTypeId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "enabled_channels", columnDefinition = "json")
    private String enabledChannels;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
