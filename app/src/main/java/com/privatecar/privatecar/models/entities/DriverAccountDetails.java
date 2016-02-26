
package com.privatecar.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverAccountDetails {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("credit")
    @Expose
    private String credit;
    @SerializedName("low_rating_count")
    @Expose
    private String lowRatingCount;
    @SerializedName("noreason_decline_trip_count")
    @Expose
    private String noreasonDeclineTripCount;
    @SerializedName("overall_rating")
    @Expose
    private String overallRating;
    @SerializedName("civil_id")
    @Expose
    private Object civilId;
    @SerializedName("civil_exp_date")
    @Expose
    private Object civilExpDate;
    @SerializedName("license_no")
    @Expose
    private Object licenseNo;
    @SerializedName("license_type")
    @Expose
    private Object licenseType;
    @SerializedName("license_exp_date")
    @Expose
    private Object licenseExpDate;
    @SerializedName("personal_photo")
    @Expose
    private String personalPhoto;
    @SerializedName("civilid_photo")
    @Expose
    private String civilidPhoto;
    @SerializedName("civilid_back_photo")
    @Expose
    private String civilidBackPhoto;
    @SerializedName("license_photo")
    @Expose
    private String licensePhoto;
    @SerializedName("license_back_photo")
    @Expose
    private String licenseBackPhoto;
    @SerializedName("rental_company_id")
    @Expose
    private Object rentalCompanyId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("last_location")
    @Expose
    private String lastLocation;
    @SerializedName("default_car_id")
    @Expose
    private String defaultCarId;
    @SerializedName("totaltrips")
    @Expose
    private String totaltrips;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("todayprofit")
    @Expose
    private int todayprofit;
    @SerializedName("todayhours")
    @Expose
    private int todayhours;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The credit
     */
    public String getCredit() {
        return credit;
    }

    /**
     * @param credit The credit
     */
    public void setCredit(String credit) {
        this.credit = credit;
    }

    /**
     * @return The lowRatingCount
     */
    public String getLowRatingCount() {
        return lowRatingCount;
    }

    /**
     * @param lowRatingCount The low_rating_count
     */
    public void setLowRatingCount(String lowRatingCount) {
        this.lowRatingCount = lowRatingCount;
    }

    /**
     * @return The noreasonDeclineTripCount
     */
    public String getNoreasonDeclineTripCount() {
        return noreasonDeclineTripCount;
    }

    /**
     * @param noreasonDeclineTripCount The noreason_decline_trip_count
     */
    public void setNoreasonDeclineTripCount(String noreasonDeclineTripCount) {
        this.noreasonDeclineTripCount = noreasonDeclineTripCount;
    }

    /**
     * @return The overallRating
     */
    public String getOverallRating() {
        return overallRating;
    }

    /**
     * @param overallRating The overall_rating
     */
    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    /**
     * @return The civilId
     */
    public Object getCivilId() {
        return civilId;
    }

    /**
     * @param civilId The civil_id
     */
    public void setCivilId(Object civilId) {
        this.civilId = civilId;
    }

    /**
     * @return The civilExpDate
     */
    public Object getCivilExpDate() {
        return civilExpDate;
    }

    /**
     * @param civilExpDate The civil_exp_date
     */
    public void setCivilExpDate(Object civilExpDate) {
        this.civilExpDate = civilExpDate;
    }

    /**
     * @return The licenseNo
     */
    public Object getLicenseNo() {
        return licenseNo;
    }

    /**
     * @param licenseNo The license_no
     */
    public void setLicenseNo(Object licenseNo) {
        this.licenseNo = licenseNo;
    }

    /**
     * @return The licenseType
     */
    public Object getLicenseType() {
        return licenseType;
    }

    /**
     * @param licenseType The license_type
     */
    public void setLicenseType(Object licenseType) {
        this.licenseType = licenseType;
    }

    /**
     * @return The licenseExpDate
     */
    public Object getLicenseExpDate() {
        return licenseExpDate;
    }

    /**
     * @param licenseExpDate The license_exp_date
     */
    public void setLicenseExpDate(Object licenseExpDate) {
        this.licenseExpDate = licenseExpDate;
    }

    /**
     * @return The personalPhoto
     */
    public String getPersonalPhoto() {
        return personalPhoto;
    }

    /**
     * @param personalPhoto The personal_photo
     */
    public void setPersonalPhoto(String personalPhoto) {
        this.personalPhoto = personalPhoto;
    }

    /**
     * @return The civilidPhoto
     */
    public String getCivilidPhoto() {
        return civilidPhoto;
    }

    /**
     * @param civilidPhoto The civilid_photo
     */
    public void setCivilidPhoto(String civilidPhoto) {
        this.civilidPhoto = civilidPhoto;
    }

    /**
     * @return The civilidBackPhoto
     */
    public String getCivilidBackPhoto() {
        return civilidBackPhoto;
    }

    /**
     * @param civilidBackPhoto The civilid_back_photo
     */
    public void setCivilidBackPhoto(String civilidBackPhoto) {
        this.civilidBackPhoto = civilidBackPhoto;
    }

    /**
     * @return The licensePhoto
     */
    public String getLicensePhoto() {
        return licensePhoto;
    }

    /**
     * @param licensePhoto The license_photo
     */
    public void setLicensePhoto(String licensePhoto) {
        this.licensePhoto = licensePhoto;
    }

    /**
     * @return The licenseBackPhoto
     */
    public String getLicenseBackPhoto() {
        return licenseBackPhoto;
    }

    /**
     * @param licenseBackPhoto The license_back_photo
     */
    public void setLicenseBackPhoto(String licenseBackPhoto) {
        this.licenseBackPhoto = licenseBackPhoto;
    }

    /**
     * @return The rentalCompanyId
     */
    public Object getRentalCompanyId() {
        return rentalCompanyId;
    }

    /**
     * @param rentalCompanyId The rental_company_id
     */
    public void setRentalCompanyId(Object rentalCompanyId) {
        this.rentalCompanyId = rentalCompanyId;
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
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The lastLocation
     */
    public String getLastLocation() {
        return lastLocation;
    }

    /**
     * @param lastLocation The last_location
     */
    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    /**
     * @return The defaultCarId
     */
    public String getDefaultCarId() {
        return defaultCarId;
    }

    /**
     * @param defaultCarId The default_car_id
     */
    public void setDefaultCarId(String defaultCarId) {
        this.defaultCarId = defaultCarId;
    }

    /**
     * @return The totaltrips
     */
    public String getTotaltrips() {
        return totaltrips;
    }

    /**
     * @param totaltrips The totaltrips
     */
    public void setTotaltrips(String totaltrips) {
        this.totaltrips = totaltrips;
    }

    /**
     * @return The fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname The fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return The todayprofit
     */
    public int getTodayprofit() {
        return todayprofit;
    }

    /**
     * @param todayprofit The todayprofit
     */
    public void setTodayprofit(int todayprofit) {
        this.todayprofit = todayprofit;
    }

    /**
     * @return The todayhours
     */
    public int getTodayhours() {
        return todayhours;
    }

    /**
     * @param todayhours The todayhours
     */
    public void setTodayhours(int todayhours) {
        this.todayhours = todayhours;
    }

}
