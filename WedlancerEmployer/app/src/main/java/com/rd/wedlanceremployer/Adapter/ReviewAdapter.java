package com.rd.wedlanceremployer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import com.rd.wedlanceremployer.Activity.R;

import com.rd.wedlanceremployer.Models.Freelancers;
import com.rd.wedlanceremployer.Models.Reviews;


import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    public List<Reviews> objreviewList;
    //public List<Freelancers> objfreelancerlist;
    Context context;


    // Constructor for initialization
    public ReviewAdapter(Context context, List<Reviews> objreviewList) {
        this.context = context;
        this.objreviewList = objreviewList;
        //this.objfreelancerlist = objfreelancerlist;
        //,List<Freelancers>objfreelancerlist
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlereviewdesign, parent, false);

        return new ViewHolder(view);

    }

    // Binding data to the into specified position

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reviews reviewlist = objreviewList.get(position);
        //Freelancers freelancerlist = objfreelancerlist.get(position);

        holder.txtreview.setText(reviewlist.getMessage());
        holder.txtusername.setText(reviewlist.getEmployeerusername());
        holder.ratingBar.setRating(reviewlist.getRating());


    }

    // Binding data to the into specified position

    @Override
    public int getItemCount() {
        return objreviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtreview,txtusername;
        RatingBar ratingBar;


        public ViewHolder(View view) {
            super(view);
            txtreview = view.findViewById(R.id.txtreview);
            txtusername = view.findViewById(R.id.txtusername);
            ratingBar = view.findViewById(R.id.ratingBar);
        }
    }


}
