package com.anterka.bjyotish.entities;

// ==============================================
// PAYMENT ENTITY
// ==============================================

import com.anterka.bjyotish.constants.PaymentStatusEnum;
import com.anterka.bjyotish.constants.PaymentTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "payment_sequence_generator", sequenceName = "seq_payment_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentTypeEnum paymentType;

    @Column(name = "reference_id")
    private Long referenceId; // consultation_id, subscription_id, or order_id

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "payment_gateway", length = 50)
    private String paymentGateway;

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Column(name = "gateway_payment_id")
    private String gatewayPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private PaymentStatusEnum status = PaymentStatusEnum.PENDING;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
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
