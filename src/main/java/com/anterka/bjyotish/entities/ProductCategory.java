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
public class ProductCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private ProductCategory parentCategory;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Instant createdAt = Instant.now();

    private List<ProductCategory> subCategories;

    private List<Product> products;

    protected void onCreate() {
        createdAt = Instant.now();
    }

    // Utility methods
    public boolean isActive() {
        return isActive != null && isActive;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isRootCategory() {
        return parentCategory == null;
    }

    public boolean hasSubCategories() {
        return subCategories != null && !subCategories.isEmpty();
    }

    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }

    public int getSubCategoriesCount() {
        return subCategories != null ? subCategories.size() : 0;
    }

    public int getProductsCount() {
        return products != null ? products.size() : 0;
    }

    public String getFullPath() {
        StringBuilder path = new StringBuilder();
        if (parentCategory != null) {
            path.append(parentCategory.getFullPath()).append(" > ");
        }
        path.append(name);
        return path.toString();
    }
}
