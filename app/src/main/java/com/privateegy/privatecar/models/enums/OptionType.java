package com.privateegy.privatecar.models.enums;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum OptionType {
    CUSTOMER_RATING("1"), DRIVER_RATING("2"), DECLINE_TRIP("3");

    private String value;

    OptionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
