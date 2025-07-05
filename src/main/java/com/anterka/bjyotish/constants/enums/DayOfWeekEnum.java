package com.anterka.bjyotish.constants.enums;

public enum DayOfWeekEnum {
    SUNDAY(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3),
    THURSDAY(4), FRIDAY(5), SATURDAY(6);

    private final int value;

    DayOfWeekEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DayOfWeekEnum fromValue(int value) {
        for (DayOfWeekEnum day : DayOfWeekEnum.values()) {
            if (day.value == value) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid day of week value: " + value);
    }
}
