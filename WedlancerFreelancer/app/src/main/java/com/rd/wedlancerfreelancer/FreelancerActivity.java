package com.rd.wedlancerfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlancerfreelancer.Adapter.FreelancerAdapter;
import com.rd.wedlancerfreelancer.Models.Freelancers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class FreelancerActivity extends AppCompatActivity {

    RecyclerView freelancerrecyclerview;
    List<Freelancers> objFreelancerList;//Model name
    FreelancerAdapter adapter;
    ProgressDialog dialog;//progressdialog
    String username="";
    TextView notfound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer);
        getSupportActionBar().setTitle("Freelancers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progressdialog
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        freelancerrecyclerview=findViewById(R.id.freelancerrecyclerview);
        notfound=findViewById(R.id.notfound);
        objFreelancerList=new ArrayList<>();
        adapter=new FreelancerAdapter(FreelancerActivity.this,objFreelancerList);
        freelancerrecyclerview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        freelancerrecyclerview.setLayoutManager(linearLayoutManager);
        objFreelancerList = new ArrayList<Freelancers>();


        dialog.show();//progressdialog
        //API
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Profiles/allprofiles", response -> {


            dialog.dismiss();//progressdialog
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
            adapter = new FreelancerAdapter(this, objFreelancerList);
            freelancerrecyclerview.setAdapter(adapter);

        }, error -> Toast.makeText(FreelancerActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show());

        //progressdialog
        str.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //progressdialog

        RequestQueue q = Volley.newRequestQueue(FreelancerActivity.this);
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