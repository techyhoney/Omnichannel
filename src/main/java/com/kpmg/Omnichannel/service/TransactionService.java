package com.kpmg.omnichannel.service;

import com.kpmg.omnichannel.dto.TransactionRequest;
import com.kpmg.omnichannel.dto.TransactionResponse;
import com.kpmg.omnichannel.exception.AccountNotActiveException;
import com.kpmg.omnichannel.exception.InsufficientBalanceException;
import com.kpmg.omnichannel.exception.KycNotVerifiedException;
import com.kpmg.omnichannel.exception.ResourceNotFoundException;
import com.kpmg.omnichannel.model.*;
import com.kpmg.omnichannel.repository.PaymentTypeRepository;
import com.kpmg.omnichannel.repository.TransactionRepository;
import com.kpmg.omnichannel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final WalletService walletService;

    @Transactional
    public TransactionResponse initiateTransaction(TransactionRequest request) {
        // Fetch user and merchant
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        User merchant = userRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + request.getMerchantId()));

        // Validate User Account Status
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AccountNotActiveException(
                    "Transaction not allowed: User account is " + user.getStatus() + 
                    ". Only ACTIVE accounts can perform transactions."
            );
        }

        // Validate User KYC Status
        if (user.getKycStatus() != KycStatus.VERIFIED) {
            throw new KycNotVerifiedException(
                    "Transaction not allowed: User KYC status is " + user.getKycStatus() + 
                    ". KYC verification is required to perform transactions."
            );
        }

        // Validate Merchant Account Status
        if (merchant.getStatus() != UserStatus.ACTIVE) {
            throw new AccountNotActiveException(
                    "Transaction not allowed: Merchant account is " + merchant.getStatus() + 
                    ". Transactions can only be made to ACTIVE merchants."
            );
        }

        // Validate Merchant KYC Status
        if (merchant.getKycStatus() != KycStatus.VERIFIED) {
            throw new KycNotVerifiedException(
                    "Transaction not allowed: Merchant KYC status is " + merchant.getKycStatus() + 
                    ". Transactions can only be made to KYC verified merchants."
            );
        }

        // Fetch and validate payment type
        PaymentType paymentType = paymentTypeRepository.findById(request.getPaymentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment type not found with id: " + request.getPaymentTypeId()));

        // Validate payment type is active
        if (!paymentType.getIsActive()) {
            throw new AccountNotActiveException(
                    "Transaction not allowed: Payment type '" + paymentType.getName() + 
                    "' is currently inactive."
            );
        }

        // TODO: Check Transaction Limits

        // Check if user has sufficient balance
        if (!walletService.hasSufficientBalance(user.getUserId(), request.getAmount())) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient wallet balance. Available: %.2f, Required: %.2f",
                            walletService.getBalance(user.getUserId()), request.getAmount())
            );
        }

        // Create and save transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setMerchant(merchant);
        transaction.setPaymentType(paymentType);
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setStatus(TransactionStatus.INITIATED);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction initiated: {} for amount: {}", savedTransaction.getTransactionId(), request.getAmount());
        
        return mapToResponse(savedTransaction);
    }

    public TransactionResponse getTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponse updateTransactionStatus(UUID id, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        TransactionStatus oldStatus = transaction.getStatus();
        transaction.setStatus(status);
        Transaction updatedTransaction = transactionRepository.save(transaction);

        // Update wallet balances when transaction is approved (SUCCESS status)
        if (status == TransactionStatus.SUCCESS && oldStatus != TransactionStatus.SUCCESS) {
            try {
                walletService.transfer(
                        transaction.getUser().getUserId(),
                        transaction.getMerchant().getUserId(),
                        transaction.getAmount()
                );
                log.info("Wallet balances updated for transaction: {}", id);
            } catch (Exception e) {
                // Rollback transaction status if wallet update fails
                transaction.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                log.error("Failed to update wallet balances for transaction: {}", id, e);
                throw new RuntimeException("Transaction failed: Unable to update wallet balances - " + e.getMessage());
            }
        }

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
