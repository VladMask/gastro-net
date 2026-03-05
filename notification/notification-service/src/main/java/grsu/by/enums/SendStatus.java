package grsu.by.enums;

import lombok.Getter;

@Getter
public enum SendStatus {
    NEW("NEW"),
    SUCCESS("SUCCESS"),
    ERROR("ERROR");

    private final String name;

    SendStatus(String name) {
        this.name = name;
    }
}
