
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerTrip {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("pickupaddress")
    @Expose
    private String pickupaddress;
    @SerializedName("destinationaddress")
    @Expose
    private String destinationaddress;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("fare")
    @Expose
    private float fare;
    @SerializedName("distance")
    @Expose
    private float distance;
    @SerializedName("time")
    @Expose
    private int time;

    /**
     * @return The code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return The pickupaddress
     */
    public String getPickupaddress() {
        return pickupaddress;
    }

    /**
     * @param pickupaddress The pickupaddress
     */
    public void setPickupaddress(String pickupaddress) {
        this.pickupaddress = pickupaddress;
    }

    /**
     * @return The destinationaddress
     */
    public String getDestinationaddress() {
        return destinationaddress;
    }

    /**
     * @param destinationaddress The destinationaddress
     */
    public void setDestinationaddress(String destinationaddress) {
        this.destinationaddress = destinationaddress;
    }

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return The fare
     */
    public float getFare() {
        return fare;
    }

    /**
     * @param fare The fare
     */
    public void setFare(float fare) {
        this.fare = fare;
    }

    /**
     * @return The distance
     */
    public float getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }

    /**
     * @return The time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(int time) {
        this.time = time;
    }

}
