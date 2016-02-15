package com.privatecar.privatecar.models.entities;

/**
 * Created by Shamyyoun on 2/15/2016.
 */
public class Place {
    private String title;
    private String address;
    private int time;
    private boolean myLocation;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isMyLocation() {
        return myLocation;
    }

    public void setMyLocation(boolean myLocation) {
        this.myLocation = myLocation;
    }
}
