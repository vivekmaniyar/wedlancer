package com.rd.wedlanceremployer.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlanceremployer.Adapter.BookingAdapter;
import com.rd.wedlanceremployer.Adapter.FreelancerAdapter;
import com.rd.wedlanceremployer.Models.Bookings;
import com.rd.wedlanceremployer.Models.Freelancers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MydashboardActivity extends AppCompatActivity {

    RecyclerView bookingsrecyclerview;
    List<Bookings> objBookingList;//Model name
    BookingAdapter adapter;
    ProgressDialog dialog;//progressdialog
    String username="",token;
    TextView notfound;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydashboard);
        getSupportActionBar().setTitle("Bookings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplication().getSharedPreferences("freelancing", 0);
        token = pref.getString("token", "");
        username=pref.getString("username","");
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();

        //progressdialog
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        bookingsrecyclerview=findViewById(R.id.bookingsrecyclerview);
        notfound=findViewById(R.id.notfound);
        objBookingList=new ArrayList<>();
        adapter=new BookingAdapter(MydashboardActivity.this,objBookingList);
        bookingsrecyclerview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bookingsrecyclerview.setLayoutManager(linearLayoutManager);
        objBookingList = new ArrayList<Bookings>();

        //dialog.show();//progressdialog
        //API
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Profiles/employeerbookings?username="+username, response -> {


            //dialog.dismiss();//progressdialog
            if(response.equals("[]"))
            {
                notfound.setVisibility(View.VISIBLE);
            }else{
                notfound.setVisibility(View.GONE);
            }

            //Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
            Gson gson = new Gson(); //father of json
            //collection type=fetch multiple data better than string
            Type collectionType = new TypeToken<List<Bookings>>() {
            }.getType();
            objBookingList = gson.fromJson(response, collectionType);
            adapter = new BookingAdapter(this, objBookingList);
            bookingsrecyclerview.setAdapter(adapter);


        }, error -> Toast.makeText(MydashboardActivity.this, "Server Error !" + error.getMessage(), Toast.LENGTH_SHORT).show()){
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Content-Type", "application/json; charset=UTF-8");
//                        params.put("token", token);
                params.put("Authorization","Bearer "+token);
                return params;
            }
        };

        //progressdialog
        str.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //progressdialog

        RequestQueue q = Volley.newRequestQueue(MydashboardActivity.this);
        q.add(str);
        //API



    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}