package com.example.k_mart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestCardAdapter extends RecyclerView.Adapter<RequestCardAdapter.MyViewHolder> {

    ArrayList<RequestDataToShow> list;
    private Context context;
    private RequestCardAdapter.ItemClickListener clickListener;

    public RequestCardAdapter(Context context, ArrayList<RequestDataToShow> list, RequestCardAdapter.ItemClickListener l){
        this.list = list;
        this.context = context;
        this.clickListener = l;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_frag_recyclerview_view, parent, false);
        return new RequestCardAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RequestDataToShow d = list.get(position);
        int pos = position;
        holder.first.setText(list.get(position).getFirst());
        holder.second.setText(list.get(position).getSecond());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.OnItemClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView first,second;
        Context context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            first = itemView.findViewById(R.id.req_frag_card_firsttext);
            second = itemView.findViewById(R.id.req_frag_card_secondtext);

        }
    }

    public interface ItemClickListener{
        void OnItemClick(int pos);
    }
}
