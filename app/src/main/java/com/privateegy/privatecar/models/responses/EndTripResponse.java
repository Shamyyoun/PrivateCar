package com.privateegy.privatecar.models.responses;

import com.privateegy.privatecar.models.entities.EndTripResponseContent;

import java.util.ArrayList;

/**
 * Created by basim on 12/4/16.
 */
public class EndTripResponse {
    private boolean status;
    private EndTripResponseContent content;
    private ArrayList<String> validation;

    public boolean isSuccess() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public EndTripResponseContent getContent() {
        return content;
    }

    public void setContent(EndTripResponseContent content) {
        this.content = content;
    }

    public ArrayList<String> getValidation() {
        return validation;
    }

    public void setValidation(ArrayList<String> validation) {
        this.validation = validation;
    }
}
