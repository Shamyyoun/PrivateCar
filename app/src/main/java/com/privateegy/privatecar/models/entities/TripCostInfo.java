
package com.privateegy.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TripCostInfo implements Serializable {

    @SerializedName("actual_fare")
    @Expose
    private float actualFare;
    @SerializedName("AvalCredit")
    @Expose
    private float AvalCredit;
    @SerializedName("CurrentCredit")
    @Expose
    private float CurrentCredit;

    /**
     * @return The actualFare
     */
    public float getActualFare() {
        return actualFare;
    }

    /**
     * @param actualFare The actual_fare
     */
    public void setActualFare(float actualFare) {
        this.actualFare = actualFare;
    }

    /**
     * @return The AvalCredit
     */
    public float getAvalCredit() {
        return AvalCredit;
    }

    /**
     * @param AvalCredit The AvalCredit
     */
    public void setAvalCredit(float AvalCredit) {
        this.AvalCredit = AvalCredit;
    }

    /**
     * @return The CurrentCredit
     */
    public float getCurrentCredit() {
        return CurrentCredit;
    }

    /**
     * @param CurrentCredit The CurrentCredit
     */
    public void setCurrentCredit(float CurrentCredit) {
        this.CurrentCredit = CurrentCredit;
    }

}
