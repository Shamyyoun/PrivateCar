
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CustomerTripsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<CustomerTrip> trips = new ArrayList<CustomerTrip>();
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
     * @return The trips
     */
    public List<CustomerTrip> getTrips() {
        return trips;
    }

    /**
     * @param trips The trips
     */
    public void setTrips(List<CustomerTrip> trips) {
        this.trips = trips;
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
