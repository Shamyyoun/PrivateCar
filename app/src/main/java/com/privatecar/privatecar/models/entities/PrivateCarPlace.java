package com.privatecar.privatecar.models.entities;

import com.privatecar.privatecar.utils.Utils;

import java.io.Serializable;

/**
 * Created by Shamyyoun on 2/15/2016.
 */
public class PrivateCarPlace implements Serializable {
    private String name;
    private String address;
    private PrivateCarLocation location;
    private int time;
    private boolean markerLocation = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isMarkerLocation() {
        return markerLocation;
    }

    public void setMarkerLocation(boolean markerLocation) {
        this.markerLocation = markerLocation;
    }

    public PrivateCarLocation getLocation() {
        return location;
    }

    public void setLocation(PrivateCarLocation location) {
        this.location = location;
    }

    public String
    getFullAddress() {
        String fullAddress = name;
        if (!Utils.isNullOrEmpty(address)) {
            name += " - " + address;
        }
        return fullAddress;
    }
}
