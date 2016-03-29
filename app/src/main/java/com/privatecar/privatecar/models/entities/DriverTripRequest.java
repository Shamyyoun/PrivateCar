
package com.privatecar.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverTripRequest implements Serializable {

    @SerializedName("Id")
    @Expose
    private int Id;
    @SerializedName("Code")
    @Expose
    private int Code;
    @SerializedName("Customer")
    @Expose
    private String Customer;
    @SerializedName("CustomerRating")
    @Expose
    private float CustomerRating;
    @SerializedName("CustomerImage")
    @Expose
    private String CustomerImage;
    @SerializedName("Mobile")
    @Expose
    private String Mobile;
    @SerializedName("PickAddress")
    @Expose
    private String PickAddress;
    @SerializedName("DestinationAddress")
    @Expose
    private String DestinationAddress;
    @SerializedName("PickLocation")
    @Expose
    private String PickLocation;
    @SerializedName("DestinationLocation")
    @Expose
    private String DestinationLocation;
    @SerializedName("Notes")
    @Expose
    private String Notes;
    @SerializedName("PickNotes")
    @Expose
    private String PickNotes;
    @SerializedName("DestNotes")
    @Expose
    private String DestNotes;
    @SerializedName("PaymentType")
    @Expose
    private String PaymentType;

    /**
     * @return The Id
     */
    public int getId() {
        return Id;
    }

    /**
     * @param Id The Id
     */
    public void setId(int Id) {
        this.Id = Id;
    }

    /**
     * @return The Code
     */
    public int getCode() {
        return Code;
    }

    /**
     * @param Code The Code
     */
    public void setCode(int Code) {
        this.Code = Code;
    }

    /**
     * @return The Customer
     */
    public String getCustomer() {
        return Customer;
    }

    /**
     * @param Customer The Customer
     */
    public void setCustomer(String Customer) {
        this.Customer = Customer;
    }

    /**
     * @param CustomerRating The Customer Rating
     */
    public void setCustomerRating(float CustomerRating) {
        this.CustomerRating = CustomerRating;
    }

    /**
     * @return The Customer Rating
     */
    public float getCustomerRating() {
        return CustomerRating;
    }

    /**
     * @return The Customer Image
     */
    public String getCustomerImage() {
        return CustomerImage;
    }

    /**
     * @param CustomerImage The CustomerImage
     */
    public void setCustomerImage(String CustomerImage) {
        this.CustomerImage = CustomerImage;
    }

    /**
     * @return The Mobile
     */
    public String getMobile() {
        return Mobile;
    }

    /**
     * @param Mobile The Mobile
     */
    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    /**
     * @return The PickAddress
     */
    public String getPickAddress() {
        return PickAddress;
    }

    /**
     * @param PickAddress The PickAddress
     */
    public void setPickAddress(String PickAddress) {
        this.PickAddress = PickAddress;
    }

    /**
     * @return The DestinationAddress
     */
    public String getDestinationAddress() {
        return DestinationAddress;
    }

    /**
     * @param DestinationAddress The DestinationAddress
     */
    public void setDestinationAddress(String DestinationAddress) {
        this.DestinationAddress = DestinationAddress;
    }

    /**
     * @return The PickLocation
     */
    public String getPickLocation() {
        return PickLocation;
    }

    /**
     * @param PickLocation The PickLocation
     */
    public void setPickLocation(String PickLocation) {
        this.PickLocation = PickLocation;
    }

    /**
     * @return The DestinationLocation
     */
    public String getDestinationLocation() {
        return DestinationLocation;
    }

    /**
     * @param DestinationLocation The DestinationLocation
     */
    public void setDestinationLocation(String DestinationLocation) {
        this.DestinationLocation = DestinationLocation;
    }

    /**
     * @return The Notes
     */
    public String getNotes() {
        return Notes;
    }

    /**
     * @param Notes The Notes
     */
    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    /**
     * @return The PickNotes
     */
    public String getPickNotes() {
        return PickNotes;
    }

    /**
     * @param PickNotes The PickNotes
     */
    public void setPickNotes(String PickNotes) {
        this.PickNotes = PickNotes;
    }

    /**
     * @return The DestNotes
     */
    public String getDestNotes() {
        return DestNotes;
    }

    /**
     * @param DestNotes The DestNotes
     */
    public void setDestNotes(String DestNotes) {
        this.DestNotes = DestNotes;
    }

    /**
     * @return The PaymentType
     */
    public String getPaymentType() {
        return PaymentType;
    }

    /**
     * @param PaymentType The PaymentType
     */
    public void setPaymentType(String PaymentType) {
        this.PaymentType = PaymentType;
    }
}
