package com.rd.wedlancerfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
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
import com.rd.wedlancerfreelancer.Adapter.NewReviewAdapter;
import com.rd.wedlancerfreelancer.Adapter.ReviewAdapter;
import com.rd.wedlancerfreelancer.Models.Freelancers;
import com.rd.wedlancerfreelancer.Models.Reviews;
import com.rd.wedlancerfreelancer.Utils.Config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {
    RatingBar ratingBar;
    TextView txtreview, txtusername;
    String username;
    ProgressDialog dialog;//progressdialog
    List<Reviews> reviewsList = new ArrayList<>();
    SharedPreferences pref;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().setTitle("Reviews");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
        pref = getApplication().getSharedPreferences("freelancing", 0);
        //progressdialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        //get data from mydashboardactivity
        username = getIntent().getStringExtra("username");
        //get data from mydashboardactivity

        ratingBar = findViewById(R.id.ratingBar);
        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        txtreview = findViewById(R.id.txtreview);
        txtusername = findViewById(R.id.txtusername);


        //set data
        txtusername.setText(username);
        //set data

        //dialog.show();//progressdialog
        //API
        StringRequest strp = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Reviews/freelancerreviews?username=" + username, response -> {

            //dialog.dismiss();//progressdialog

            Gson gson = new Gson(); //father of json
            //collection type=fetch multiple data better than string
            Type collectionType = new TypeToken<List<Reviews>>() {
            }.getType();
            reviewsList = gson.fromJson(response, collectionType);
            list.setAdapter(new NewReviewAdapter(ReviewActivity.this,reviewsList));
//            Log.e("TAG", "onCreate:123 "+reviewsList.get(0).getMessage());
//            txtreview.setText(reviewsLis`t.get(0).getMessage());

        }, error -> Toast.makeText(ReviewActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                 params.put("Content-Type", "application/json; charset=UTF-8");
//                        params.put("token", Config.token);
                params.put("Authorization", "Bearer" + Config.token);
                return params;
            }
        };

        //progressdialog
        strp.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //progressdialog

        RequestQueue rq = Volley.newRequestQueue(ReviewActivity.this);
        rq.add(strp);
        //API

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}