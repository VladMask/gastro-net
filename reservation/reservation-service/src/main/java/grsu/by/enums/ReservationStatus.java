package grsu.by.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    CREATED("CREATED"),
    CONFIRMED("CONFIRMED"),
    EXPIRED("EXPIRED"),
    CANCELLED("CANCELLED");

    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }
}
