package com.ecommerce.entity.enums;

public enum PaymentMethod {
    CARD("Credit/Debit Card"),
    UPI("UPI Payment"),
    WALLET("Digital Wallet"),
    BANK_TRANSFER("Bank Transfer"),
    COD("Cash on Delivery"),
    NET_BANKING("Net Banking");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isOnline() {
        return this != COD;
    }

    public boolean requiresVerification() {
        return this == CARD || this == NET_BANKING;
    }
}
