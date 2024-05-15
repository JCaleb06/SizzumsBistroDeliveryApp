package com.uep.sizzumsbistrodeliveryapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.webkit.PermissionRequest;
import android.widget.TextView;
import android.widget.Toast;
//import com.tomtom.sdk.map.MapView;
//import com.tomtom.sdk.map.OnMapReadyCallback;
//import com.tomtom.sdk.map.TomtomMap;
//import com.tomtom.online.sdk.map.MapFragment;
//import com.tomtom.online.sdk.map.MapFragmentBuilder;
//import com.tomtom.online.sdk.map.MapProperties;
//import com.tomtom.online.sdk.map.MapViewConfiguration;
//import com.tomtom.online.sdk.map.TomtomMapCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class gps extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_LOCATION = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView longtitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        longtitude = findViewById(R.id.longtitude);
        latitude = findViewById(R.id.latitude);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(gps.this);


    }


}