package com.studenttracker.models;

import java.io.Serializable;

/**
 * Created by Ravi on 6/4/2017.
 */

public class Announcement  implements Serializable{

    public String id;
    public String message;
    public String image;
    public String mediam;
    public String date_created;


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
