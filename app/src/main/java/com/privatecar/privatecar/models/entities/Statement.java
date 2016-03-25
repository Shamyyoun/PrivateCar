
package com.privatecar.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statement {

    @SerializedName("profit")
    @Expose
    private float profit;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("trip_id")
    @Expose
    private int tripId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    /**
     * @return The profit
     */
    public float getProfit() {
        return profit;
    }

    /**
     * @param profit The profit
     */
    public void setProfit(float profit) {
        this.profit = profit;
    }

    /**
     * @return The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return The tripId
     */
    public int getTripId() {
        return tripId;
    }

    /**
     * @param tripId The trip_id
     */
    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
