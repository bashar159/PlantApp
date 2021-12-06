package com.example.task14.UserAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task14.Classes.OredersModel;
import com.example.task14.HelperAdapter.ToHelperAdapter;
import com.example.task14.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private Context context;
    private ArrayList<OredersModel> orderModels ;

    public OrderAdapter(Context context, ArrayList<OredersModel> orderModels) {
        this.context = context;
        this.orderModels = orderModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recylcer, parent, false);
        return new OrderAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(orderModels.get(position).getName_text());
        holder.tExp.setText(orderModels.get(position).gettExp());

        {
            if (orderModels.get(position).getRating().equals("expert")) {
                holder.ratingBar.setRating(5);

            } else if (orderModels.get(position).getRating().equals("high")) {
                holder.ratingBar.setRating(4);

            } else if (orderModels.get(position).getRating().equals("medium")) {
                holder.ratingBar.setRating(3.5f);

            } else if (orderModels.get(position).getRating().equals("low")) {
                holder.ratingBar.setRating(2.5f);
            }
        }

    }

    @Override
    public int getItemCount() {
        return orderModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name , tExp;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.orderTextName);
            tExp = itemView.findViewById(R.id.orderTypeOfExp);
            ratingBar = itemView.findViewById(R.id.orderRatingBar);

        }
    }
}
