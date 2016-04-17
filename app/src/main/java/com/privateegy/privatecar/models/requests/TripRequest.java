package com.privateegy.privatecar.models.requests;

import com.privateegy.privatecar.models.entities.PrivateCarPlace;
import com.privateegy.privatecar.models.enums.AddressType;
import com.privateegy.privatecar.models.enums.CarType;
import com.privateegy.privatecar.models.enums.PaymentType;

import java.util.Calendar;

/**
 * Created by Shamyyoun on 3/27/2016.
 */
public class TripRequest {
    private boolean pickupNow;
    private PrivateCarPlace pickupPlace;
    private CarType carType;
    private PaymentType paymentType;
    private AddressType destinationType;
    private PrivateCarPlace destinationPlace;
    private float estimateDistance; // kilo meters
    private float estimateTime; // seconds
    private float estimateFare;
    private Calendar pickupTime;
    private String notes;
    private String pickupDetails;

    public PrivateCarPlace getPickupPlace() {
        return pickupPlace;
    }

    public boolean isPickupNow() {
        return pickupNow;
    }

    public void setPickupNow(boolean pickupNow) {
        this.pickupNow = pickupNow;
    }

    public void setPickupPlace(PrivateCarPlace pickupPlace) {
        this.pickupPlace = pickupPlace;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public AddressType getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(AddressType destinationType) {
        this.destinationType = destinationType;
    }

    public PrivateCarPlace getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(PrivateCarPlace destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public float getEstimateDistance() {
        return estimateDistance;
    }

    public void setEstimateDistance(float estimateDistance) {
        this.estimateDistance = estimateDistance;
    }

    public float getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(float estimateTime) {
        this.estimateTime = estimateTime;
    }

    public float getEstimateFare() {
        return estimateFare;
    }

    public void setEstimateFare(float estimateFare) {
        this.estimateFare = estimateFare;
    }

    public Calendar getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Calendar pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPickupDetails() {
        return pickupDetails;
    }

    public void setPickupDetails(String pickupDetails) {
        this.pickupDetails = pickupDetails;
    }
}
