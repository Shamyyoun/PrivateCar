package com.privatecar.privatecar.models.entities;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by basim on 27/2/16.
 */
public class Location {
    private double lat;
    private double lng;

    public Location() {

    }

    public Location(android.location.Location location) {
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
    }

    public Location(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }


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
