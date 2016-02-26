package com.privatecar.privatecar.models.enums;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum PaymentType {
    CASH("1"), ACCOUNT_CREDIT("2");

    private String value;

    PaymentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
