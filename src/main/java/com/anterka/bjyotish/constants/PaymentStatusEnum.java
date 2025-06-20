package com.anterka.bjyotish.constants;

public enum PaymentStatusEnum {
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    REFUNDED("Refunded"),
    CANCELLED("Cancelled");

    private final String displayName;

    PaymentStatusEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isSuccessful() {
        return this == COMPLETED;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == REFUNDED || this == CANCELLED;
    }

    public boolean canBeRefunded() {
        return this == COMPLETED;
    }

    public boolean canBeCancelled() {
        return this == PENDING;
    }
}
