package com.privatecar.privatecar.models.entities;

import com.privatecar.privatecar.models.enums.UserType;

/**
 * Created by basim on 22/1/16.
 * *
 */
public class User {
    private String accessToken;
    private long expiryTimestamp; // expiry timestamp
    private String userName;
    private String password;
    private UserType type;
    private DriverAccountDetails accountDetails;
    private boolean online;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public DriverAccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(DriverAccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

}
