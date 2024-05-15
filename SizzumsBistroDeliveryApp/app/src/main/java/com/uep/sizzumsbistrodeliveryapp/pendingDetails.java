package com.uep.sizzumsbistrodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class pendingDetails extends AppCompatActivity {
    TextView route,ordernumber,customername,address,contactnumber,subtotal,shippingfee,totalpayment,callnow,paymentID,paymentmethod,back;
    AppCompatButton complete;
    ArrayList<productDM> pdm;
    String name,qty,imagedirectory,currentUser;
    Drawable image;
    int drawableID;
    ConstraintLayout animcon;
    LottieAnimationView animrider,animtext;
    RecyclerView productlists;
    Dialog loading;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_details);


        loading = new Dialog(pendingDetails.this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setContentView(R.layout.dialog_loading);
        complete = findViewById(R.id.btncomplete);
        back = findViewById(R.id.back);
        paymentID = findViewById(R.id.paymentid);
        paymentmethod = findViewById(R.id.paymentmethod);
        customername = findViewById(R.id.customername);
        address = findViewById(R.id.textView5);
        contactnumber = findViewById(R.id.contactnumber);
        subtotal = findViewById(R.id.subtotal);
        shippingfee = findViewById(R.id.shippingfee);
        totalpayment = findViewById(R.id.totalpayment);
        callnow = findViewById(R.id.btncallnow);

        productlists = findViewById(R.id.productlists);

        route=findViewById(R.id.btncheckmap);
        pdm = new ArrayList<>();
        ordernumber = findViewById(R.id.ordernumber);
        Intent intent = getIntent();
        if (intent.getStringExtra("status").toString().contains("delivered")){
            callnow.setVisibility(View.INVISIBLE);
            complete.setVisibility(View.INVISIBLE);
        }


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.show();
                ParseQuery<ParseObject> setstatus = new ParseQuery<ParseObject>("OnlineOrders");
                setstatus.whereEqualTo("orderNumber",ordernumber.getText().toString());
                setstatus.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e== null){
                            for (ParseObject o:objects){
                                o.put("status","delivered");
                                o.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e== null){
                                            Toast.makeText(pendingDetails.this, "Order delivered successfilly.", Toast.LENGTH_SHORT).show();
                                        }else {
                                            loading.dismiss();
                                            Toast.makeText(pendingDetails.this, "Error : " +e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            loading.dismiss();
                            Intent intent1 = new Intent(pendingDetails.this,dashboard.class);
                            startActivity(intent1);
                            finish();
                        }else{
                            loading.dismiss();
                            Toast.makeText(pendingDetails.this, "Error : " + e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if (intent.getStringExtra("ordernumber") != null){
            ordernumber.setText(intent.getStringExtra("ordernumber"));

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("OnlineOrders");
            query.whereEqualTo("status",intent.getStringExtra("status"));
            query.whereEqualTo("orderNumber",intent.getStringExtra("ordernumber"));
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null){
                        for (ParseObject o:objects){
                            customername.setText(o.getString("firstname") + " " + o.getString("lastname"));
                            address.setText(o.getString("address"));
                            contactnumber.setText(o.getString("contactnumber"));
                            subtotal.setText("₱ "+ String.valueOf(o.getInt("subTotal")));
                            shippingfee.setText("₱ "+ String.valueOf(o.getInt("shippingFee")));
                            totalpayment.setText( "₱ "+ String.valueOf(o.getInt("totalPayment")));
                            paymentID.setText(o.getString("paymentid"));
                            paymentmethod.setText(o.getString("paymentMethod"));
                            name = o.getString("name");
                            qty = String.valueOf(o.getInt("qty")+ "x");
                            imagedirectory = "@drawable/"+o.getString("imagename");
                            drawableID = getResources().getIdentifier(imagedirectory,"drawable",getPackageName());
                            image = getResources().getDrawable(drawableID);

                            productDM p = new productDM(name,qty,image);
                            pdm.add(p);
                        }
                        productlists.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                        productRA adapter = new productRA(pdm);
                        productlists.setAdapter(adapter);
                    }else{
                        Toast.makeText(pendingDetails.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent1= new Intent(pendingDetails.this,map.class);
                intent1.putExtra("status",intent.getStringExtra("status"));
                intent1.putExtra("ordernumber",intent.getStringExtra("ordernumber"));
                intent1.putExtra("address",address.getText().toString());
                startActivity(intent1);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        callnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:"+ contactnumber.getText().toString()));
                if (ContextCompat.checkSelfPermission(pendingDetails.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity) pendingDetails.this, new String[]{Manifest.permission.CALL_PHONE},1);
                }else{
                    try{
                        startActivity(call);
                    }catch (SecurityException e){
                        Toast.makeText(pendingDetails.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(pendingDetails.this,dashboard.class);
        startActivity(intent);
        finish();
    }
}