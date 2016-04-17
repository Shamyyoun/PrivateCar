package com.privateegy.privatecar.models.enums;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum Language {
    ARABIC("ar"), ENGLISH("en");

    private String value;

    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
