package com.privatecar.privatecar.models.entities;

/**
 * Created by Basim Alamuddin on 27/03/2016.
 */
public class MapsApiDistance {
    public String text;
    public Integer value; // in meters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
