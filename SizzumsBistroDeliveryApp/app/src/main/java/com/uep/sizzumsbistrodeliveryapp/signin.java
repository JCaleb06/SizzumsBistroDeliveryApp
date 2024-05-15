package com.uep.sizzumsbistrodeliveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseUser;

public class signin extends AppCompatActivity {
    TextInputEditText user,pass;
    AppCompatButton btn;
    ImageView showhide;
    Dialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        user = findViewById(R.id.editUser);
        pass = findViewById(R.id.editpw);
        btn = findViewById(R.id.signin);
        showhide = findViewById(R.id.imgsh);
        loading = new Dialog(signin.this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setContentView(R.layout.dialog_loading);

        ParseUser u = ParseUser.getCurrentUser();
        if (u != null){
            if (u.getBoolean("emailVerified") == false){
                Intent intent = new Intent(signin.this,dashboard.class);
                startActivity(intent);
                finish();
            }
        }
        showhide.setOnClickListener(v->{
            if (pass.getInputType() == 129){
                pass.setInputType(97);
                showhide.setImageResource(R.drawable.ic_show);
            }else{
                pass.setInputType(129);
                showhide.setImageResource(R.drawable.ic_hide);
            }
        });
        btn.setOnClickListener(v->{
            if(user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
                loading.dismiss();
                Toast.makeText(signin.this, "Please input your user username and password!", Toast.LENGTH_SHORT).show();
            }else {
                if(pass.getText().length() < 8){
                    loading.dismiss();
                    Toast.makeText(signin.this, "Your password must be have at least 8 characters long!", Toast.LENGTH_SHORT).show();
                }else{
                    ParseUser.logInInBackground(user.getText().toString(), pass.getText().toString(), (ParseUser user, ParseException e)-> {
                        if (user != null){
                            Boolean emailVerified = user.getBoolean("emailVerified");
                            String role = user.getString("role");
                            if (emailVerified == false && role.contains("deliveryman")){
                                ParseUser us = ParseUser.getCurrentUser();
                                if (us != null){
                                    if (us.getBoolean("emailVerified") == false){
                                        Intent intent = new Intent(signin.this,dashboard.class);
                                        startActivity(intent);
                                        Toast.makeText(signin.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        loading.dismiss();
                                    }
                                }
                            }else{
                                loading.dismiss();
                                Toast.makeText(signin.this, "User did not confirm the e-mail!!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            loading.dismiss();
                            Toast.makeText(signin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}