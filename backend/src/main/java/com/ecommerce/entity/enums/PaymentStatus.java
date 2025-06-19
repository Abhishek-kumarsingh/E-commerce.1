package com.ecommerce.entity.enums;

public enum PaymentStatus {
    PENDING("Payment is pending"),
    PROCESSING("Payment is being processed"),
    COMPLETED("Payment completed successfully"),
    FAILED("Payment failed"),
    CANCELLED("Payment was cancelled"),
    REFUNDED("Payment has been refunded"),
    PARTIALLY_REFUNDED("Payment has been partially refunded");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSuccessful() {
        return this == COMPLETED;
    }

    public boolean isFailed() {
        return this == FAILED || this == CANCELLED;
    }

    public boolean isRefundable() {
        return this == COMPLETED;
    }

    public boolean isPending() {
        return this == PENDING || this == PROCESSING;
    }
}
