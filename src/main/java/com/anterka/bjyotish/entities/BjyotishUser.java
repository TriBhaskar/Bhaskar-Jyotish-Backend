package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import com.anterka.bjyotish.constants.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Main Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BjyotishUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String email;

    private String phone;

    private String passwordHash;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    @Builder.Default
    private UserRoleEnum role = UserRoleEnum.CLIENT;

    @Builder.Default
    private UserStatusEnum status = UserStatusEnum.PENDING_VERIFICATION;

    private String profileImageUrl;

    @Builder.Default
    private Boolean emailVerified = false;

    @Builder.Default
    private Boolean phoneVerified = false;

    private Instant lastLoginAt;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    private List<UserAddress> addresses = new ArrayList<>();

    private List<BirthDetails> birthDetails = new ArrayList<>();

    // Helper methods for managing relationships
    public void addAddress(UserAddress address) {
        addresses.add(address);
        address.setBjyotishUser(this);
    }

    public void removeAddress(UserAddress address) {
        addresses.remove(address);
        address.setBjyotishUser(null);
    }

    public void addBirthDetails(BirthDetails birthDetails) {
        this.birthDetails.add(birthDetails);
        birthDetails.setBjyotishUser(this);
    }

    public void removeBirthDetails(BirthDetails birthDetails) {
        this.birthDetails.remove(birthDetails);
        birthDetails.setBjyotishUser(null);
    }

    // UserDetails implementation methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatusEnum.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatusEnum.ACTIVE || status == UserStatusEnum.PENDING_VERIFICATION;
    }

    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
