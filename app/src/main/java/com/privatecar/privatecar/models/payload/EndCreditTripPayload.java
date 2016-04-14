
package com.privatecar.privatecar.models.payload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.TripCostInfo;

public class EndCreditTripPayload {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("content")
    @Expose
    private TripCostInfo tripCostInfo;

    /**
     * 
     * @return
     *     The key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *     The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @return
     *     The tripCostInfo
     */
    public TripCostInfo getTripCostInfo() {
        return tripCostInfo;
    }

    /**
     * 
     * @param tripCostInfo
     *     The tripCostInfo
     */
    public void setTripCostInfo(TripCostInfo tripCostInfo) {
        this.tripCostInfo = tripCostInfo;
    }

}
