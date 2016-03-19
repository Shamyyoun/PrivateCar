package com.privatecar.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by basim on 19/3/16.
 */
public class NearDriver {
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("low_rating_count")
    @Expose
    private Integer lowRatingCount;
    @SerializedName("overall_rating")
    @Expose
    private Integer overallRating;
    @SerializedName("personal_photo")
    @Expose
    private String personalPhoto;
    @SerializedName("last_location")
    @Expose
    private String lastLocation;
    @SerializedName("totaltrips")
    @Expose
    private Integer totaltrips;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("todayprofit")
    @Expose
    private Integer todayprofit;
    @SerializedName("todayhours")
    @Expose
    private Integer todayhours;

    /**
     * @return The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return The lowRatingCount
     */
    public Integer getLowRatingCount() {
        return lowRatingCount;
    }

    /**
     * @param lowRatingCount The low_rating_count
     */
    public void setLowRatingCount(Integer lowRatingCount) {
        this.lowRatingCount = lowRatingCount;
    }

    /**
     * @return The overallRating
     */
    public Integer getOverallRating() {
        return overallRating;
    }

    /**
     * @param overallRating The overall_rating
     */
    public void setOverallRating(Integer overallRating) {
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
     * @return The totaltrips
     */
    public Integer getTotaltrips() {
        return totaltrips;
    }

    /**
     * @param totaltrips The totaltrips
     */
    public void setTotaltrips(Integer totaltrips) {
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
    public Integer getTodayprofit() {
        return todayprofit;
    }

    /**
     * @param todayprofit The todayprofit
     */
    public void setTodayprofit(Integer todayprofit) {
        this.todayprofit = todayprofit;
    }

    /**
     * @return The todayhours
     */
    public Integer getTodayhours() {
        return todayhours;
    }

    /**
     * @param todayhours The todayhours
     */
    public void setTodayhours(Integer todayhours) {
        this.todayhours = todayhours;
    }
    
}
