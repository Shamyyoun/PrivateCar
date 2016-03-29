package com.privatecar.privatecar.models.entities;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by basim on 17/2/16.
 */
public class StatementsGroup implements Serializable {
    private Calendar date;
    private int tripCount;
    private float profit;

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
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
