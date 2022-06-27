package com.rd.wedlancerfreelancer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rd.wedlanceremployer.Models.Portfolio;
import com.rd.wedlancerfreelancer.Models.Bookings;
import com.rd.wedlancerfreelancer.R;
import com.rd.wedlancerfreelancer.ReviewActivity;

import java.util.ArrayList;
import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder> {

    List<Portfolio> imglist;
    Context context;

    // Constructor for initialization
    public PortfolioAdapter(Context context, List<Portfolio> objBookingList) {
        this.context = context;
        imglist = objBookingList;
    }

    @NonNull
    @Override
    public PortfolioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portfolio, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(imglist.get(position).getImage()).placeholder(R.drawable.img_error).error(R.drawable.img_error).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return imglist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);

        }
    }
}
