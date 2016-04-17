package com.privateegy.privatecar.models.responses;

/**
 * Created by basim on 26/2/16.
 */
public class PromoCodeResponse {
    private boolean status;
    private String validation;

    public boolean isSuccess() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }
}
