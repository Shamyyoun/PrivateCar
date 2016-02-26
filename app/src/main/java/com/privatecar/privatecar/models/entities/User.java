package com.privatecar.privatecar.models.entities;

import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.responses.AccountDetailsResponse;

/**
 * Created by basim on 22/1/16.
 * *
 */
public class User {
    private String accessToken;
    private UserType type;
    private AccountDetailsResponse accountDetails;

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

    public AccountDetailsResponse getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetailsResponse accountDetails) {
        this.accountDetails = accountDetails;
    }
}
