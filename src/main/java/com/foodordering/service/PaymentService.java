package com.foodordering.service;

import com.foodordering.dto.request.PaymentRequest;
import com.foodordering.dto.response.PaymentResponse;
import com.foodordering.model.entity.Payment;

import java.util.List;

public interface PaymentService {
    
    PaymentResponse create(PaymentRequest request);
    
    PaymentResponse getById(String id);
    
    List<PaymentResponse> getAll();
    
    List<PaymentResponse> getByOrder(String orderId);
    
    List<PaymentResponse> getByStatus(Payment.PaymentStatus status);
    
    PaymentResponse processMomoPayment(String paymentId);
    
    PaymentResponse handleMomoCallback(String orderId, String resultCode, String message);
    
    PaymentResponse update(String id, PaymentRequest request);
    
    void delete(String id);
    
    void softDelete(String id);
}

