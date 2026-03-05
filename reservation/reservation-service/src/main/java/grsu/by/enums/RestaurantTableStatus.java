package grsu.by.enums;

import lombok.Getter;

@Getter
public enum RestaurantTableStatus {
    AVAILABLE("AVAILABLE"),
    RESERVED("RESERVED"),
    OCCUPIED("OCCUPIED"),
    UNAVAILABLE("UNAVAILABLE");

    private final String name;

    RestaurantTableStatus(String name) {
        this.name = name;
    }
}
