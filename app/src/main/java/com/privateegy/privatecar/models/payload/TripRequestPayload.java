
package com.privateegy.privatecar.models.payload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.DriverTripRequest;

public class TripRequestPayload {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("content")
    @Expose
    private DriverTripRequest tripRequest;

    /**
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The tripRequest
     */
    public DriverTripRequest getTripRequest() {
        return tripRequest;
    }

    /**
     * @param tripRequest The tripRequest
     */
    public void setTripRequest(DriverTripRequest tripRequest) {
        this.tripRequest = tripRequest;
    }

}
