package com.kpmg.omnichannel.controller;

import com.kpmg.omnichannel.dto.PaymentTypeRequest;
import com.kpmg.omnichannel.dto.PaymentTypeResponse;
import com.kpmg.omnichannel.service.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment-types")
@RequiredArgsConstructor
public class PaymentTypeController {

    private final PaymentTypeService paymentTypeService;

    @PostMapping
    public ResponseEntity<PaymentTypeResponse> createPaymentType(@RequestBody PaymentTypeRequest request) {
        return ResponseEntity.ok(paymentTypeService.createPaymentType(request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentTypeResponse>> getAllPaymentTypes() {
        return ResponseEntity.ok(paymentTypeService.getAllPaymentTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTypeResponse> getPaymentTypeById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentTypeService.getPaymentTypeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentTypeResponse> updatePaymentType(@PathVariable UUID id,
            @RequestBody PaymentTypeRequest request) {
        return ResponseEntity.ok(paymentTypeService.updatePaymentType(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentType(@PathVariable UUID id) {
        paymentTypeService.deletePaymentType(id);
        return ResponseEntity.noContent().build();
    }
}
