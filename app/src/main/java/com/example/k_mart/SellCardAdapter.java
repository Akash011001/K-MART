package com.example.k_mart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class SellCardAdapter extends RecyclerView.Adapter<SellCardAdapter.MyViewHolder> {
    ArrayList<String> list;

    SellCardAdapter(ArrayList<String> list){
        this.list = list;
    }
    @NonNull
    @Override
    public SellCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_sell, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SellCardAdapter.MyViewHolder holder, int position) {
        holder.name.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView name, location, price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.card_buy_name);
            location = itemView.findViewById(R.id.card_buy_location);
            price = itemView.findViewById(R.id.card_buy_price);
            image = itemView.findViewById(R.id.card_buy_item_image);
        }
    }
}
