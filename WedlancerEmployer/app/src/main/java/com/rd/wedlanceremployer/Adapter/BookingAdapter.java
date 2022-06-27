package com.rd.wedlanceremployer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rd.wedlanceremployer.Activity.MydashboardActivity;
import com.rd.wedlanceremployer.Activity.R;
import com.rd.wedlanceremployer.Activity.ReviewActivity;
import com.rd.wedlanceremployer.Models.Bookings;


import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    public List<Bookings> objBookingList;
    String username="",fusername="";
    Context context;

    // Constructor for initialization
    public BookingAdapter(Context context, List<Bookings> objBookingList) {
        this.context = context;
        this.objBookingList = objBookingList;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlebookingdesign, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {

        Bookings bookingslist = objBookingList.get(position);

        //Concate string
//        fn=bookingslist.getFirstname();
//        ln=bookingslist.getLastname();
//        name=fn+" "+ln;
        //Concate string
        username=bookingslist.getEmployerusername();
        fusername=bookingslist.getFreelancerusername();
        //Toast.makeText(context, ""+username, Toast.LENGTH_SHORT).show();

        holder.txtbookingid.setText(String.valueOf(bookingslist.getBookingId()));
        holder.txtusername.setText(bookingslist.getEmployerusername());
        holder.txtfreelancername.setText(bookingslist.getFreelancerusername());
        holder.txtsdate.setText(bookingslist.getStartDate());
        holder.txtedate.setText(bookingslist.getEndDate());
        holder.txtstatus.setText(bookingslist.getStatus());


        holder.btnreview.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("fusername",fusername);
            context.startActivity(intent);
        });
        holder.btncom.setOnClickListener(v -> {
            Intent intent = new Intent(context, MydashboardActivity.class);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return objBookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtbookingid, txtfreelancername, txtusername, txtsdate, txtedate, txtstatus;
        Button btnreview,btncom;


        public ViewHolder(View view) {
            super(view);
            txtbookingid = view.findViewById(R.id.txtbookingid);
            txtfreelancername = view.findViewById(R.id.txtfreelancername);
            txtusername = view.findViewById(R.id.txtusername);
            txtsdate = view.findViewById(R.id.txtsdate);
            txtedate = view.findViewById(R.id.txtedate);
            txtstatus = view.findViewById(R.id.txtstatus);
            btnreview = view.findViewById(R.id.btnreview);
            btncom = view.findViewById(R.id.btncom);
        }
    }
}
