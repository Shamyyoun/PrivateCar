
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<Trip> trips = new ArrayList<>();
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
     * @return The trips
     */
    public List<Trip> getTrips() {
        return trips;
    }

    /**
     * @param trips The trips
     */
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
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
