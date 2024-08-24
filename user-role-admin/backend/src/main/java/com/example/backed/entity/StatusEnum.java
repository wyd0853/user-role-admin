package com.example.backed.entity;

public enum StatusEnum {
    INACTIVE(0), // 对应0值
    ACTIVE(1);   // 对应1值

    private final int value;

    StatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StatusEnum fromValue(int value) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
