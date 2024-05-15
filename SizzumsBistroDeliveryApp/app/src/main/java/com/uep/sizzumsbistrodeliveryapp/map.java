package com.uep.sizzumsbistrodeliveryapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class map extends AppCompatActivity {
    WebView webView;
    ImageButton back;

    private static final int PERMISSION_REQUEST_LOCATION = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    Double longi,lati;
    String address,status,ordernumber,brgy,city,province;
    Dialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        loading = new Dialog(map.this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setContentView(R.layout.dialog_loading);

        Intent  intent  = getIntent();
        address = intent.getStringExtra("address");
        status  = intent.getStringExtra("status");
        ordernumber = intent.getStringExtra("ordernumber");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(map.this);


        String[] parts = address.split(", ");


        brgy  =  parts[2];
        city  = parts[3];
        province = parts[4];
        Toast.makeText(map.this, "address : "  + address, Toast.LENGTH_LONG).show();

        back = findViewById(R.id.back);
        back.setOnClickListener(v->{
            onBackPressed();
        });

        loading.show();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                loading.dismiss();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        getCurrentLocation();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent inte = new Intent(map.this, pendingDetails.class);
        inte.putExtra("status",status);
        inte.putExtra("ordernumber",ordernumber);
        inte.putExtra("address",address);
        startActivity(inte);
        finish();

    }
    public void getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (fusedLocationProviderClient != null) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_LOCATION);
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(map.this, new OnCompleteListener<Location>() {

                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                Location location = task.getResult();
                                lati = location.getLatitude();
                                longi = location.getLongitude();

                                try {
                                    webView.loadUrl("https://www.google.com/maps/dir/" + lati + "," + longi + "/" + URLEncoder.encode(brgy + " " + city + " " + province, "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    throw new RuntimeException(e);
                                }
                                //Toast.makeText(map.this, "Longi : " + longi + "Lati : " + lati, Toast.LENGTH_LONG).show();
                                //
//                                latitude.setText(String.valueOf(lati));
//                                longtitude.setText(String.valueOf(longi));
                            }else{
                                Toast.makeText(map.this, "Failed to get location", Toast.LENGTH_SHORT).show();
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
        ActivityCompat.requestPermissions(map.this,
                new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMISSION_REQUEST_LOCATION);
    }
    public boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(map.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }
}