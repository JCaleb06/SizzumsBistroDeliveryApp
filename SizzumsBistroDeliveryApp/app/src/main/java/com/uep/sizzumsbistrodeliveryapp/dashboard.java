package com.uep.sizzumsbistrodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class dashboard extends AppCompatActivity {

    ArrayList<String> allordernumber;
    HashSet<String> hashSet;
    String customer,address,contact,dm_username,ordernumber,stts;
    public static String currentUser;
    int totalpayment;
    TextView user,taskcompleted;
    RecyclerView orderlists;
    AppCompatButton delivered,showall;
    ArrayList<orderDM> odm;
    Dialog loading,logout;
    AppCompatButton btncancel,btnconfirm,btnlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        allordernumber = new ArrayList<>();
        hashSet = new HashSet<>();
        user =findViewById(R.id.user);
        orderlists = findViewById(R.id.orderlists);
        delivered = findViewById(R.id.appCompatButton2);

        loading = new Dialog(dashboard.this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setContentView(R.layout.dialog_loading);

        btnlogout = findViewById(R.id.logout);
        logout = new Dialog(dashboard.this);
        logout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logout.setCancelable(false);
        logout.setContentView(R.layout.dialog_logout);

        btncancel = logout.findViewById(R.id.btncancel);
        btnconfirm = logout.findViewById(R.id.btnconfirm);

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.dismiss();
            }
        });

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.show();
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(dashboard.this,signin.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(dashboard.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            loading.dismiss();
                            Toast.makeText(dashboard.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.show();
            }
        });



        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, delivered.class);
                startActivity(intent);
                finish();
            }
        });
        ParseUser us = ParseUser.getCurrentUser();
        if (us != null){
            dm_username = us.getUsername();
            user.setText(dm_username);
        }
        ParseQuery<ParseObject> confirmed = new ParseQuery<ParseObject>("OnlineOrders");
        confirmed.whereEqualTo("status","outfordelivery");
        confirmed.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                loading.show();
                if (e == null){
                    for (ParseObject o:objects){
                        allordernumber.add(o.getString("orderNumber"));
                    }
                    hashSet.addAll(allordernumber);
                    allordernumber.clear();
                    allordernumber.addAll(hashSet);
                    odm = new ArrayList<>();
                    if (allordernumber.isEmpty()){
                        loading.dismiss();
                    }else{
                        for (int ord = 0; ord < allordernumber.size();ord++){
                            ParseQuery<ParseObject> getorder = new ParseQuery<ParseObject>("OnlineOrders");
                            getorder.whereEqualTo("orderNumber",allordernumber.get(ord));
                            getorder.setLimit(1);
                            getorder.cancel();
                            getorder.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null){
                                        for (ParseObject o:objects){
                                            customer = o.getString("firstname") + " " + o.getString("lastname");
                                            address = o.getString("address");
                                            contact = o.getString("contactnumber");
                                            totalpayment = o.getInt("totalPayment");
                                            ordernumber = o.getString("orderNumber");
                                            stts = o.getString("status");
                                            orderDM ord = new orderDM(stts,customer,address,contact,totalpayment,ordernumber);
                                            odm.add(ord);
                                        }
                                        loading.dismiss();
                                        orderlists.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                                        orderlists.setAdapter(new orderRA(odm));
                                    }else{
                                        loading.dismiss();
                                        Toast.makeText(dashboard.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                }else{
                    loading.dismiss();
                    Toast.makeText(dashboard.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}