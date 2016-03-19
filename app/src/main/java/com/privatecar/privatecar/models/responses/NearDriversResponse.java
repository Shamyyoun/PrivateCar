package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.NearDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by basim on 19/3/16.
 */
public class NearDriversResponse {
    private boolean status;
    @SerializedName("content")
    private List<NearDriver> drivers = new ArrayList<>();

    public boolean isSuccess() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<NearDriver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<NearDriver> drivers) {
        this.drivers = drivers;
    }
}
