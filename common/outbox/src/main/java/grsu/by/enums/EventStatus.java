package grsu.by.enums;

import lombok.Getter;

@Getter
public enum EventStatus {
    NEW("NEW"),
    SUCCESS("SUCCESS"),
    ERROR("ERROR");

    private final String name;

    EventStatus(String name) {
        this.name = name;
    }

}
