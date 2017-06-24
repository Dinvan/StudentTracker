package com.raynsmartschool.models;

import java.io.Serializable;

/**
 * Created by Ravi on 6/24/2017.
 */

public class StudentModel implements Serializable{
    String fname;
    String sname;
    String id;
    boolean isSelected;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return sname;
    }

    public void setLname(String lname) {
        this.sname = lname;
    }

    public String getStudent_id() {
        return id;
    }

    public void setStudent_id(String student_id) {
        this.id = student_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
