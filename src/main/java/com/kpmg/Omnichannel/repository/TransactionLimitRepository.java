package com.kpmg.omnichannel.repository;

import com.kpmg.omnichannel.model.PaymentType;
import com.kpmg.omnichannel.model.Role;
import com.kpmg.omnichannel.model.TransactionLimit;
import com.kpmg.omnichannel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, UUID> {
    Optional<TransactionLimit> findByUserAndPaymentType(User user, PaymentType paymentType);

    Optional<TransactionLimit> findByRoleAndPaymentType(Role role, PaymentType paymentType);
}
