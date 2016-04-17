package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.NearbyPlace;
import com.privateegy.privatecar.models.entities.PrivateCarLocation;
import com.privateegy.privatecar.models.entities.PrivateCarPlace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by basim on 26/3/16.
 */
public class NearbyPlacesResponse implements Serializable {

    @SerializedName("results")
    private List<NearbyPlace> places;
    private String status;

    public List<NearbyPlace> getPlaces() {
        return places;
    }

    public List<PrivateCarPlace> getPrivateCarPlaces() {
        ArrayList<PrivateCarPlace> places = new ArrayList<>(getPlaces().size());

        if (isOk()) {
            int i = 0;
            for (NearbyPlace tmpPlace : getPlaces()) {
                if (i++ == 0) continue; //discard the first item
//                Utils.LogE(tmpPlace.getName() + ", " + tmpPlace.getVicinity());
                PrivateCarPlace place = new PrivateCarPlace();
                place.setName(tmpPlace.getName());
                place.setAddress(tmpPlace.getVicinity());
                place.setLocation(new PrivateCarLocation(tmpPlace.getGeometry().getLocation()));
                places.add(place);
            }
        }

        return places;
    }


    public void setPlaces(List<NearbyPlace> places) {
        this.places = places;
    }

    public String getStatus() {
        return status;
    }

    public boolean isOk() {
        return status.equals("OK");
    }
}
