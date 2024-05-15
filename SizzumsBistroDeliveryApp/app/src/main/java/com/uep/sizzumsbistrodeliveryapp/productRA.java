package com.uep.sizzumsbistrodeliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class productRA extends RecyclerView.Adapter<productRA.productVH> {
    Context context;
    ArrayList<productDM> pdm;

    public productRA(ArrayList<productDM> pdm){
        this.pdm = pdm;
    }
    @NonNull
    @Override
    public productVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pendingdetails_productlayout,parent,false);
        return new productVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productVH holder, int position) {
        holder.name.setText(pdm.get(position).getName());
        holder.qty.setText(pdm.get(position).getQty());
        holder.image.setImageDrawable(pdm.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return pdm.size();
    }

    public class productVH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,qty;

        public productVH(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            image = itemView.findViewById(R.id.productimage);
            name = itemView.findViewById(R.id.productname);
            qty = itemView.findViewById(R.id.productqty);
        }
    }
}
