package com.kpmg.omnichannel.controller;

import com.kpmg.omnichannel.dto.TransactionRequest;
import com.kpmg.omnichannel.dto.TransactionResponse;
import com.kpmg.omnichannel.model.TransactionStatus;
import com.kpmg.omnichannel.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> initiateTransaction(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.initiateTransaction(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TransactionResponse> updateTransactionStatus(@PathVariable UUID id,
            @RequestParam TransactionStatus status) {
        return ResponseEntity.ok(transactionService.updateTransactionStatus(id, status));
    }
}
