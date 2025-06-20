package com.anterka.bjyotish.constants;

public enum PaymentTypeEnum {
    CONSULTATION("Consultation"),
    SUBSCRIPTION("Subscription"),
    PRODUCT_PURCHASE("Product Purchase");

    private final String displayName;

    PaymentTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isRecurring() {
        return this == SUBSCRIPTION;
    }

    public boolean isOneTime() {
        return this == CONSULTATION || this == PRODUCT_PURCHASE;
    }
}
