package com.raynsmartschool.models;

import java.io.Serializable;

/**
 * Created by Ravi on 6/15/2017.
 */

public class TeachersClassModel implements Serializable {

    public String class_name;
    public String class_section;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_section() {
        return class_section;
    }

    public void setClass_section(String class_section) {
        this.class_section = class_section;
    }
}
