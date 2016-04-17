package com.privateegy.privatecar.models.enums;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum UserType {
    CUSTOMER(1), DRIVER(2);

    private int value;

    UserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}