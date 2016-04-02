
package com.privatecar.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fare {

    @SerializedName("open_fare")
    @Expose
    private float openFare;
    @SerializedName("kilometer_fare")
    @Expose
    private float kilometerFare;
    @SerializedName("minute_wait_fare")
    @Expose
    private float minuteWaitFare;
    @SerializedName("class")
    @Expose
    private int _class;
    @SerializedName("en_desc")
    @Expose
    private String enDesc;
    @SerializedName("ar_desc")
    @Expose
    private String arDesc;

    /**
     * @return The openFare
     */
    public float getOpenFare() {
        return openFare;
    }

    /**
     * @param openFare The open_fare
     */
    public void setOpenFare(float openFare) {
        this.openFare = openFare;
    }

    /**
     * @return The kilometerFare
     */
    public float getKilometerFare() {
        return kilometerFare;
    }

    /**
     * @param kilometerFare The kilometer_fare
     */
    public void setKilometerFare(float kilometerFare) {
        this.kilometerFare = kilometerFare;
    }

    /**
     * @return The minuteWaitFare
     */
    public float getMinuteWaitFare() {
        return minuteWaitFare;
    }

    /**
     * @param minuteWaitFare The minute_wait_fare
     */
    public void setMinuteWaitFare(float minuteWaitFare) {
        this.minuteWaitFare = minuteWaitFare;
    }

    /**
     * @return The _class
     */
    public int getClass_() {
        return _class;
    }

    /**
     * @param _class The class
     */
    public void setClass_(int _class) {
        this._class = _class;
    }

    /**
     * @return The enDesc
     */
    public String getEnDesc() {
        return enDesc;
    }

    /**
     * @param enDesc The en_desc
     */
    public void setEnDesc(String enDesc) {
        this.enDesc = enDesc;
    }

    /**
     * @return The arDesc
     */
    public String getArDesc() {
        return arDesc;
    }

    /**
     * @param arDesc The ar_desc
     */
    public void setArDesc(String arDesc) {
        this.arDesc = arDesc;
    }

}
