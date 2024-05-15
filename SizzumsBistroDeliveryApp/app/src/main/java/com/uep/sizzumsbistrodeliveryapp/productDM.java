package com.uep.sizzumsbistrodeliveryapp;

import android.graphics.drawable.Drawable;

public class productDM {
    String name,qty;
    Drawable image;

    public productDM(String name, String qty, Drawable image){
        this.name = name;
        this.qty = qty;
        this.image = image;
    }
    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }

    public Drawable getImage() {
        return image;
    }
}
