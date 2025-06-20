package com.anterka.bjyotish.entities;

// ==============================================
// ORDER ENTITY
// ==============================================

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "order_sequence_generator", sequenceName = "seq_order_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal subtotal;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    @Builder.Default
    private java.math.BigDecimal taxAmount = java.math.BigDecimal.ZERO;

    @Column(name = "shipping_amount", precision = 10, scale = 2)
    @Builder.Default
    private java.math.BigDecimal shippingAmount = java.math.BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "shipping_address", columnDefinition = "JSONB")
    private String shippingAddress; // JSON string

    @Column(name = "billing_address", columnDefinition = "JSONB")
    private String billingAddress; // JSON string

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> orderItems;

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
    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }

    public boolean isConfirmed() {
        return status == OrderStatus.CONFIRMED;
    }

    public boolean isShipped() {
        return status == OrderStatus.SHIPPED;
    }

    public boolean isDelivered() {
        return status == OrderStatus.DELIVERED;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }

    public boolean isCompleted() {
        return status == OrderStatus.DELIVERED;
    }

    public boolean canCancel() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    public boolean canShip() {
        return status == OrderStatus.CONFIRMED;
    }

    public boolean canDeliver() {
        return status == OrderStatus.SHIPPED;
    }

    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void ship() {
        this.status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        this.status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public int getTotalItems() {
        return orderItems != null ? orderItems.stream().mapToInt(OrderItem::getQuantity).sum() : 0;
    }

    public int getUniqueItemsCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    public String getCustomerName() {
        return bjyotishUser != null ? bjyotishUser.getFirstName() + " " + bjyotishUser.getLastName() : "Unknown Customer";
    }

    public String getCustomerEmail() {
        return bjyotishUser != null ? bjyotishUser.getEmail() : "Unknown Email";
    }

    public boolean hasDigitalItems() {
        return orderItems != null && orderItems.stream()
                .anyMatch(item -> item.getProduct() != null && item.getProduct().isDigital());
    }

    public boolean hasPhysicalItems() {
        return orderItems != null && orderItems.stream()
                .anyMatch(item -> item.getProduct() != null && !item.getProduct().isDigital());
    }

    public boolean requiresShipping() {
        return hasPhysicalItems();
    }

    public java.math.BigDecimal calculateTotal() {
        return subtotal.add(taxAmount).add(shippingAmount);
    }

    public void recalculateAmounts() {
        if (orderItems != null) {
            subtotal = orderItems.stream()
                    .map(OrderItem::getTotalPrice)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            totalAmount = calculateTotal();
        }
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }
}
