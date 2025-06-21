package com.anterka.bjyotish.entities;

// ==============================================
// SYSTEM SETTING ENTITY
// ==============================================

import com.anterka.bjyotish.constants.DataTypeEnum;
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
@Table(name = "system_settings")
public class SystemSetting implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_setting_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "system_setting_sequence_generator", sequenceName = "seq_system_setting_id", allocationSize = 1)
    private Long id;

    @Column(name = "setting_key", nullable = false, unique = true, length = 100)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", length = 20)
    @Builder.Default
    private DataTypeEnum dataType = DataTypeEnum.STRING;

    @Column(name = "is_public")
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

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
    public boolean isPublic() {
        return isPublic != null && isPublic;
    }

    public void makePublic() {
        this.isPublic = true;
    }

    public void makePrivate() {
        this.isPublic = false;
    }

    public String getStringValue() {
        return settingValue;
    }

    public Integer getIntegerValue() {
        if (settingValue == null || settingValue.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(settingValue.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Long getLongValue() {
        if (settingValue == null || settingValue.trim().isEmpty()) return null;
        try {
            return Long.parseLong(settingValue.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Double getDoubleValue() {
        if (settingValue == null || settingValue.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(settingValue.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Boolean getBooleanValue() {
        if (settingValue == null || settingValue.trim().isEmpty()) return null;
        return Boolean.parseBoolean(settingValue.trim());
    }

    public java.math.BigDecimal getDecimalValue() {
        if (settingValue == null || settingValue.trim().isEmpty()) return null;
        try {
            return new java.math.BigDecimal(settingValue.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public java.time.LocalDate getDateValue() {
        if (settingValue == null || settingValue.trim().isEmpty()) return null;
        try {
            return java.time.LocalDate.parse(settingValue.trim());
        } catch (java.time.format.DateTimeParseException e) {
            return null;
        }
    }

    public java.time.LocalDateTime getDateTimeValue() {
        if (settingValue == null || settingValue.trim().isEmpty()) return null;
        try {
            return java.time.LocalDateTime.parse(settingValue.trim());
        } catch (java.time.format.DateTimeParseException e) {
            return null;
        }
    }

    public void setValue(Object value) {
        if (value == null) {
            this.settingValue = null;
            return;
        }
        this.settingValue = value.toString();
    }

    public void setStringValue(String value) {
        this.settingValue = value;
        this.dataType = DataTypeEnum.STRING;
    }

    public void setIntegerValue(Integer value) {
        this.settingValue = value != null ? value.toString() : null;
        this.dataType = DataTypeEnum.INTEGER;
    }

    public void setLongValue(Long value) {
        this.settingValue = value != null ? value.toString() : null;
        this.dataType = DataTypeEnum.LONG;
    }

    public void setDoubleValue(Double value) {
        this.settingValue = value != null ? value.toString() : null;
        this.dataType = DataTypeEnum.DOUBLE;
    }

    public void setBooleanValue(Boolean value) {
        this.settingValue = value != null ? value.toString() : null;
        this.dataType = DataTypeEnum.BOOLEAN;
    }

    public void setDecimalValue(java.math.BigDecimal value) {
        this.settingValue = value != null ? value.toString() : null;
        this.dataType = DataTypeEnum.DECIMAL;
    }

    public void setDateValue(java.time.LocalDate value) {
        this.settingValue = value != null ? value.toString() : null;
        this.dataType = DataTypeEnum.DATE;
    }

    public void setDateTimeValue(java.time.LocalDateTime value) {
        this.settingValue = value != null ? value.toString() : null;
        this.dataType = DataTypeEnum.DATETIME;
    }

    public Object getTypedValue() {
        return switch (dataType) {
            case STRING -> getStringValue();
            case INTEGER -> getIntegerValue();
            case LONG -> getLongValue();
            case DOUBLE -> getDoubleValue();
            case BOOLEAN -> getBooleanValue();
            case DECIMAL -> getDecimalValue();
            case DATE -> getDateValue();
            case DATETIME -> getDateTimeValue();
        };
    }

    public boolean hasValue() {
        return settingValue != null && !settingValue.trim().isEmpty();
    }

    public boolean isValidValue() {
        if (!hasValue()) return false;

        return switch (dataType) {
            case STRING -> true;
            case INTEGER -> getIntegerValue() != null;
            case LONG -> getLongValue() != null;
            case DOUBLE -> getDoubleValue() != null;
            case BOOLEAN -> getBooleanValue() != null;
            case DECIMAL -> getDecimalValue() != null;
            case DATE -> getDateValue() != null;
            case DATETIME -> getDateTimeValue() != null;
        };
    }

    public String getDataTypeDescription() {
        return switch (dataType) {
            case STRING -> "Text";
            case INTEGER -> "Integer Number";
            case LONG -> "Long Number";
            case DOUBLE -> "Decimal Number";
            case BOOLEAN -> "True/False";
            case DECIMAL -> "Precise Decimal";
            case DATE -> "Date";
            case DATETIME -> "Date and Time";
        };
    }

    public String getSettingSummary() {
        return String.format("%s (%s) = %s", settingKey, getDataTypeDescription(),
                hasValue() ? settingValue : "Not Set");
    }

    public boolean isRecentlyUpdated() {
        return updatedAt != null && updatedAt.isAfter(Instant.now().minusSeconds(3600)); // Last hour
    }

    public boolean isSystemCritical() {
        return settingKey != null && (
                settingKey.startsWith("system.") ||
                        settingKey.startsWith("security.") ||
                        settingKey.startsWith("database.") ||
                        settingKey.startsWith("payment.")
        );
    }
}
