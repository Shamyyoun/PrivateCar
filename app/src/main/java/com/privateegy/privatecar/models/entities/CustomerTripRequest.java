package com.privateegy.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerTripRequest {

    @SerializedName("customer_id")
    @Expose
    private int customerId;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("pickup_location")
    @Expose
    private String pickupLocation;
    @SerializedName("pickup_now")
    @Expose
    private String pickupNow;
    @SerializedName("address_type")
    @Expose
    private String addressType;
    @SerializedName("notes_for_driver")
    @Expose
    private String notesForDriver;
    @SerializedName("pickup_address_notes")
    @Expose
    private String pickupAddressNotes;
    @SerializedName("will_lead_the_driver")
    @Expose
    private int willLeadTheDriver;
    @SerializedName("destination_address")
    @Expose
    private String destinationAddress;
    @SerializedName("destination_address_notes")
    @Expose
    private String destinationAddressNotes;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("code")
    @Expose
    private String code;

    /**
     * @return The customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId The customer_id
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return The pickupAddress
     */
    public String getPickupAddress() {
        return pickupAddress;
    }

    /**
     * @param pickupAddress The pickup_address
     */
    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    /**
     * @return The serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType The service_type
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return The paymentType
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType The payment_type
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return The status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return The pickupLocation
     */
    public String getPickupLocation() {
        return pickupLocation;
    }

    /**
     * @param pickupLocation The pickup_location
     */
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * @return The pickupNow
     */
    public String getPickupNow() {
        return pickupNow;
    }

    /**
     * @param pickupNow The pickup_now
     */
    public void setPickupNow(String pickupNow) {
        this.pickupNow = pickupNow;
    }

    /**
     * @return The addressType
     */
    public String getAddressType() {
        return addressType;
    }

    /**
     * @param addressType The address_type
     */
    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    /**
     * @return The notesForDriver
     */
    public String getNotesForDriver() {
        return notesForDriver;
    }

    /**
     * @param notesForDriver The notes_for_driver
     */
    public void setNotesForDriver(String notesForDriver) {
        this.notesForDriver = notesForDriver;
    }

    /**
     * @return The pickupAddressNotes
     */
    public String getPickupAddressNotes() {
        return pickupAddressNotes;
    }

    /**
     * @param pickupAddressNotes The pickup_address_notes
     */
    public void setPickupAddressNotes(String pickupAddressNotes) {
        this.pickupAddressNotes = pickupAddressNotes;
    }

    /**
     * @return The willLeadTheDriver
     */
    public int getWillLeadTheDriver() {
        return willLeadTheDriver;
    }

    /**
     * @param willLeadTheDriver The will_lead_the_driver
     */
    public void setWillLeadTheDriver(int willLeadTheDriver) {
        this.willLeadTheDriver = willLeadTheDriver;
    }

    /**
     * @return The destinationAddress
     */
    public String getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param destinationAddress The destination_address
     */
    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return The destinationAddressNotes
     */
    public String getDestinationAddressNotes() {
        return destinationAddressNotes;
    }

    /**
     * @param destinationAddressNotes The destination_address_notes
     */
    public void setDestinationAddressNotes(String destinationAddressNotes) {
        this.destinationAddressNotes = destinationAddressNotes;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

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
     * @return The code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(String code) {
        this.code = code;
    }

}
