
package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.Ad;

import java.util.ArrayList;
import java.util.List;

public class AdsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<Ad> ads = new ArrayList<Ad>();
    @SerializedName("validation")
    @Expose
    private List<String> validation;

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
     * @return The ads
     */
    public List<Ad> getAds() {
        return ads;
    }

    /**
     * @param ads The ads
     */
    public void setAds(List<Ad> ads) {
        this.ads = ads;
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
