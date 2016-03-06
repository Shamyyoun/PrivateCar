package com.privatecar.privatecar.models.entities;

import java.util.Locale;

/**
 * Created by basim on 27/2/16.
 */
public class Location {
    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%f,%f", getLat(), getLng());
    }
}
