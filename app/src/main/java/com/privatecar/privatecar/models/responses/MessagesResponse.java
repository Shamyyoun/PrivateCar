
package com.privatecar.privatecar.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatecar.privatecar.models.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("content")
    @Expose
    private List<Message> messages = new ArrayList<>();
    @SerializedName("validation")
    @Expose
    private Object validation;

    /**
     * @return The status
     */
    public boolean isSuccess() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return The messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * @param messages The messages
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
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
