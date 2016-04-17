
package com.privateegy.privatecar.models.payload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.TripInfo;

public class AcceptTripPayload {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("content")
    @Expose
    private TripInfo tripInfo;

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
     * @return The tripInfo
     */
    public TripInfo getTripInfo() {
        return tripInfo;
    }

    /**
     * @param tripInfo The tripInfo
     */
    public void setTripInfo(TripInfo tripInfo) {
        this.tripInfo = tripInfo;
    }

}
