package com.rd.wedlancerfreelancer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rd.wedlancerfreelancer.HomeActivity;
import com.rd.wedlancerfreelancer.Models.Packages;
import com.rd.wedlancerfreelancer.PaymentActivity;
import com.rd.wedlancerfreelancer.R;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    public List<Packages> objPackageList;
    String username="";
    Context context;

    // Constructor for initialization
    public PackageAdapter(Context context, List<Packages> objPackageList, String username) {
        this.context = context;
        this.objPackageList = objPackageList;
        this.username=username;
    }

    @NonNull
    @Override
    public PackageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlepackagedesign, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Packages packageslist = objPackageList.get(position);

        String packagename= packageslist.getPackageName();
        Float price= packageslist.getPrice();

        holder.txtpackagename.setText(packageslist.getPackageName());
        holder.txtpdesc.setText(packageslist.getDescription());
        holder.txtprice.setText(String.valueOf(packageslist.getPrice()));
        holder.btnpay.setOnClickListener(v -> {
            Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("package",packagename);
            intent.putExtra("price",String.valueOf(packageslist.getPrice()));
            //Toast.makeText(context, "Thank you", Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return objPackageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtpackagename,txtpdesc,txtprice;
        Button btnpay;


        public ViewHolder(View view) {
            super(view);
            txtpackagename = view.findViewById(R.id.txtpackagename);
            txtpdesc = view.findViewById(R.id.txtpdesc);
            txtprice = view.findViewById(R.id.txtprice);
            btnpay = view.findViewById(R.id.btnpay);
        }
    }
}

