package com.foodordering.dto.request;

import com.foodordering.model.entity.Payment;
import jakarta.validation.constraints.NotBlank;

public class PaymentRequest {
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    private Payment.PaymentMethod paymentMethod;
    
    private String description;

    public PaymentRequest() {
    }

    public PaymentRequest(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Payment.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

