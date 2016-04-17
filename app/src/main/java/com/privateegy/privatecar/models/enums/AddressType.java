package com.privateegy.privatecar.models.enums;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum AddressType {
    ADDRESS("1"), LEAD_DRIVER("2");

    private String value;

    AddressType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
