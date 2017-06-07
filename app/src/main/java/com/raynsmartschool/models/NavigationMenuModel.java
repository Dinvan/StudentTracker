package com.raynsmartschool.models;

/**
 * Created by ravib on 05/27/2017.
 */
public class NavigationMenuModel {

    String name = "";
    int Icon;

    public NavigationMenuModel(String menu) {
        this.name = menu;
     }


    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
