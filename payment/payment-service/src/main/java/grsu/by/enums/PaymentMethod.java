package grsu.by.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("CASH"),
    CARD("CARD");

    private final String name;

    PaymentMethod(String name) {
        this.name = name;
    }
}
