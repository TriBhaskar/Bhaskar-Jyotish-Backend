package com.anterka.bjyotish.entities;

// ==============================================
// ORDER ITEM ENTITY
// ==============================================

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "order_item_sequence_generator", sequenceName = "seq_order_item_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal totalPrice;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        calculateTotalPrice();
    }

    @PreUpdate
    protected void onUpdate() {
        calculateTotalPrice();
    }

    // Utility methods
    public void calculateTotalPrice() {
        if (unitPrice != null && quantity != null) {
            totalPrice = unitPrice.multiply(java.math.BigDecimal.valueOf(quantity));
        }
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public void setUnitPrice(java.math.BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    public String getProductName() {
        return product != null ? product.getName() : "Unknown Product";
    }

    public String getOrderNumber() {
        return order != null ? order.getOrderNumber() : "Unknown Order";
    }

    public boolean isDigitalProduct() {
        return product != null && product.isDigital();
    }

    public boolean isPhysicalProduct() {
        return product != null && !product.isDigital();
    }

    public java.math.BigDecimal getSavingsAmount() {
        if (product != null && product.hasDiscount()) {
            return product.getDiscountAmount().multiply(java.math.BigDecimal.valueOf(quantity));
        }
        return java.math.BigDecimal.ZERO;
    }

    public java.math.BigDecimal getOriginalTotalPrice() {
        if (product != null) {
            return product.getPrice().multiply(java.math.BigDecimal.valueOf(quantity));
        }
        return totalPrice;
    }

    public boolean hasDiscount() {
        return product != null && product.hasDiscount();
    }

    public String getItemSummary() {
        return String.format("%s x %d = %s", getProductName(), quantity, totalPrice);
    }
}
