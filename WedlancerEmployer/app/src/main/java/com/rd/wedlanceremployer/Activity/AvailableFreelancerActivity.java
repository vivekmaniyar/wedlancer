package com.rd.wedlanceremployer.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlanceremployer.Adapter.AvailableFreelancerAdapter;
import com.rd.wedlanceremployer.Models.Freelancers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class AvailableFreelancerActivity extends AppCompatActivity {
    RecyclerView freelancerrecyclerview;
    List<Freelancers> objFreelancerList;//Model name
    ProgressDialog dialog;//progressdialog
    AvailableFreelancerAdapter adapter;
    TextView notfound;
    String category,city,startdate,enddate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_freelancer);

        getSupportActionBar().setTitle("Freelancers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progressdialog
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        category=getIntent().getStringExtra("category");
        city=getIntent().getStringExtra("city");
        startdate=getIntent().getStringExtra("startdate");
        enddate=getIntent().getStringExtra("enddate");
        //Toast.makeText(this, ""+startdate+""+enddate, Toast.LENGTH_SHORT).show();

        freelancerrecyclerview=findViewById(R.id.freelancerrecyclerview);
        notfound=findViewById(R.id.notfound);
        objFreelancerList=new ArrayList<>();
        adapter=new AvailableFreelancerAdapter(AvailableFreelancerActivity.this,objFreelancerList, startdate, enddate);
        freelancerrecyclerview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        freelancerrecyclerview.setLayoutManager(linearLayoutManager);
        objFreelancerList = new ArrayList<Freelancers>();

        //dialog.show();//progressdialog
        //API
        StringRequest str=new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Profiles/availableprofiles?startdate="+startdate+"&enddate="+startdate+"&city="+city+"&category="+category, response -> {
            //dialog.dismiss();
            if(response.equals("[]"))
            {
                notfound.setVisibility(View.VISIBLE);
            }else{
                notfound.setVisibility(View.GONE);
            }
            //Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
            Gson gson = new Gson(); //father of json
            //collection type=fetch multiple data better than string
            Type collectionType = new TypeToken<List<Freelancers>>() {
            }.getType();
            objFreelancerList = gson.fromJson(response, collectionType);
            adapter = new AvailableFreelancerAdapter(this, objFreelancerList,startdate,enddate);
            freelancerrecyclerview.setAdapter(adapter);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AvailableFreelancerActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("startdate",startdate);
                params.put("enddate",enddate);
                return params;
            }
        };

        RequestQueue q= Volley.newRequestQueue(AvailableFreelancerActivity.this);
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
