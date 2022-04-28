package com.example.k_mart;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BuyCardAdapter extends RecyclerView.Adapter<BuyCardAdapter.MyViewHolder> {

    ArrayList<DataToShow> list;
    private Context context;
    private ItemClickListener clickListener;

    public BuyCardAdapter(Context context,ArrayList<DataToShow> list, ItemClickListener l){
        this.list = list;
        this.context = context;
        this.clickListener = l;
    }


    @NonNull
    @Override
    public BuyCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_buy, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyCardAdapter.MyViewHolder holder, int position) {
        //holder.name.setText(list.get(position));
        DataToShow d = list.get(position);
        int pos = position;
        holder.name.setText(d.getFirst());
        holder.location.setText(d.getSecond());
        holder.price.setText(d.getThird());
        Glide.with(context).load(d.getImageUrl()).centerCrop().placeholder(R.drawable.logofinal).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.OnItemClick(pos);
            }
        });

        //holder.image.setImageBitmap(d.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView name, location, price;
        Context context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.card_buy_name);
            location = itemView.findViewById(R.id.card_buy_location);
            price = itemView.findViewById(R.id.card_buy_price);
            image = itemView.findViewById(R.id.card_buy_item_image);

        }
    }

    public interface ItemClickListener{
        void OnItemClick(int pos);
    }
}
