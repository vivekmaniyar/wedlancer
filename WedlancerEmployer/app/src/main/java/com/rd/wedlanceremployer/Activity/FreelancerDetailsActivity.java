package com.rd.wedlanceremployer.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.rd.wedlanceremployer.Models.Portfolio;
import com.rd.wedlanceremployer.Slider.SliderAdapter;
import com.rd.wedlanceremployer.Slider.SliderItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreelancerDetailsActivity extends AppCompatActivity {

    SharedPreferences pref;
    String name,category,fusername,img;
    TextView txtfreelancername,txtcatgory;
    Button btnreview;
    ProgressDialog dialog;//progressdialog
    ImageView profileimage;
    Freelancers freelancerslist;
    RecyclerView list, videolist;
    List<com.rd.wedlanceremployer.Models.Portfolio> portfolioList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_details);

        getSupportActionBar().setTitle("Freelancer's Portfolio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back

        //progressdialog
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        //get data from freelanceractivity
        name=getIntent().getStringExtra("name");
        Log.e("TAG", "onCreate: "+name );

        category=getIntent().getStringExtra("category");
        fusername=getIntent().getStringExtra("fusername");
        Toast.makeText(this, ""+fusername, Toast.LENGTH_SHORT).show();
        img = getIntent().getStringExtra("img");
        //get data from freelanceractivity

        txtfreelancername=findViewById(R.id.txtfreelancername);
        txtcatgory=findViewById(R.id.txtcatgory);
        btnreview=findViewById(R.id.btnreview);
        profileimage = findViewById(R.id.profileimage);
        list = findViewById(R.id.list);
        videolist = findViewById(R.id.videolist);

        txtfreelancername.setText(name);
        txtcatgory.setText(category);
        Glide.with(FreelancerDetailsActivity.this).load(img).into(profileimage);

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

            list.setAdapter(new PortfolioAdapter(FreelancerDetailsActivity.this, portfolioList));
            videolist.setAdapter(new RecyclerViewAdapter(FreelancerDetailsActivity.this, portfolioList, this.getLifecycle()));


        }, error -> Toast.makeText(FreelancerDetailsActivity.this, "Server Error :" +" "+ error.getMessage(), Toast.LENGTH_SHORT).show());

        //progressdialog
        str.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue q = Volley.newRequestQueue(FreelancerDetailsActivity.this);
        q.add(str);
        //PortfolioAPI



        //REVIEW
        btnreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreelancerDetailsActivity.this, ReviewListActivity.class);
                intent.putExtra("fusername",fusername);
                startActivity(intent);
            }
        });
        //REVIEW
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}