package com.privatecar.privatecar.models.entities;

import java.io.Serializable;

/**
 * Created by basim on 26/3/16.
 */
public class PlaceGeometry implements Serializable {
    private PrivateCarLocation location;

    public PrivateCarLocation getLocation() {
        return location;
    }

    public void setLocation(PrivateCarLocation location) {
        this.location = location;
    }
}
