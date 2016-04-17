
package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.Fare;

import java.util.ArrayList;
import java.util.List;

public class FaresResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<Fare> fares = new ArrayList<Fare>();
    @SerializedName("validation")
    @Expose
    private List<String> validation = new ArrayList<String>();

    /**
     * @return The status
     */
    public boolean isSuccess() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return The fares
     */
    public List<Fare> getFares() {
        return fares;
    }

    /**
     * @param fares The fares
     */
    public void setFares(List<Fare> fares) {
        this.fares = fares;
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
