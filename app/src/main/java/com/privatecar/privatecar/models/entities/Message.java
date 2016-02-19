package com.privatecar.privatecar.models.entities;

import java.io.Serializable;

/**
 * Created by basim on 14/2/16.
 * *
 */
public class Message implements Serializable {
    private String title;
    private String body;
    private String date;
    private Boolean selected = false;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
