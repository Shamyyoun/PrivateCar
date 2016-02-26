
package com.privatecar.privatecar.models.payload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.TripRequest;

public class TripRequestPayload {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("content")
    @Expose
    private TripRequest tripRequest;

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
    public TripRequest getTripRequest() {
        return tripRequest;
    }

    /**
     * @param tripRequest The tripRequest
     */
    public void setTripRequest(TripRequest tripRequest) {
        this.tripRequest = tripRequest;
    }

}
