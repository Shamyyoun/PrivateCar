package com.privateegy.privatecar.models.enums;

import java.io.Serializable;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum CarType implements Serializable {
    ECONOMY("1"), BUSINESS("2");

    private String value;

    CarType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
