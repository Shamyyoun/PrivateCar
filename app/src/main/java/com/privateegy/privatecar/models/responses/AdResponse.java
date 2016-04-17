
package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.Ad;

public class AdResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private Ad ad;
    @SerializedName("validation")
    @Expose
    private Object validation;

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
     * @return The ad
     */
    public Ad getAd() {
        return ad;
    }

    /**
     * @param ad The ad
     */
    public void setAd(Ad ad) {
        this.ad = ad;
    }

    /**
     * @return The validation
     */
    public Object getValidation() {
        return validation;
    }

    /**
     * @param validation The validation
     */
    public void setValidation(Object validation) {
        this.validation = validation;
    }

}
