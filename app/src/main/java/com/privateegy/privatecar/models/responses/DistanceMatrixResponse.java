package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.DistanceMatrixRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basim Alamuddin on 27/03/2016.
 */
public class DistanceMatrixResponse {
    @SerializedName("origin_addresses")
    public List<String> originAddresses = new ArrayList<>();
    @SerializedName("destination_addresses")
    public List<String> destinationAddresses = new ArrayList<>();
    public List<DistanceMatrixRow> rows = new ArrayList<>();
    public String status;

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public List<DistanceMatrixRow> getRows() {
        return rows;
    }

    public void setRows(List<DistanceMatrixRow> rows) {
        this.rows = rows;
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

