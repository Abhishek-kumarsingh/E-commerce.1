package com.ecommerce.entity.enums;

public enum OrderStatus {
    PENDING("Order placed, awaiting confirmation"),
    CONFIRMED("Order confirmed, preparing for processing"),
    PROCESSING("Order is being processed"),
    SHIPPED("Order has been shipped"),
    OUT_FOR_DELIVERY("Order is out for delivery"),
    DELIVERED("Order has been delivered"),
    CANCELLED("Order has been cancelled"),
    REFUNDED("Order has been refunded");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this != CANCELLED && this != REFUNDED;
    }

    public boolean isCompleted() {
        return this == DELIVERED;
    }

    public boolean isCancellable() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean isRefundable() {
        return this == DELIVERED;
    }
}
