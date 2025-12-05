package com.kpmg.omnichannel.service;

import com.kpmg.omnichannel.dto.TransactionRequest;
import com.kpmg.omnichannel.dto.TransactionResponse;
import com.kpmg.omnichannel.model.PaymentType;
import com.kpmg.omnichannel.model.Transaction;
import com.kpmg.omnichannel.model.TransactionStatus;
import com.kpmg.omnichannel.model.User;
import com.kpmg.omnichannel.repository.PaymentTypeRepository;
import com.kpmg.omnichannel.repository.TransactionRepository;
import com.kpmg.omnichannel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    @Transactional
    public TransactionResponse initiateTransaction(TransactionRequest request) {
        // TODO: Check Transaction Limits

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        User merchant = userRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        PaymentType paymentType = paymentTypeRepository.findById(request.getPaymentTypeId())
                .orElseThrow(() -> new RuntimeException("Payment Type not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setMerchant(merchant);
        transaction.setPaymentType(paymentType);
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setStatus(TransactionStatus.INITIATED);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponse(savedTransaction);
    }

    public TransactionResponse getTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponse updateTransactionStatus(UUID id, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setStatus(status);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToResponse(updatedTransaction);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(transaction.getTransactionId());
        response.setUserId(transaction.getUser().getUserId());
        response.setMerchantId(transaction.getMerchant().getUserId());
        response.setPaymentTypeId(transaction.getPaymentType().getPaymentTypeId());
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency());
        response.setStatus(transaction.getStatus());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
}
