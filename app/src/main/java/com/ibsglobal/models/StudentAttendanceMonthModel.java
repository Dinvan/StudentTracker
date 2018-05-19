package com.ibsglobal.models;

/**
 * Created by Ravi on 6/28/2017.
 */

public class StudentAttendanceMonthModel {

    private String month;
    private String present_count;
    private String absent_count;
    private String holiday;
    private String[] absent_days;


    public String getAbsent_count() {
        return absent_count;
    }

    public void setAbsent_count(String absent_count) {
        this.absent_count = absent_count;
    }

    public String[] getAbsent_days() {
        return absent_days;
    }

    public void setAbsent_days(String[] absent_days) {
        this.absent_days = absent_days;
    }

    public String getMonth_name() {
        return month;
    }

    public void setMonth_name(String month_name) {
        this.month = month_name;
    }

    public String getPresent_days() {
        return present_count;
    }

    public void setPresent_days(String present_days) {
        this.present_count = present_days;
    }

    public String getHolidays() {
        return holiday;
    }

    public void setHolidays(String holidays) {
        this.holiday = holidays;
    }



}
