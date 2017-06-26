package com.raynsmartschool.models;

import java.io.Serializable;

/**
 * Created by Ravi on 6/4/2017.
 */

public class Announcement  implements Serializable{

    public String id;
    public String message;
    public String title;
    public String image;
    public String mediam;
    public String date_created;
    public String sender_id;
    public String sender_name;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMediam() {
        return mediam;
    }

    public void setMediam(String mediam) {
        this.mediam = mediam;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
