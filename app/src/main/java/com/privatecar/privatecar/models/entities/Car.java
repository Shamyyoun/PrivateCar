
package com.privatecar.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Car implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("license_no")
    @Expose
    private String licenseNo;
    @SerializedName("license_exp_date")
    @Expose
    private String licenseExpDate;
    @SerializedName("car_type")
    @Expose
    private String carType;
    @SerializedName("downgraded_to_economy")
    @Expose
    private int downgradedToEconomy;

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand The brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return The model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model The model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return The licenseNo
     */
    public String getLicenseNo() {
        return licenseNo;
    }

    /**
     * @param licenseNo The license_no
     */
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    /**
     * @return The licenseExpDate
     */
    public String getLicenseExpDate() {
        return licenseExpDate;
    }

    /**
     * @param licenseExpDate The license_exp_date
     */
    public void setLicenseExpDate(String licenseExpDate) {
        this.licenseExpDate = licenseExpDate;
    }

    /**
     * @return The carType
     */
    public String getCarType() {
        return carType;
    }

    /**
     * @param carType The car_type
     */
    public void setCarType(String carType) {
        this.carType = carType;
    }

    /**
     * @return The downgradedToEconomy
     */
    public int getDowngradedToEconomy() {
        return downgradedToEconomy;
    }

    /**
     * @param downgradedToEconomy The downgraded_to_economy
     */
    public void setDowngradedToEconomy(int downgradedToEconomy) {
        this.downgradedToEconomy = downgradedToEconomy;
    }

}
