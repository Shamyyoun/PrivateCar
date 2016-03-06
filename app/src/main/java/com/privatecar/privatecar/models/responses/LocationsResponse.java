package com.privatecar.privatecar.models.responses;

import com.privatecar.privatecar.models.entities.Location;

import java.util.ArrayList;

/**
 * Created by basim on 27/2/16.
 */

public class LocationsResponse {
    private boolean status;
    private ArrayList<Location> content;
    private ArrayList<String> validation;

    public boolean isSuccess() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<String> getValidation() {
        return validation;
    }

    public void setValidation(ArrayList<String> validation) {
        this.validation = validation;
    }

    public ArrayList<Location> getContent() {
        return content;
    }

    public void setContent(ArrayList<Location> content) {
        this.content = content;
    }
}

