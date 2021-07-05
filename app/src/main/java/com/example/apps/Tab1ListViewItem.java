package com.example.apps;

import android.graphics.drawable.Drawable;

public class Tab1ListViewItem {
    private Drawable iconDrawable ;
    private String nameStr ;
    private String numberStr ;

    public Tab1ListViewItem() {

    }
    public Tab1ListViewItem(Drawable icon, String name, String number) {
        this.iconDrawable = icon;
        this.nameStr = name;
        this.numberStr = number;
    }

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setName(String name) {
        nameStr = name ;
    }
    public void setNumber(String number) {
        numberStr = number ;
    }

    public Drawable getIcon() { return this.iconDrawable ; }
    public String getName() {
        return this.nameStr ;
    }
    public String getNumber() {
        return this.numberStr ;
    }
}