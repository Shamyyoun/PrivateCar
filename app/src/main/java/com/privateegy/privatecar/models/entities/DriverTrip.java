
package com.privateegy.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverTrip implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("customer_id")
    @Expose
    private int customerId;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("pickup_address_notes")
    @Expose
    private String pickupAddressNotes;
    @SerializedName("destination_address")
    @Expose
    private String destinationAddress;
    @SerializedName("destination_address_notes")
    @Expose
    private String destinationAddressNotes;
    @SerializedName("will_lead_the_driver")
    @Expose
    private int willLeadTheDriver;
    @SerializedName("pickup_date_time")
    @Expose
    private String pickupDateTime;
    @SerializedName("service_type")
    @Expose
    private int serviceType;
    @SerializedName("estimate_fare")
    @Expose
    private int estimateFare;
    @SerializedName("payment_type")
    @Expose
    private int paymentType;
    @SerializedName("notes_for_driver")
    @Expose
    private String notesForDriver;
    @SerializedName("promo_code_id")
    @Expose
    private Object promoCodeId;
    @SerializedName("driver_id")
    @Expose
    private int driverId;
    @SerializedName("car_id")
    @Expose
    private Object carId;
    @SerializedName("driver_estimate_arrival_time")
    @Expose
    private Object driverEstimateArrivalTime;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("actual_fare")
    @Expose
    private Object actualFare;
    @SerializedName("address_type")
    @Expose
    private int addressType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("estimate_distance")
    @Expose
    private float estimateDistance;
    @SerializedName("estimate_time")
    @Expose
    private double estimateTime;
    @SerializedName("pickup_location")
    @Expose
    private String pickupLocation;
    @SerializedName("destination_location")
    @Expose
    private String destinationLocation;
    @SerializedName("pickup_now")
    @Expose
    private int pickupNow;
    @SerializedName("actual_distance")
    @Expose
    private Object actualDistance;
    @SerializedName("actual_time")
    @Expose
    private Object actualTime;

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
     * @return The pickupDateTime
     */
    public String getPickupDateTime() {
        return pickupDateTime;
    }

    /**
     * @param pickupDateTime The pickup_date_time
     */
    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    /**
     * @return The serviceType
     */
    public int getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType The service_type
     */
    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return The estimateFare
     */
    public int getEstimateFare() {
        return estimateFare;
    }

    /**
     * @param estimateFare The estimate_fare
     */
    public void setEstimateFare(int estimateFare) {
        this.estimateFare = estimateFare;
    }

    /**
     * @return The paymentType
     */
    public int getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType The payment_type
     */
    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
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
     * @return The promoCodeId
     */
    public Object getPromoCodeId() {
        return promoCodeId;
    }

    /**
     * @param promoCodeId The promo_code_id
     */
    public void setPromoCodeId(Object promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    /**
     * @return The driverId
     */
    public int getDriverId() {
        return driverId;
    }

    /**
     * @param driverId The driver_id
     */
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    /**
     * @return The carId
     */
    public Object getCarId() {
        return carId;
    }

    /**
     * @param carId The car_id
     */
    public void setCarId(Object carId) {
        this.carId = carId;
    }

    /**
     * @return The driverEstimateArrivalTime
     */
    public Object getDriverEstimateArrivalTime() {
        return driverEstimateArrivalTime;
    }

    /**
     * @param driverEstimateArrivalTime The driver_estimate_arrival_time
     */
    public void setDriverEstimateArrivalTime(Object driverEstimateArrivalTime) {
        this.driverEstimateArrivalTime = driverEstimateArrivalTime;
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
     * @return The actualFare
     */
    public Object getActualFare() {
        return actualFare;
    }

    /**
     * @param actualFare The actual_fare
     */
    public void setActualFare(Object actualFare) {
        this.actualFare = actualFare;
    }

    /**
     * @return The addressType
     */
    public int getAddressType() {
        return addressType;
    }

    /**
     * @param addressType The address_type
     */
    public void setAddressType(int addressType) {
        this.addressType = addressType;
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
     * @return The deletedAt
     */
    public Object getDeletedAt() {
        return deletedAt;
    }

    /**
     * @param deletedAt The deleted_at
     */
    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * @return The estimateDistance
     */
    public float getEstimateDistance() {
        return estimateDistance;
    }

    /**
     * @param estimateDistance The estimate_distance
     */
    public void setEstimateDistance(float estimateDistance) {
        this.estimateDistance = estimateDistance;
    }

    /**
     * @return The estimateTime
     */
    public double getEstimateTime() {
        return estimateTime;
    }

    /**
     * @param estimateTime The estimate_time
     */
    public void setEstimateTime(double estimateTime) {
        this.estimateTime = estimateTime;
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
     * @return The destinationLocation
     */
    public String getDestinationLocation() {
        return destinationLocation;
    }

    /**
     * @param destinationLocation The destination_location
     */
    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    /**
     * @return The pickupNow
     */
    public int getPickupNow() {
        return pickupNow;
    }

    /**
     * @param pickupNow The pickup_now
     */
    public void setPickupNow(int pickupNow) {
        this.pickupNow = pickupNow;
    }

    /**
     * @return The actualDistance
     */
    public Object getActualDistance() {
        return actualDistance;
    }

    /**
     * @param actualDistance The actual_distance
     */
    public void setActualDistance(Object actualDistance) {
        this.actualDistance = actualDistance;
    }

    /**
     * @return The actualTime
     */
    public Object getActualTime() {
        return actualTime;
    }

    /**
     * @param actualTime The actual_time
     */
    public void setActualTime(Object actualTime) {
        this.actualTime = actualTime;
    }

}
