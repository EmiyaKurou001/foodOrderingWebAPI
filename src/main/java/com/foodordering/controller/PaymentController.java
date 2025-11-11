package com.foodordering.controller;

import com.foodordering.dto.request.PaymentRequest;
import com.foodordering.dto.response.PaymentResponse;
import com.foodordering.model.entity.Payment;
import com.foodordering.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable String id) {
        PaymentResponse response = paymentService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {
        List<PaymentResponse> responses = paymentService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getByOrder(@PathVariable String orderId) {
        List<PaymentResponse> responses = paymentService.getByOrder(orderId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getByStatus(@PathVariable Payment.PaymentStatus status) {
        List<PaymentResponse> responses = paymentService.getByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/process-momo")
    public ResponseEntity<PaymentResponse> processMomoPayment(@PathVariable String id) {
        PaymentResponse response = paymentService.processMomoPayment(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/callback")
    public ResponseEntity<PaymentResponse> handleMomoCallback(@RequestBody Map<String, String> callbackData) {
        String orderId = callbackData.get("orderId");
        String resultCode = callbackData.get("resultCode");
        String message = callbackData.get("message");
        
        PaymentResponse response = paymentService.handleMomoCallback(orderId, resultCode, message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleMomoWebhook(@RequestBody Map<String, String> webhookData) {
        // Handle MoMo webhook (IPN - Instant Payment Notification)
        String orderId = webhookData.get("orderId");
        String resultCode = webhookData.get("resultCode");
        String message = webhookData.get("message");
        
        paymentService.handleMomoCallback(orderId, resultCode, message);
        return ResponseEntity.ok("OK");
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> update(@PathVariable String id, @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDelete(@PathVariable String id) {
        paymentService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}

