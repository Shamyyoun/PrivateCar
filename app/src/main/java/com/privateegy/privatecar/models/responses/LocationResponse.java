package com.privateegy.privatecar.models.responses;

/**
 * Created by basim on 3/4/16.
 */
public class LocationResponse {
    private boolean status;
    private String content; //like "lat,lng"
    private String[] validation;

    public boolean isSuccess() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValidation() {
        return validation[0];
    }

    public void setValidation(String[] validation) {
        this.validation = validation;
    }
}
