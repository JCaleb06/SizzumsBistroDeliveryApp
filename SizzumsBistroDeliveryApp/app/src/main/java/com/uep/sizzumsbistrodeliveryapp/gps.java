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

        getCurrentLocation();
    }

    public void getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (fusedLocationProviderClient != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_LOCATION);
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(gps.this, new OnCompleteListener<Location>() {
                        Double lati,longi;
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                Location location = task.getResult();
                                lati = location.getLatitude();
                                longi = location.getLongitude();

                                Toast.makeText(gps.this, "Longi : " + longi + "Lati : " + lati, Toast.LENGTH_LONG).show();
                                //
//                                latitude.setText(String.valueOf(lati));
//                                longtitude.setText(String.valueOf(longi));
                            }else{
                                Toast.makeText(gps.this, "Failed to get location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }else{
                Toast.makeText(this, "Turn in Location", Toast.LENGTH_SHORT).show();
                Intent intent  = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        }else{
            requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_LOCATION){
            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "GRANTED!", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            }else{
                Toast.makeText(this, "DENIED!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    LocationManager locationManager;
    @SuppressLint("ServiceCast")
    private Boolean isLocationEnabled(){

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean isGPsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGPsEnabled;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(gps.this,
                new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMISSION_REQUEST_LOCATION);
    }
    public boolean checkPermissions(){
            if (ActivityCompat.checkSelfPermission(gps.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(gps.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                return true;
            }
            return false;
    }
}