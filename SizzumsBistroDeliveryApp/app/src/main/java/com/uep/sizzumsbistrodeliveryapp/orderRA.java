package com.uep.sizzumsbistrodeliveryapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class orderRA extends RecyclerView.Adapter<orderRA.orderVH> {
    ArrayList<orderDM> odm;
    Context context;

    public orderRA(ArrayList<orderDM> odm) {
        this.odm = odm;
    }

    @NonNull
    @Override
    public orderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlayout, parent, false);
        return new orderVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull orderVH holder, @SuppressLint("RecyclerView") int position) {
        holder.customername.setText(odm.get(position).getCustomer());
        holder.totalpayment.setText(String.valueOf(odm.get(position).getTotalpayment()));
        holder.address.setText(odm.get(position).getAddress());
        holder.contactnumber.setText(odm.get(position).getContactnumber());
        holder.ordernumber.setText(odm.get(position).getOrdernumber());
        if (odm.get(position).getStatus().toString().contains("delivered")){
            holder.btncallnow.setVisibility(View.INVISIBLE);
        }else{
            holder.btncallnow.setVisibility(View.VISIBLE);
        }
        holder.seemore.setOnClickListener(v->{
            Intent intent = new Intent(context,pendingDetails.class);
            intent.putExtra("ordernumber",odm.get(position).getOrdernumber());
            intent.putExtra("status",odm.get(position).getStatus());
            context.startActivity(intent);
        });
        holder.btncallnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:"+holder.contactnumber.getText().toString()));
                if (ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},1);
                }else{
                    try{
                        context.startActivity(call);
                    }catch (SecurityException e){
                        Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return odm.size();
    }

    public class orderVH extends RecyclerView.ViewHolder {
        TextView customername, contactnumber, address, btncallnow, totalpayment,ordernumber;
        AppCompatButton seemore;

        public orderVH(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            ordernumber = itemView.findViewById(R.id.ordernumber);
            seemore = itemView.findViewById(R.id.seemore);
            customername = itemView.findViewById(R.id.customername);
            contactnumber = itemView.findViewById(R.id.contactnumber);
            address = itemView.findViewById(R.id.textView5);
            btncallnow = itemView.findViewById(R.id.btncallnow);
            totalpayment = itemView.findViewById(R.id.totalpayment);
        }
    }
}
