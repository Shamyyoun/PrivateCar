package com.privatecar.privatecar.models.entities;

/**
 * Created by basim on 21/1/16.
 */
public class Config {

    public static String CURRENT_APP_VERSION_KEY = "CurrentAppVersion";
    public static String CUSTOMER_SERVICE_NUMBER_KEY = "CustomerServiceNumber";
    public static String LOGIN_ENABLED_KEY = "LoginEnabled";
    public static String REGISTER_ENABLED_KEY = "RegisterEnabled";
    public static String MIN_APP_VERSION_KEY = "MinAppVersion";

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
