package com.app.middleware.persistence.type;

public enum PushConfig {
    SOUND("default"),
    COLOR("#FFFF00");

    private String value;

    PushConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
