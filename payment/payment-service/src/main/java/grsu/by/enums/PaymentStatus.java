package grsu.by.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING_CONFIRMATION("PENDING_CONFIRMATION"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED"),
    ERROR("ERROR");

    private final String name;

    PaymentStatus(String name) {
        this.name = name;
    }
}
