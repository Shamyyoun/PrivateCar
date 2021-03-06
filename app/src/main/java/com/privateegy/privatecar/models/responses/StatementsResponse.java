
package com.privateegy.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privateegy.privatecar.models.entities.Statement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatementsResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<Statement> statements = new ArrayList<Statement>();
    @SerializedName("validation")
    @Expose
    private Object validation;

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
     * @return The statements
     */
    public List<Statement> getStatements() {
        return statements;
    }

    /**
     * @param statements The statements
     */
    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    /**
     * @return The validation
     */
    public Object getValidation() {
        return validation;
    }

    /**
     * @param validation The validation
     */
    public void setValidation(Object validation) {
        this.validation = validation;
    }

}
