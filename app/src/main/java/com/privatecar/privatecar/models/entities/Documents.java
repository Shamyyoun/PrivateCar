
package com.privatecar.privatecar.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Documents {
    @SerializedName("Personal Photo")
    @Expose
    private String PersonalPhoto;
    @SerializedName("Civil ID Photo")
    @Expose
    private String CivilIDPhoto;
    @SerializedName("Civil ID Back Photo")
    @Expose
    private String CivilIDBackPhoto;
    @SerializedName("License Photo")
    @Expose
    private String LicensePhoto;
    @SerializedName("License Back Photo")
    @Expose
    private String LicenseBackPhoto;

    /**
     * @return The PersonalPhoto
     */
    public String getPersonalPhoto() {
        return PersonalPhoto;
    }

    /**
     * @param PersonalPhoto The Personal Photo
     */
    public void setPersonalPhoto(String PersonalPhoto) {
        this.PersonalPhoto = PersonalPhoto;
    }

    /**
     * @return The CivilIDPhoto
     */
    public String getCivilIDPhoto() {
        return CivilIDPhoto;
    }

    /**
     * @param CivilIDPhoto The Civil ID Photo
     */
    public void setCivilIDPhoto(String CivilIDPhoto) {
        this.CivilIDPhoto = CivilIDPhoto;
    }

    /**
     * @return The CivilIDBackPhoto
     */
    public String getCivilIDBackPhoto() {
        return CivilIDBackPhoto;
    }

    /**
     * @param CivilIDBackPhoto The Civil ID Back Photo
     */
    public void setCivilIDBackPhoto(String CivilIDBackPhoto) {
        this.CivilIDBackPhoto = CivilIDBackPhoto;
    }

    /**
     * @return The LicensePhoto
     */
    public String getLicensePhoto() {
        return LicensePhoto;
    }

    /**
     * @param LicensePhoto The License Photo
     */
    public void setLicensePhoto(String LicensePhoto) {
        this.LicensePhoto = LicensePhoto;
    }

    /**
     * @return The LicenseBackPhoto
     */
    public String getLicenseBackPhoto() {
        return LicenseBackPhoto;
    }

    /**
     * @param LicenseBackPhoto The License Back Photo
     */
    public void setLicenseBackPhoto(String LicenseBackPhoto) {
        this.LicenseBackPhoto = LicenseBackPhoto;
    }

    public List<String> getAsArray() {
        List<String> documents = new ArrayList<String>();

        // check all values and add it to the array
        if (!Utils.isNullOrEmpty(PersonalPhoto)) {
            documents.add(PersonalPhoto);
        }
        if (!Utils.isNullOrEmpty(CivilIDPhoto)) {
            documents.add(CivilIDPhoto);
        }
        if (!Utils.isNullOrEmpty(CivilIDBackPhoto)) {
            documents.add(CivilIDBackPhoto);
        }
        if (!Utils.isNullOrEmpty(LicensePhoto)) {
            documents.add(LicensePhoto);
        }
        if (!Utils.isNullOrEmpty(LicenseBackPhoto)) {
            documents.add(LicenseBackPhoto);
        }

        return documents;
    }
}
