package com.foodordering.model.entity;

import com.foodordering.model.abstraction.BaseEntity;
import com.foodordering.model.abstraction.IAuditable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
public class Order extends BaseEntity implements IAuditable {

    @Field("account_id")
    private String accountId;

    @Field("order_items")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Field("total_amount")
    private Double totalAmount;

    @Field("status")
    private OrderStatus status = OrderStatus.PENDING;

    @Field("delivery_address")
    private String deliveryAddress;

    @Field("notes")
    private String notes;

    public Order() {
    }

    public Order(String accountId, List<OrderItem> orderItems) {
        this.accountId = accountId;
        this.orderItems = orderItems;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        return "Order{" +
                "id='" + getId() + '\'' +
                ", accountId='" + accountId + '\'' +
                ", orderItems=" + orderItems +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", modifiedAt=" + getModifiedAt() +
                '}';
    }

    // Nested class for Order Items
    public static class OrderItem {
        @Field("menu_item_id")
        private String menuItemId;

        @Field("quantity")
        private Integer quantity;

        @Field("price")
        private Double price;

        @Field("subtotal")
        private Double subtotal;

        public OrderItem() {
        }

        public OrderItem(String menuItemId, Integer quantity, Double price) {
            this.menuItemId = menuItemId;
            this.quantity = quantity;
            this.price = price;
            this.subtotal = price * quantity;
        }

        public String getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(String menuItemId) {
            this.menuItemId = menuItemId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
            // Recalculate subtotal when quantity changes
            if (this.price != null) {
                this.subtotal = this.price * quantity;
            }
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
            // Recalculate subtotal when price changes
            if (this.quantity != null) {
                this.subtotal = price * quantity;
            }
        }

        public Double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(Double subtotal) {
            this.subtotal = subtotal;
        }

        @Override
        public String toString() {
            return "OrderItem{" +
                    "menuItemId='" + menuItemId + '\'' +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    ", subtotal=" + subtotal +
                    '}';
        }
    }

    // Order Status Enum
    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PREPARING,
        READY,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED
    }
}

