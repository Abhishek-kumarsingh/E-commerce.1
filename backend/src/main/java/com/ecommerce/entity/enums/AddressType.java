package com.ecommerce.entity.enums;

public enum AddressType {
    HOME("Home Address"),
    WORK("Work Address"),
    OTHER("Other Address");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
