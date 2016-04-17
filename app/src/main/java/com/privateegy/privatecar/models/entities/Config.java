
package com.privateegy.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {

    public static final String KEY_CURRENT_APP_VERSION = "CurrentAppVersion";
    public static final String KEY_MIN_APP_VERSION = "MinAppVersion";
    public static final String KEY_CUSTOMER_SERVICE_NUMBER = "CustomerServiceNumber";
    public static final String KEY_DRIVER_SERVICE_NUMBER = "DriverServiceNumber";
    public static final String KEY_LOGIN_ENABLED = "LoginEnabled";
    public static final String KEY_REGISTER_ENABLED = "RegisterEnabled";
    public static final String KEY_LOW_CUSTOMER_TRIP_RATE = "LowCustomerTripRate";
    public static final String KEY_LOW_DRIVER_TRIP_RATE = "LowDriverTripRate";
    public static final String KEY_MAP_REFRESH_RATE = "MapRefereshRate"; // in sec
    public static final String KEY_MIN_TRIP_FARE = "MinTripFare";
    public static final String KEY_WAITING_TIME_SPEED = "WaitingTimeSpeed";
    public static final String KEY_BASE_URL = "BaseURL";
    public static final String KEY_WEBSITE_URL = "WebSiteURL";
    public static final String KEY_INVITE_TEMPLATE = "InviteTemplate";
    public static final String KEY_STOP_TRIP = "StopTripRquest";


    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("value")
    @Expose
    private String value;

    /**
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(String value) {
        this.value = value;
    }

}
