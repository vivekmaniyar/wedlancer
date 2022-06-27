package com.rd.wedlanceremployer.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlanceremployer.Adapter.PortfolioAdapter;
import com.rd.wedlanceremployer.Adapter.RecyclerViewAdapter;
import com.rd.wedlanceremployer.Models.Freelancers;
import com.rd.wedlanceremployer.Utils.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableFreelancerPortfolioActivity extends AppCompatActivity {

    SharedPreferences pref;
    String name, category, img, startdate, enddate,token,username,fusername;
    ImageView profileimage;
    TextView txtfreelancername, txtcatgory;
    Button btnbook,btnreview;
    ProgressDialog dialog;//progressdialog
    Freelancers freelancerslist;
    RecyclerView list, videolist;
    List<com.rd.wedlanceremployer.Models.Portfolio> portfolioList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_freelancer_portfolio);
        getSupportActionBar().setTitle("Freelancer's Portfolio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pref = getApplication().getSharedPreferences("freelancing", 0);
        //Toast.makeText(this, ""+pref, Toast.LENGTH_SHORT).show();
        token = pref.getString("token", "");
        //Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
        username = pref.getString("username", "");
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();
        //progressdialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        //get data from availabefreelanceractivity
        startdate = getIntent().getStringExtra("startdate");

        //Toast.makeText(this, ""+startdate, Toast.LENGTH_SHORT).show();
        enddate = getIntent().getStringExtra("enddate");
        //Toast.makeText(this, ""+enddate, Toast.LENGTH_SHORT).show();
        name = getIntent().getStringExtra("name");
        category = getIntent().getStringExtra("category");
        fusername = getIntent().getStringExtra("fusername");
        //Toast.makeText(this, ""+fusername, Toast.LENGTH_SHORT).show();
        img = getIntent().getStringExtra("img");
        //get data from availabefreelanceractivity
        Log.e("startdate:----------------",startdate);
        Log.e("enddate:----------------",enddate);
        Log.e("fusername:----------------",fusername);
        Log.e("username:----------------",username);

        txtfreelancername = findViewById(R.id.txtfreelancername);
        txtcatgory = findViewById(R.id.txtcatgory);
        btnbook = findViewById(R.id.btnbook);
        btnreview = findViewById(R.id.btnreview);
        profileimage = findViewById(R.id.profileimage);
        list = findViewById(R.id.list);
        videolist = findViewById(R.id.videolist);

        txtfreelancername.setText(name);
        txtcatgory.setText(category);

        Glide.with(AvailableFreelancerPortfolioActivity.this).load(img).into(profileimage);

        list.setLayoutManager(new GridLayoutManager(this, 2));
        videolist.setLayoutManager(new LinearLayoutManager(this));

        dialog.show();//progressdialog

        //PortfolioAPI
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Profiles/searchportfolio?username=" + fusername, response -> {

            dialog.dismiss();//progressdialog

            Gson gson = new Gson(); //father of json
            //collection type=fetch multiple data better than string
            Type collectionType = new TypeToken<List<com.rd.wedlanceremployer.Models.Portfolio>>() {

            }.getType();
            portfolioList = gson.fromJson(response, collectionType);

            list.setAdapter(new PortfolioAdapter(AvailableFreelancerPortfolioActivity.this, portfolioList));
            videolist.setAdapter(new RecyclerViewAdapter(AvailableFreelancerPortfolioActivity.this, portfolioList, this.getLifecycle()));


        }, error -> Toast.makeText(AvailableFreelancerPortfolioActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show());

        //progressdialog
        str.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue q = Volley.newRequestQueue(AvailableFreelancerPortfolioActivity.this);
        q.add(str);
        //PortfolioAPI



        //Booking API
        btnbook.setOnClickListener(v -> {
            dialog.show();//progressdialog
            if (pref.getBoolean("islogin", false)) {

                JSONObject map = new JSONObject();
                try {
                    map.put("startDate", startdate);
                    map.put("endDate", enddate);
                    map.put("employerusername", username);
                    map.put("freelancerusername",fusername);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //API
                JsonObjectRequest strb = new JsonObjectRequest(Request.Method.POST, "https://wedlancer.azurewebsites.net/api/Bookings",map, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            JSONObject obj=response;
                            String status= obj.getString("status");
                            if(status.equals("Success"))
                            {

                                Intent intent = new Intent(AvailableFreelancerPortfolioActivity.this, MydashboardActivity.class);
                                Toast.makeText(AvailableFreelancerPortfolioActivity.this, "Freelancer Booked Successfully", Toast.LENGTH_SHORT).show();
                                intent.putExtra("username",username);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(AvailableFreelancerPortfolioActivity.this, "Booking Pending!", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AvailableFreelancerPortfolioActivity.this, "Login Required!" , Toast.LENGTH_SHORT).show();
                    }
                }) {
                    //This is for Headers If You Needed
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                       // params.put("Content-Type", "application/json; charset=UTF-8");
//                        params.put("token", token);
                        params.put("Authorization","Bearer "+token);
                        return params;
                    }
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        Log.e("Startate: - ",startdate);
                        Log.e("Endate: - ",enddate);
                        Log.e("username: - ",username);
                        Log.e("fusername: - ",fusername);
                        params.put("startDate", startdate);
                        params.put("endDate", enddate);
                        params.put("employerusername", username);
                        params.put("freelancerusername",fusername);
                        return params;
                    }
                };

                //progressdialog
                strb.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //progressdialog

                RequestQueue qb = Volley.newRequestQueue(AvailableFreelancerPortfolioActivity.this);
                qb.add(strb);
                //API
            } else {
                Toast.makeText(AvailableFreelancerPortfolioActivity.this, "Login Required", Toast.LENGTH_SHORT).show();
            }

        });
        //Booking API


        //REVIEW
        btnreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvailableFreelancerPortfolioActivity.this, ReviewListActivity.class);
                intent.putExtra("fusername", fusername);
                startActivity(intent);

            }
        });
        //REVIEW


    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}