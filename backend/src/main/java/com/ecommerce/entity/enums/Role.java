package com.ecommerce.entity.enums;

public enum Role {
    USER("Regular User"),
    ADMIN("Administrator"),
    MODERATOR("Moderator");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isModerator() {
        return this == MODERATOR || this == ADMIN;
    }

    public boolean isUser() {
        return this == USER;
    }
}
