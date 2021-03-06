package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AccessTokenResponse {

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("expires_in")
    @Expose
    private int expiresIn; // in seconds

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("validation")
    @Expose
    private List<String> validation = new ArrayList<>();

    /**
     * @return The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken The access_token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return The expiresIn
     */
    public int getExpiresIn() {
        return expiresIn;
    }

    /**
     * @param expiresIn The expires_in
     */
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * @return The status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return The validation
     */
    public List<String> getValidation() {
        return validation;
    }

    /**
     * @param validation The validation
     */
    public void setValidation(List<String> validation) {
        this.validation = validation;
    }

}
