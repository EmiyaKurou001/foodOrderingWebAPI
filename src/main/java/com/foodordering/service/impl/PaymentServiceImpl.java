package com.foodordering.service.impl;

import com.foodordering.dto.request.PaymentRequest;
import com.foodordering.dto.response.PaymentResponse;
import com.foodordering.integration.momo.MomoPayService;
import com.foodordering.model.entity.Order;
import com.foodordering.model.entity.Payment;
import com.foodordering.repository.OrderRepository;
import com.foodordering.repository.PaymentRepository;
import com.foodordering.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MomoPayService momoPayService;

    @Override
    public PaymentResponse create(PaymentRequest request) {
        // Validate order exists
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));

        // Check if order already has a successful payment
        List<Payment> existingPayments = paymentRepository.findByOrderId(request.getOrderId());
        boolean hasSuccessfulPayment = existingPayments.stream()
                .anyMatch(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS);
        
        if (hasSuccessfulPayment) {
            throw new RuntimeException("Order already has a successful payment");
        }

        // Get amount from order
        Double amount = order.getTotalAmount();
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Order amount is invalid: " + amount);
        }

        // Create payment entity
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(amount);
        payment.setDescription(request.getDescription() != null ? 
                request.getDescription() : "Payment for order " + request.getOrderId());
        
        if (request.getPaymentMethod() != null) {
            payment.setPaymentMethod(request.getPaymentMethod());
        } else {
            payment.setPaymentMethod(Payment.PaymentMethod.MOMO);
        }

        payment.setStatus(Payment.PaymentStatus.PENDING);

        // If MoMo payment, create payment request
        if (payment.getPaymentMethod() == Payment.PaymentMethod.MOMO) {
            MomoPayService.MomoPaymentResponse momoResponse = momoPayService.createPayment(
                    request.getOrderId(),
                    amount,
                    payment.getDescription()
            );

            if ("0".equals(momoResponse.getResultCode())) {
                payment.setMomoOrderId(momoResponse.getOrderId());
                payment.setMomoPayUrl(momoResponse.getPayUrl());
                payment.setMomoResponseCode(momoResponse.getResultCode());
                payment.setMomoMessage(momoResponse.getMessage());
                payment.setStatus(Payment.PaymentStatus.PROCESSING);
            } else {
                payment.setMomoResponseCode(momoResponse.getResultCode());
                payment.setMomoMessage(momoResponse.getMessage());
                payment.setStatus(Payment.PaymentStatus.FAILED);
            }
        }

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    @Override
    public PaymentResponse getById(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getByOrder(String orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse processMomoPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        if (payment.getPaymentMethod() != Payment.PaymentMethod.MOMO) {
            throw new RuntimeException("Payment is not a MoMo payment");
        }

        if (payment.getStatus() != Payment.PaymentStatus.PROCESSING && 
            payment.getStatus() != Payment.PaymentStatus.PENDING) {
            throw new RuntimeException("Payment cannot be processed in current status: " + payment.getStatus());
        }

        // In production, this would check payment status with MoMo API
        // For now, we'll return the payment URL for user to complete payment
        return toResponse(payment);
    }

    @Override
    public PaymentResponse handleMomoCallback(String orderId, String resultCode, String message) {
        Payment payment = paymentRepository.findByMomoOrderId(orderId);
        
        if (payment == null) {
            throw new RuntimeException("Payment not found for MoMo order ID: " + orderId);
        }

        // Update payment status based on callback
        if ("0".equals(resultCode)) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setPaidAt(Instant.now());
            payment.setMomoTransactionId(orderId);
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }

        payment.setMomoResponseCode(resultCode);
        payment.setMomoMessage(message);

        Payment updated = paymentRepository.save(payment);

        // Update order status if payment successful
        if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
            Order order = orderRepository.findById(payment.getOrderId())
                    .orElse(null);
            if (order != null && order.getStatus() == Order.OrderStatus.PENDING) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
                orderRepository.save(order);
            }
        }

        return toResponse(updated);
    }

    @Override
    public PaymentResponse update(String id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        // Only allow updates to pending payments
        if (payment.getStatus() != Payment.PaymentStatus.PENDING) {
            throw new RuntimeException("Cannot update payment with status: " + payment.getStatus());
        }

        if (request.getDescription() != null) {
            payment.setDescription(request.getDescription());
        }

        Payment updated = paymentRepository.save(payment);
        return toResponse(updated);
    }

    @Override
    public void delete(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public void softDelete(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        payment.softDelete();
        paymentRepository.save(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setOrderId(payment.getOrderId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setMomoTransactionId(payment.getMomoTransactionId());
        response.setMomoOrderId(payment.getMomoOrderId());
        response.setMomoPayUrl(payment.getMomoPayUrl());
        response.setMomoResponseCode(payment.getMomoResponseCode());
        response.setMomoMessage(payment.getMomoMessage());
        response.setPaidAt(payment.getPaidAt());
        response.setDescription(payment.getDescription());
        response.setCreatedAt(payment.getCreatedAt());
        response.setModifiedAt(payment.getModifiedAt());
        return response;
    }
}

