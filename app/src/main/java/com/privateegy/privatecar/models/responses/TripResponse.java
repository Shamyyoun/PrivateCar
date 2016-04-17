
package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.DriverTrip;

import java.util.ArrayList;
import java.util.List;

public class TripResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private DriverTrip driverTrip;
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
     * @return The driverTrip
     */
    public DriverTrip getDriverTrip() {
        return driverTrip;
    }

    /**
     * @param driverTrip The driverTrip
     */
    public void setDriverTrip(DriverTrip driverTrip) {
        this.driverTrip = driverTrip;
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
