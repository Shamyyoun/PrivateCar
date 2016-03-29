package com.privatecar.privatecar.models.responses;

import com.privatecar.privatecar.models.entities.MapsApiRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basim Alamuddin on 27/03/2016.
 */
public class DirectionsApiResponse {
    public List<MapsApiRoute> routes = new ArrayList<>();
    public String status;

    public boolean isOk() {
        return status.equals("OK");
    }
}
