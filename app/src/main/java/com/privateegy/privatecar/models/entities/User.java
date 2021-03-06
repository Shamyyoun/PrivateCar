package com.privateegy.privatecar.models.entities;

import com.privateegy.privatecar.models.enums.UserType;

/**
 * Created by basim on 22/1/16.
 * *
 */
public class User {
    private String accessToken;
    private long expiryTimestamp; // expiry timestamp
    private String userName;
    private String password;
    private String socialUserId;
    private String socialToken;
    private String socialProvider;
    private UserType type;
    private DriverAccountDetails driverAccountDetails;
    private CustomerAccountDetails customerAccountDetails;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSocialUserId() {
        return socialUserId;
    }

    public void setSocialUserId(String socialUserId) {
        this.socialUserId = socialUserId;
    }

    public String getSocialToken() {
        return socialToken;
    }

    public void setSocialToken(String socialToken) {
        this.socialToken = socialToken;
    }

    public String getSocialProvider() {
        return socialProvider;
    }

    public void setSocialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public DriverAccountDetails getDriverAccountDetails() {
        return driverAccountDetails;
    }

    public void setDriverAccountDetails(DriverAccountDetails driverAccountDetails) {
        this.driverAccountDetails = driverAccountDetails;
    }

    public CustomerAccountDetails getCustomerAccountDetails() {
        return customerAccountDetails;
    }

    public void setCustomerAccountDetails(CustomerAccountDetails customerAccountDetails) {
        this.customerAccountDetails = customerAccountDetails;
    }
}
