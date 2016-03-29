
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.CustomerTripRequest;

import java.util.ArrayList;
import java.util.List;

public class TripRequestResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private CustomerTripRequest tripRequest;
    @SerializedName("validation")
    @Expose
    private List<String> validation = new ArrayList<String>();

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
     * @return The tripRequest
     */
    public CustomerTripRequest getTripRequest() {
        return tripRequest;
    }

    /**
     * @param tripRequest The tripRequest
     */
    public void setTripRequest(CustomerTripRequest tripRequest) {
        this.tripRequest = tripRequest;
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
