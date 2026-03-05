package grsu.by.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED"),
    CONFIRMED("CONFIRMED"),
    IN_PREPARATION("IN_PREPARATION"),
    READY("READY"),
    CANCELLED("CANCELLED"),
    COMPLETED("COMPLETED");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }
}
