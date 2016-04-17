
package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.Config;

import java.util.ArrayList;
import java.util.List;

public class ConfigResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<Config> configs = new ArrayList<Config>();
    @SerializedName("validation")
    @Expose
    private Object validation;

    /**
     * 
     * @return
     *     The status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The configs
     */
    public List<Config> getConfigs() {
        return configs;
    }

    /**
     * 
     * @param configs
     *     The configs
     */
    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    /**
     * 
     * @return
     *     The validation
     */
    public Object getValidation() {
        return validation;
    }

    /**
     * 
     * @param validation
     *     The validation
     */
    public void setValidation(Object validation) {
        this.validation = validation;
    }

}
