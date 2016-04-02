package com.privatecar.privatecar.models.responses;

import java.util.ArrayList;

/**
 * Created by basim on 26/2/16.
 */
public class GeneralResponse {
    private boolean status;
    private ArrayList<String> validation = new ArrayList<>();

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
}
