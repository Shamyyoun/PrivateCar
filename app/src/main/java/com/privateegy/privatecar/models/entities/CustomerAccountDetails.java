
package com.privateegy.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerAccountDetails {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("credit")
    @Expose
    private int credit;
    @SerializedName("low_rating_count")
    @Expose
    private int lowRatingCount;
    @SerializedName("credit_card_info")
    @Expose
    private Object creditCardInfo;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("last_location")
    @Expose
    private String lastLocation;
    @SerializedName("overall_rating")
    @Expose
    private double overallRating;
    @SerializedName("personal_photo")
    @Expose
    private String personalPhoto;
    @SerializedName("totaltrips")
    @Expose
    private int totaltrips;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;

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
     * @return The credit
     */
    public int getCredit() {
        return credit;
    }

    /**
     * @param credit The credit
     */
    public void setCredit(int credit) {
        this.credit = credit;
    }

    /**
     * @return The lowRatingCount
     */
    public int getLowRatingCount() {
        return lowRatingCount;
    }

    /**
     * @param lowRatingCount The low_rating_count
     */
    public void setLowRatingCount(int lowRatingCount) {
        this.lowRatingCount = lowRatingCount;
    }

    /**
     * @return The creditCardInfo
     */
    public Object getCreditCardInfo() {
        return creditCardInfo;
    }

    /**
     * @param creditCardInfo The credit_card_info
     */
    public void setCreditCardInfo(Object creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
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
    public String getDeletedAt() {
        return deletedAt;
    }

    /**
     * @param deletedAt The deleted_at
     */
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * @return The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(int userId) {
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
     * @return The overallRating
     */
    public double getOverallRating() {
        return overallRating;
    }

    /**
     * @param overallRating The overall_rating
     */
    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
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
     * @return The totaltrips
     */
    public int getTotaltrips() {
        return totaltrips;
    }

    /**
     * @param totaltrips The totaltrips
     */
    public void setTotaltrips(int totaltrips) {
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
     * @return The mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile The mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
