package com.kpmg.omnichannel.controller;

import com.kpmg.omnichannel.dto.ApiResponse;
import com.kpmg.omnichannel.dto.WalletRequest;
import com.kpmg.omnichannel.dto.WalletResponse;
import com.kpmg.omnichannel.model.User;
import com.kpmg.omnichannel.repository.UserRepository;
import com.kpmg.omnichannel.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Wallet Management", description = "APIs for managing user wallet balances")
public class WalletController {

    private final WalletService walletService;
    private final UserRepository userRepository;

    @GetMapping("/{userId}/balance")
    @Operation(summary = "Get wallet balance", 
               description = "Get current wallet balance for a user")
    public ResponseEntity<ApiResponse<WalletResponse>> getBalance(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        BigDecimal balance = walletService.getBalance(userId);
        
        WalletResponse response = WalletResponse.builder()
                .userId(userId)
                .userName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .walletBalance(balance)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success("Wallet balance retrieved successfully", response));
    }

    @PostMapping("/{userId}/add-money")
    @Operation(summary = "Add money to wallet", 
               description = "Add money to user's wallet (for testing/top-up)")
    public ResponseEntity<ApiResponse<WalletResponse>> addMoney(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Valid @RequestBody WalletRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        BigDecimal previousBalance = user.getWalletBalance();
        BigDecimal newBalance = walletService.addMoney(userId, request.getAmount());
        
        WalletResponse response = WalletResponse.builder()
                .userId(userId)
                .userName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .walletBalance(newBalance)
                .previousBalance(previousBalance)
                .amountChanged(request.getAmount())
                .operation("CREDIT")
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(
                String.format("Successfully added %.2f to wallet", request.getAmount()), 
                response));
    }

    @PostMapping("/{userId}/deduct-money")
    @Operation(summary = "Deduct money from wallet", 
               description = "Deduct money from user's wallet")
    public ResponseEntity<ApiResponse<WalletResponse>> deductMoney(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Valid @RequestBody WalletRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        BigDecimal previousBalance = user.getWalletBalance();
        BigDecimal newBalance = walletService.deductMoney(userId, request.getAmount());
        
        WalletResponse response = WalletResponse.builder()
                .userId(userId)
                .userName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .walletBalance(newBalance)
                .previousBalance(previousBalance)
                .amountChanged(request.getAmount())
                .operation("DEBIT")
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(
                String.format("Successfully deducted %.2f from wallet", request.getAmount()), 
                response));
    }
}
