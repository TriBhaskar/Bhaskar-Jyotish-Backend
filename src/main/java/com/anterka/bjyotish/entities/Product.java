package com.anterka.bjyotish.entities;

// ==============================================
// PRODUCT ENTITY
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
@Table(name = "products")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "product_sequence_generator", sequenceName = "seq_product_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal price;

    @Column(name = "discount_price", precision = 10, scale = 2)
    private java.math.BigDecimal discountPrice;

    @Column(name = "images", columnDefinition = "TEXT[]")
    private String[] images;

    @Column(name = "is_digital")
    @Builder.Default
    private Boolean isDigital = false;

    @Column(name = "stock_quantity")
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
