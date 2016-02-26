
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.Config;

import java.util.ArrayList;
import java.util.List;

public class ConfigResponse {
    public static final String KEY_CURRENT_APP_VERSION = "CurrentAppVersion";
    public static final String KEY_CUSTOMER_SERVICE_NUMBER = "CustomerServiceNumber";
    public static final String KEY_LOGIN_ENABLED = "LoginEnabled";
    public static final String KEY_MIN_APP_VERSION = "MinAppVersion";
    public static final String KEY_REGISTER_ENABLED = "RegisterEnabled";

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
