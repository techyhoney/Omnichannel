package com.kpmg.omnichannel.service;

import com.kpmg.omnichannel.dto.TransactionLimitRequest;
import com.kpmg.omnichannel.dto.TransactionLimitResponse;
import com.kpmg.omnichannel.model.PaymentType;
import com.kpmg.omnichannel.model.Role;
import com.kpmg.omnichannel.model.TransactionLimit;
import com.kpmg.omnichannel.model.User;
import com.kpmg.omnichannel.repository.PaymentTypeRepository;
import com.kpmg.omnichannel.repository.RoleRepository;
import com.kpmg.omnichannel.repository.TransactionLimitRepository;
import com.kpmg.omnichannel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionLimitService {

    private final TransactionLimitRepository transactionLimitRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    public TransactionLimitResponse createTransactionLimit(TransactionLimitRequest request) {
        TransactionLimit limit = new TransactionLimit();

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            limit.setUser(user);
        } else if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            limit.setRole(role);
        } else {
            throw new RuntimeException("Either User ID or Role ID must be provided");
        }

        PaymentType paymentType = paymentTypeRepository.findById(request.getPaymentTypeId())
                .orElseThrow(() -> new RuntimeException("Payment Type not found"));
        limit.setPaymentType(paymentType);

        limit.setPerTxnLimit(request.getPerTxnLimit());
        limit.setDailyLimit(request.getDailyLimit());
        limit.setMonthlyLimit(request.getMonthlyLimit());

        TransactionLimit savedLimit = transactionLimitRepository.save(limit);
        return mapToResponse(savedLimit);
    }

    public List<TransactionLimitResponse> getAllTransactionLimits() {
        return transactionLimitRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TransactionLimitResponse updateTransactionLimit(UUID id, TransactionLimitRequest request) {
        TransactionLimit limit = transactionLimitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction Limit not found"));

        limit.setPerTxnLimit(request.getPerTxnLimit());
        limit.setDailyLimit(request.getDailyLimit());
        limit.setMonthlyLimit(request.getMonthlyLimit());

        TransactionLimit updatedLimit = transactionLimitRepository.save(limit);
        return mapToResponse(updatedLimit);
    }

    private TransactionLimitResponse mapToResponse(TransactionLimit limit) {
        TransactionLimitResponse response = new TransactionLimitResponse();
        response.setLimitId(limit.getLimitId());
        if (limit.getUser() != null) {
            response.setUserId(limit.getUser().getUserId());
        }
        if (limit.getRole() != null) {
            response.setRoleId(limit.getRole().getRoleId());
        }
        response.setPaymentTypeId(limit.getPaymentType().getPaymentTypeId());
        response.setPerTxnLimit(limit.getPerTxnLimit());
        response.setDailyLimit(limit.getDailyLimit());
        response.setMonthlyLimit(limit.getMonthlyLimit());
        return response;
    }
}
