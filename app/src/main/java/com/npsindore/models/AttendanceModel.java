package com.npsindore.models;

import java.io.Serializable;

/**
 * Created by Ravi on 6/24/2017.
 */

public class AttendanceModel implements Serializable{
    private String fname;
    private String sname;
    private String id;
    private int status = 1;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
