package com.foodordering.dto.response;

import com.foodordering.model.entity.Payment;
import java.time.Instant;

public class PaymentResponse {
    
    private String id;
    private String orderId;
    private Double amount;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
    private String momoTransactionId;
    private String momoOrderId;
    private String momoPayUrl;
    private String momoResponseCode;
    private String momoMessage;
    private Instant paidAt;
    private String description;
    private Instant createdAt;
    private Instant modifiedAt;

    public PaymentResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Payment.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Payment.PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(Payment.PaymentStatus status) {
        this.status = status;
    }

    public String getMomoTransactionId() {
        return momoTransactionId;
    }

    public void setMomoTransactionId(String momoTransactionId) {
        this.momoTransactionId = momoTransactionId;
    }

    public String getMomoOrderId() {
        return momoOrderId;
    }

    public void setMomoOrderId(String momoOrderId) {
        this.momoOrderId = momoOrderId;
    }

    public String getMomoPayUrl() {
        return momoPayUrl;
    }

    public void setMomoPayUrl(String momoPayUrl) {
        this.momoPayUrl = momoPayUrl;
    }

    public String getMomoResponseCode() {
        return momoResponseCode;
    }

    public void setMomoResponseCode(String momoResponseCode) {
        this.momoResponseCode = momoResponseCode;
    }

    public String getMomoMessage() {
        return momoMessage;
    }

    public void setMomoMessage(String momoMessage) {
        this.momoMessage = momoMessage;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

