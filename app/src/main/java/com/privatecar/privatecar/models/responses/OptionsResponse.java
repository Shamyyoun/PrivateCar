
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<Option> options = new ArrayList<Option>();
    @SerializedName("validation")
    @Expose
    private List<String> validation = new ArrayList<>();

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
     * @return The options
     */
    public List<Option> getOptions() {
        return options;
    }

    /**
     * @param options The options
     */
    public void setOptions(List<Option> options) {
        this.options = options;
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
