package com.anterka.bjyotish.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private BjyotishUser bjyotishUser;

    private String orderNumber;

    private java.math.BigDecimal subtotal;

    @Builder.Default
    private java.math.BigDecimal taxAmount = java.math.BigDecimal.ZERO;

    @Builder.Default
    private java.math.BigDecimal shippingAmount = java.math.BigDecimal.ZERO;

    private java.math.BigDecimal totalAmount;

    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    private String shippingAddress; // JSON string

    private String billingAddress; // JSON string

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    private List<OrderItem> orderItems;

    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

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
