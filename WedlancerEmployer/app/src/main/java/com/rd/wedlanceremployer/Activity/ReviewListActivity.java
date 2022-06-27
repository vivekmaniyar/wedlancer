package com.rd.wedlanceremployer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlanceremployer.Adapter.ReviewAdapter;
import com.rd.wedlanceremployer.Models.Freelancers;
import com.rd.wedlanceremployer.Models.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity {
    RecyclerView reviewrecyclerview;
    List<Reviews> objreviewList;//Model name
    //List<Freelancers> objfreelancerlist;//Model name
    ReviewAdapter adapter;
    ProgressDialog dialog;//progressdialog
    String fusername;
    TextView notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        getSupportActionBar().setTitle("Reviews");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progressdialog
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        reviewrecyclerview=findViewById(R.id.reviewrecyclerview);
        notfound=findViewById(R.id.notfound);
        objreviewList=new ArrayList<>();
        //objfreelancerlist=new ArrayList<>();
        adapter=new ReviewAdapter(ReviewListActivity.this,objreviewList);
        reviewrecyclerview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewrecyclerview.setLayoutManager(linearLayoutManager);
        objreviewList = new ArrayList<Reviews>();
        //objfreelancerlist = new ArrayList<Freelancers>();


        //dialog.show();//progressdialog

        fusername = getIntent().getStringExtra("fusername");
        //Toast.makeText(this, ""+fusername, Toast.LENGTH_SHORT).show();
        //API
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Reviews/freelancerreviews?username="+fusername,response -> {
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
            Type collectionType = new TypeToken<List<Reviews>>() {
            }.getType();
            objreviewList = gson.fromJson(response, collectionType);
            //objfreelancerlist = gson.fromJson(response, collectionType);
            adapter = new ReviewAdapter(this, objreviewList);
            reviewrecyclerview.setAdapter(adapter);

        }, error -> Toast.makeText(ReviewListActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show());
        //progressdialog
        str.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //progressdialog

        RequestQueue q = Volley.newRequestQueue(ReviewListActivity.this);
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