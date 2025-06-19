package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.UserRoleEnum;
import com.anterka.bjyotish.constants.UserStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
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
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bjyotish_users")
public class BjyotishUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bjyotish_user_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "bjyotish_user_sequence_generator", sequenceName = "seq_bjyotish_user_id", allocationSize = 1)
    private Long id;

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Builder.Default
    private UserRoleEnum role = UserRoleEnum.CLIENT;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Builder.Default
    private UserStatusEnum status = UserStatusEnum.PENDING_VERIFICATION;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "email_verified")
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "phone_verified")
    @Builder.Default
    private Boolean phoneVerified = false;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "bjyotishUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "bjyotishUser", cascade = CascadeType.ALL, orphanRemoval = true)
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
        return status == UserStatusEnum.ACTIVE;
    }

    // Lifecycle callbacks for automatic timestamp updates
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
