package com.anterka.bjyotish.entities;

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
public class UserAddress implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private BjyotishUser bjyotishUser;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Builder.Default
    private Boolean isPrimary = false;

    @Builder.Default
    private Instant createdAt = Instant.now();

    protected void onCreate() {
        createdAt = Instant.now();
    }
}
