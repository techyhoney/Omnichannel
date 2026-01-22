package com.kpmg.omnichannel.service;

import com.kpmg.omnichannel.exception.InsufficientBalanceException;
import com.kpmg.omnichannel.exception.ResourceNotFoundException;
import com.kpmg.omnichannel.model.User;
import com.kpmg.omnichannel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final UserRepository userRepository;

    /**
     * Get wallet balance for a user
     */
    public BigDecimal getBalance(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return user.getWalletBalance();
    }

    /**
     * Add money to user's wallet
     */
    @Transactional
    public BigDecimal addMoney(UUID userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        BigDecimal newBalance = user.getWalletBalance().add(amount);
        user.setWalletBalance(newBalance);
        userRepository.save(user);

        log.info("Added {} to user {}. New balance: {}", amount, userId, newBalance);
        return newBalance;
    }

    /**
     * Deduct money from user's wallet
     */
    @Transactional
    public BigDecimal deductMoney(UUID userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check sufficient balance
        if (user.getWalletBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Available: %.2f, Required: %.2f",
                            user.getWalletBalance(), amount)
            );
        }

        BigDecimal newBalance = user.getWalletBalance().subtract(amount);
        user.setWalletBalance(newBalance);
        userRepository.save(user);

        log.info("Deducted {} from user {}. New balance: {}", amount, userId, newBalance);
        return newBalance;
    }

    /**
     * Transfer money between users
     */
    @Transactional
    public void transfer(UUID fromUserId, UUID toUserId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        // Deduct from sender
        deductMoney(fromUserId, amount);

        // Add to receiver
        addMoney(toUserId, amount);

        log.info("Transferred {} from user {} to user {}", amount, fromUserId, toUserId);
    }

    /**
     * Check if user has sufficient balance
     */
    public boolean hasSufficientBalance(UUID userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return user.getWalletBalance().compareTo(amount) >= 0;
    }
}
