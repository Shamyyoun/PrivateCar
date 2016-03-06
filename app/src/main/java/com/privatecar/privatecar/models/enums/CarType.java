package com.privatecar.privatecar.models.enums;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum CarType {
    ECONOMY("1"), BUSINESS("2");

    private String value;

    CarType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
