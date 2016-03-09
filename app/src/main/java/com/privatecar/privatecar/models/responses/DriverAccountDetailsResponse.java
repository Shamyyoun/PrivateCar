
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.DriverAccountDetails;

public class DriverAccountDetailsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private DriverAccountDetails accountDetails;
    @SerializedName("validation")
    @Expose
    private Object validation;

    /**
     * @return The status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return The accountDetails
     */
    public DriverAccountDetails getAccountDetails() {
        return accountDetails;
    }

    /**
     * @param accountDetails The accountDetails
     */
    public void setAccountDetails(DriverAccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    /**
     * @return The validation
     */
    public Object getValidation() {
        return validation;
    }

    /**
     * @param validation The validation
     */
    public void setValidation(Object validation) {
        this.validation = validation;
    }

}
