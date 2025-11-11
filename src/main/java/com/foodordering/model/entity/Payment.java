package com.foodordering.model.entity;

import com.foodordering.model.abstraction.BaseEntity;
import com.foodordering.model.abstraction.IAuditable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "payments")
public class Payment extends BaseEntity implements IAuditable {

    @Field("order_id")
    private String orderId;

    @Field("amount")
    private Double amount;

    @Field("payment_method")
    private PaymentMethod paymentMethod = PaymentMethod.MOMO;

    @Field("status")
    private PaymentStatus status = PaymentStatus.PENDING;

    @Field("momo_transaction_id")
    private String momoTransactionId;

    @Field("momo_order_id")
    private String momoOrderId;

    @Field("momo_pay_url")
    private String momoPayUrl;

    @Field("momo_response_code")
    private String momoResponseCode;

    @Field("momo_message")
    private String momoMessage;

    @Field("paid_at")
    private Instant paidAt;

    @Field("description")
    private String description;

    public Payment() {
    }

    public Payment(String orderId, Double amount) {
        this.orderId = orderId;
        this.amount = amount;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
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

    @Override
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public Instant getModifiedAt() {
        return this.modifiedAt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + getId() + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", paymentMethod=" + paymentMethod +
                ", status=" + status +
                ", momoTransactionId='" + momoTransactionId + '\'' +
                ", paidAt=" + paidAt +
                ", createdAt=" + getCreatedAt() +
                ", modifiedAt=" + getModifiedAt() +
                '}';
    }

    // Payment Method Enum
    public enum PaymentMethod {
        MOMO,
        CASH,
        BANK_TRANSFER,
        CREDIT_CARD
    }

    // Payment Status Enum
    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        SUCCESS,
        FAILED,
        CANCELLED,
        REFUNDED
    }
}

