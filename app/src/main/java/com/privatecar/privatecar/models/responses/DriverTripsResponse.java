
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.DriverTrip;

import java.util.ArrayList;
import java.util.List;

public class DriverTripsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<DriverTrip> driverTrips = new ArrayList<>();
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
     * @return The driverTrips
     */
    public List<DriverTrip> getDriverTrips() {
        return driverTrips;
    }

    /**
     * @param driverTrips The driverTrips
     */
    public void setDriverTrips(List<DriverTrip> driverTrips) {
        this.driverTrips = driverTrips;
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
