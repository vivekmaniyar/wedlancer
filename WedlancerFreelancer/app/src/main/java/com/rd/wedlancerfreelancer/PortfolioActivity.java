package com.rd.wedlancerfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlancerfreelancer.Adapter.PortfolioAdapter;
import com.rd.wedlancerfreelancer.Adapter.RecyclerViewAdapter;
import com.rd.wedlancerfreelancer.Models.Freelancers;
import com.rd.wedlancerfreelancer.Slider.SliderAdapter;
import com.rd.wedlancerfreelancer.Slider.SliderItems;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PortfolioActivity extends AppCompatActivity {

    SharedPreferences pref;
    String username="";
    RecyclerView list, videolist;

    List<com.rd.wedlanceremployer.Models.Portfolio> portfolioList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        getSupportActionBar().setTitle("My Portfolio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back

        pref = getApplication().getSharedPreferences("freelancing", 0);
        username = pref.getString("username", "");

        list = findViewById(R.id.list);
        videolist = findViewById(R.id.videolist);

        list.setLayoutManager(new GridLayoutManager(this, 2));
        videolist.setLayoutManager(new LinearLayoutManager(this));

        //dialog.show();//progressdialog
        //PortfolioAPI
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Profiles/searchportfolio?username="+username, response -> {

            //dialog.dismiss();//progressdialog

            Gson gson = new Gson(); //father of json
            //collection type=fetch multiple data better than string
            Type collectionType = new TypeToken<List<com.rd.wedlanceremployer.Models.Portfolio>>() {
            }.getType();

            portfolioList = gson.fromJson(response, collectionType);

            list.setAdapter(new PortfolioAdapter(PortfolioActivity.this, portfolioList));
            videolist.setAdapter(new RecyclerViewAdapter(PortfolioActivity.this, portfolioList, this.getLifecycle()));




        }, error -> Toast.makeText(PortfolioActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show());

        //progressdialog
        str.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //progressdialog

        RequestQueue q = Volley.newRequestQueue(PortfolioActivity.this);
        q.add(str);
        //PortfolioAPI




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