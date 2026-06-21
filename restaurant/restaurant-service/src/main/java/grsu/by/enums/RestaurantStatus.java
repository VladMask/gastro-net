package grsu.by.enums;

import lombok.Getter;

@Getter
public enum RestaurantStatus {
    PENDING_ACTIVATION("PENDING_ACTIVATION"),
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    REJECTED("REJECTED");

    private final String name;

    RestaurantStatus(String name) {
        this.name = name;
    }
}