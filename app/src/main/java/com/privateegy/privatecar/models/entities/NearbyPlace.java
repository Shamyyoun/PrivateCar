package com.privateegy.privatecar.models.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by basim on 26/3/16.
 */
public class NearbyPlace implements Serializable {
    @SerializedName("place_id")
    private String placeId;
    private String name;
    private String vicinity;
    private PlaceGeometry geometry;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public PlaceGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(PlaceGeometry geometry) {
        this.geometry = geometry;
    }
}
