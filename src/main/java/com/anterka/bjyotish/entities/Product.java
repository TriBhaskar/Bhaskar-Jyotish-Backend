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
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private ProductCategory category;

    private String name;

    private String description;

    private java.math.BigDecimal price;

    private java.math.BigDecimal discountPrice;

    private String[] images;

    @Builder.Default
    private Boolean isDigital = false;

    @Builder.Default
    private Integer stockQuantity = 0;

    @Builder.Default
    private Boolean isActive = true;

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
    public boolean isActive() {
        return isActive != null && isActive;
    }

    public boolean isDigital() {
        return isDigital != null && isDigital;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isInStock() {
        return isDigital() || (stockQuantity != null && stockQuantity > 0);
    }

    public boolean isOutOfStock() {
        return !isDigital() && (stockQuantity == null || stockQuantity <= 0);
    }

    public boolean hasDiscount() {
        return discountPrice != null && discountPrice.compareTo(java.math.BigDecimal.ZERO) > 0;
    }

    public java.math.BigDecimal getEffectivePrice() {
        return hasDiscount() ? discountPrice : price;
    }

    public java.math.BigDecimal getDiscountAmount() {
        return hasDiscount() ? price.subtract(discountPrice) : java.math.BigDecimal.ZERO;
    }

    public double getDiscountPercentage() {
        if (!hasDiscount() || price.compareTo(java.math.BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return getDiscountAmount().divide(price, 4, java.math.RoundingMode.HALF_UP)
                .multiply(java.math.BigDecimal.valueOf(100)).doubleValue();
    }

    public boolean hasImages() {
        return images != null && images.length > 0;
    }

    public String getPrimaryImage() {
        return hasImages() ? images[0] : null;
    }

    public String getCategoryName() {
        return category != null ? category.getName() : "Uncategorized";
    }

    public boolean canPurchase(int quantity) {
        if (!isActive()) return false;
        if (isDigital()) return true;
        return stockQuantity != null && stockQuantity >= quantity;
    }

    public void reduceStock(int quantity) {
        if (!isDigital() && stockQuantity != null) {
            stockQuantity = Math.max(0, stockQuantity - quantity);
        }
    }

    public void addStock(int quantity) {
        if (!isDigital()) {
            stockQuantity = (stockQuantity != null ? stockQuantity : 0) + quantity;
        }
    }
}
