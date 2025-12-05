package com.kpmg.omnichannel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transaction_limits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "limit_id", updatable = false, nullable = false)
    private UUID limitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id", nullable = false)
    private PaymentType paymentType;

    @Column(name = "per_txn_limit", precision = 18, scale = 2)
    private BigDecimal perTxnLimit;

    @Column(name = "daily_limit", precision = 18, scale = 2)
    private BigDecimal dailyLimit;

    @Column(name = "monthly_limit", precision = 18, scale = 2)
    private BigDecimal monthlyLimit;
}
