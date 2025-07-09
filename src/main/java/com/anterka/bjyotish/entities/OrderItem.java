package com.anterka.bjyotish.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Order order;

    private Product product;

    @Builder.Default
    private Integer quantity = 1;

    private java.math.BigDecimal unitPrice;

    private java.math.BigDecimal totalPrice;

    @Builder.Default
    private Instant createdAt = Instant.now();

    protected void onCreate() {
        createdAt = Instant.now();
        calculateTotalPrice();
    }

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
