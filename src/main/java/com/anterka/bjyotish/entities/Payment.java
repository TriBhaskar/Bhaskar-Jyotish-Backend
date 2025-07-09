package com.anterka.bjyotish.entities;

// ==============================================
// PAYMENT ENTITY
// ==============================================

import com.anterka.bjyotish.constants.enums.PaymentStatusEnum;
import com.anterka.bjyotish.constants.enums.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private BjyotishUser bjyotishUser;

    private BigDecimal amount;

    @Builder.Default
    private String currency = "INR";

    private PaymentTypeEnum paymentType;

    private Long referenceId; // consultation_id, subscription_id, or order_id

    private String paymentMethod;

    private String paymentGateway;

    private String gatewayTransactionId;

    private String gatewayPaymentId;

    @Builder.Default
    private PaymentStatusEnum status = PaymentStatusEnum.PENDING;

    private Instant paidAt;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Utility methods
    public boolean isSuccessful() {
        return status != null && status.isSuccessful();
    }

    public boolean isPending() {
        return status == PaymentStatusEnum.PENDING;
    }

    public boolean isFailed() {
        return status == PaymentStatusEnum.FAILED;
    }

    public boolean isRefunded() {
        return status == PaymentStatusEnum.REFUNDED;
    }

    public boolean isCancelled() {
        return status == PaymentStatusEnum.CANCELLED;
    }

    public boolean canBeRefunded() {
        return status != null && status.canBeRefunded();
    }

    public boolean canBeCancelled() {
        return status != null && status.canBeCancelled();
    }

    public void markAsCompleted() {
        this.status = PaymentStatusEnum.COMPLETED;
        this.paidAt = Instant.now();
    }

    public void markAsFailed() {
        this.status = PaymentStatusEnum.FAILED;
    }

    public void markAsRefunded() {
        this.status = PaymentStatusEnum.REFUNDED;
    }

    public void markAsCancelled() {
        this.status = PaymentStatusEnum.CANCELLED;
    }

    public String getPaymentTypeDisplayName() {
        return paymentType != null ? paymentType.getDisplayName() : "Unknown";
    }

    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : "Unknown";
    }

    public String getAmountFormatted() {
        return currency + " " + (amount != null ? amount.toString() : "0.00");
    }

    public boolean hasGatewayInfo() {
        return gatewayTransactionId != null || gatewayPaymentId != null;
    }

    public String getPaymentSummary() {
        return String.format("%s payment of %s - %s",
                getPaymentTypeDisplayName(), getAmountFormatted(), getStatusDisplayName());
    }

    public boolean isForConsultation() {
        return paymentType == PaymentTypeEnum.CONSULTATION;
    }

    public boolean isForSubscription() {
        return paymentType == PaymentTypeEnum.SUBSCRIPTION;
    }

    public boolean isForProduct() {
        return paymentType == PaymentTypeEnum.PRODUCT_PURCHASE;
    }
}
