package com.raynsmartschool.models;

import java.io.Serializable;

/**
 * Created by Ravi on 7/1/2017.
 */

public class StudentsModel implements Serializable{

    private String Scholarno;
    private String Studcode;
    private String Mname;
    private String Sname;
    private String Fname;
    private String Fsname;
    private String Mothname;
    private String Mothsname;
    private String email;
    private String address;
    private String Sex;
    private String ClassName;
    private String Section;
    private String Medium;
    private String Feegroup;
    private String user_id;
    private String mobile;

    public String getScholarno() {
        return Scholarno;
    }

    public void setScholarno(String scholarno) {
        Scholarno = scholarno;
    }

    public String getStudcode() {
        return Studcode;
    }

    public void setStudcode(String studcode) {
        Studcode = studcode;
    }

    public String getMname() {
        return Mname;
    }

    public void setMname(String mname) {
        Mname = mname;
    }

    public String getSname() {
        return Sname;
    }

    public void setSname(String sname) {
        Sname = sname;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getFsname() {
        return Fsname;
    }

    public void setFsname(String fsname) {
        Fsname = fsname;
    }

    public String getMothname() {
        return Mothname;
    }

    public void setMothname(String mothname) {
        Mothname = mothname;
    }

    public String getMothsname() {
        return Mothsname;
    }

    public void setMothsname(String mothsname) {
        Mothsname = mothsname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String aClass) {
        ClassName = aClass;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getMedium() {
        return Medium;
    }

    public void setMedium(String medium) {
        Medium = medium;
    }

    public String getFeegroup() {
        return Feegroup;
    }

    public void setFeegroup(String feegroup) {
        Feegroup = feegroup;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
