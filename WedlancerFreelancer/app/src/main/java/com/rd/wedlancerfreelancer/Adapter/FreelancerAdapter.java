package com.rd.wedlancerfreelancer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rd.wedlancerfreelancer.FreelancerDetailsActivity;
import com.rd.wedlancerfreelancer.R;
import com.rd.wedlancerfreelancer.Models.Freelancers;
import com.rd.wedlancerfreelancer.Utils.Config;

import java.util.List;

public class FreelancerAdapter extends RecyclerView.Adapter<FreelancerAdapter.ViewHolder> {

    public List<Freelancers> objFreelancerList;
//    String name,fn,ln,category,fusername,img;
    Context context;


    // Constructor for initialization
    public FreelancerAdapter(Context context, List<Freelancers> objFreelancerList) {
        this.context = context;
        this.objFreelancerList = objFreelancerList;
    }

    @NonNull
    @Override
    public FreelancerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlefreelancerdesign, parent, false);

        return new ViewHolder(view);

    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull FreelancerAdapter.ViewHolder holder, int position) {

        Freelancers freelancerlist = objFreelancerList.get(position);

        //Concate string
        String fn=freelancerlist.getFirstname();
        String ln=freelancerlist.getLastname();
        String img=freelancerlist.getProfilePicture();
        String name=fn+" "+ln;
        //Concate string
        String fusername=freelancerlist.getUserName();
        String category=freelancerlist.getCategory();
        holder.txtfreelancername.setText(name);
        holder.txtcatgory.setText(freelancerlist.getCategory());
        Glide.with(context).load(freelancerlist.getProfilePicture()).into(holder.profileimage);
        holder.card_view.setOnClickListener(v -> {
            Intent intent=new Intent(context, FreelancerDetailsActivity.class);
            intent.putExtra("fusername",fusername);
            intent.putExtra("category",category);
            intent.putExtra("name",name);
            intent.putExtra("img",img);
            context.startActivity(intent);
        });

        //holder.profileimage.setImageResource(profileimage.getProfilePicture());
    }

    @Override
    public int getItemCount() {
        return objFreelancerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileimage;
        TextView txtfreelancername,txtcatgory;
        CardView card_view;


        public ViewHolder(View view) {
            super(view);
            txtfreelancername = view.findViewById(R.id.txtfreelancername);
            profileimage = view.findViewById(R.id.profileimage);
            txtcatgory=view.findViewById(R.id.txtcatgory);
            card_view=view.findViewById(R.id.card_view);
        }
    }


}