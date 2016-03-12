
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.CustomerAccountDetails;

import java.util.List;

public class CustomerAccountDetailsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private CustomerAccountDetails accountDetails;
    @SerializedName("validation")
    @Expose
    private List<String> validation;

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
    public CustomerAccountDetails getAccountDetails() {
        return accountDetails;
    }

    /**
     * @param accountDetails The accountDetails
     */
    public void setAccountDetails(CustomerAccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    /**
     * @return The validation
     */
    public List<String> getValidation() {
        return validation;
    }

    /**
     * @param validation The validation
     */
    public void setValidation(List<String> validation) {
        this.validation = validation;
    }

}
