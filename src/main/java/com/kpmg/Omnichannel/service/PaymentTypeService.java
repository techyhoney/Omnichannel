package com.kpmg.omnichannel.service;

import com.kpmg.omnichannel.dto.PaymentTypeRequest;
import com.kpmg.omnichannel.dto.PaymentTypeResponse;
import com.kpmg.omnichannel.model.PaymentType;
import com.kpmg.omnichannel.repository.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;

    public PaymentTypeResponse createPaymentType(PaymentTypeRequest request) {
        PaymentType paymentType = new PaymentType();
        paymentType.setName(request.getName());
        paymentType.setEnabledChannels(request.getEnabledChannels());
        paymentType.setIsActive(request.getIsActive());

        PaymentType savedPaymentType = paymentTypeRepository.save(paymentType);
        return mapToResponse(savedPaymentType);
    }

    public List<PaymentTypeResponse> getAllPaymentTypes() {
        return paymentTypeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentTypeResponse getPaymentTypeById(UUID id) {
        PaymentType paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Type not found"));
        return mapToResponse(paymentType);
    }

    public PaymentTypeResponse updatePaymentType(UUID id, PaymentTypeRequest request) {
        PaymentType paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Type not found"));

        paymentType.setName(request.getName());
        paymentType.setEnabledChannels(request.getEnabledChannels());
        paymentType.setIsActive(request.getIsActive());

        PaymentType updatedPaymentType = paymentTypeRepository.save(paymentType);
        return mapToResponse(updatedPaymentType);
    }

    public void deletePaymentType(UUID id) {
        paymentTypeRepository.deleteById(id);
    }

    private PaymentTypeResponse mapToResponse(PaymentType paymentType) {
        PaymentTypeResponse response = new PaymentTypeResponse();
        response.setPaymentTypeId(paymentType.getPaymentTypeId());
        response.setName(paymentType.getName());
        response.setEnabledChannels(paymentType.getEnabledChannels());
        response.setIsActive(paymentType.getIsActive());
        return response;
    }
}
