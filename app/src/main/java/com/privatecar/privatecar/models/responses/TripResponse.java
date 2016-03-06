
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private Trip trip;
    @SerializedName("validation")
    @Expose
    private List<String> validation = new ArrayList<>();

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
     * @return The trip
     */
    public Trip getTrip() {
        return trip;
    }

    /**
     * @param trip The trip
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
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
