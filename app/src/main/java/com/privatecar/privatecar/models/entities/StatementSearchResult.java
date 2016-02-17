package com.privatecar.privatecar.models.entities;

import java.util.Date;

/**
 * Created by basim on 17/2/16.
 */
public class StatementSearchResult {
    private Date date;
    private int tripCount;
    private float profit;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTripCount() {
        return tripCount;
    }

    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }
}
