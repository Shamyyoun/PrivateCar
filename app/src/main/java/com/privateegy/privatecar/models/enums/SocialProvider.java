package com.privateegy.privatecar.models.enums;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public enum SocialProvider {
    FACEBOOK("facebook"), GPLUS("google");

    private String value;

    SocialProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
