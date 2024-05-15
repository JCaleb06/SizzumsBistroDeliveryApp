package com.uep.sizzumsbistrodeliveryapp;

import android.app.Application;

import com.parse.Parse;

public class back4app extends Application {
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.APPLICATION_ID))
                .clientKey(getString(R.string.CLIENT_KEY))
                .server(getString(R.string.SERVER))
                .build()
        );
    }
}
