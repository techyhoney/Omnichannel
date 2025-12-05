package com.kpmg.omnichannel.controller;

import com.kpmg.omnichannel.dto.TransactionLimitRequest;
import com.kpmg.omnichannel.dto.TransactionLimitResponse;
import com.kpmg.omnichannel.service.TransactionLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction-limits")
@RequiredArgsConstructor
public class TransactionLimitController {

    private final TransactionLimitService transactionLimitService;

    @PostMapping
    public ResponseEntity<TransactionLimitResponse> createTransactionLimit(
            @RequestBody TransactionLimitRequest request) {
        return ResponseEntity.ok(transactionLimitService.createTransactionLimit(request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionLimitResponse>> getAllTransactionLimits() {
        return ResponseEntity.ok(transactionLimitService.getAllTransactionLimits());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionLimitResponse> updateTransactionLimit(@PathVariable UUID id,
            @RequestBody TransactionLimitRequest request) {
        return ResponseEntity.ok(transactionLimitService.updateTransactionLimit(id, request));
    }
}
