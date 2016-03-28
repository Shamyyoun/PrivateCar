package com.privatecar.privatecar.models.entities;

/**
 * Created by Basim Alamuddin on 27/03/2016.
 */
public class DistanceMatrixElement {
    public MapsApiDistance distance;
    public MapsApiDuration duration;
    public String status;

    public MapsApiDistance getDistance() {
        return distance;
    }

    public void setDistance(MapsApiDistance distance) {
        this.distance = distance;
    }

    public MapsApiDuration getDuration() {
        return duration;
    }

    public void setDuration(MapsApiDuration duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOk() {
        return status.equals("OK");
    }

}
