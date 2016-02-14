package com.privatecar.privatecar.models.entities;

/**
 * Created by basim on 14/2/16.
 * *
 */
public class Message {
    private String content;
    private String date;
    private Boolean selected = false;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
