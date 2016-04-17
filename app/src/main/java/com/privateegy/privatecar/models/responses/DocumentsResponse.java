
package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.Documents;

import java.util.List;

public class DocumentsResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private Documents documents;
    @SerializedName("validation")
    @Expose
    private List<String> validation;

    /**
     * @return The status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return The documents
     */
    public Documents getDocuments() {
        return documents;
    }

    /**
     * @param documents The documents
     */
    public void setDocuments(Documents documents) {
        this.documents = documents;
    }

    /**
     * @return The validation
     */
    public List<String> getValidation() {
        return validation;
    }

    /**
     * @param validation The validation
     */
    public void setValidation(List<String> validation) {
        this.validation = validation;
    }

}
