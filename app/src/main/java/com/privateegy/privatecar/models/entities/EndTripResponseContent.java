package com.privateegy.privatecar.models.entities;

/**
 * Created by basim on 12/4/16.
 */
public class EndTripResponseContent {
    private String key; //trip_pay_remaining, end_trip_cash
    private float content;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getContent() {
        return content;
    }

    public void setContent(float content) {
        this.content = content;
    }
}
