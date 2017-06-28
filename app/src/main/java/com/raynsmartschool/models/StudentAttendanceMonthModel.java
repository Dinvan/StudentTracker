package com.raynsmartschool.models;

/**
 * Created by Ravi on 6/28/2017.
 */

public class StudentAttendanceMonthModel {

    private String month_name;
    private String present_days;
    private String absent_days;
    private String holidays;

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getPresent_days() {
        return present_days;
    }

    public void setPresent_days(String present_days) {
        this.present_days = present_days;
    }

    public String getAbsent_days() {
        return absent_days;
    }

    public void setAbsent_days(String absent_days) {
        this.absent_days = absent_days;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }
}
