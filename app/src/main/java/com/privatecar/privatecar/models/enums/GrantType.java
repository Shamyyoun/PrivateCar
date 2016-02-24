package com.privatecar.privatecar.models.enums;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum GrantType {
    PASSWORD("password"), SOCIAL("social");

    private String value;

    GrantType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
