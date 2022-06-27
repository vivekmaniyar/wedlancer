package com.rd.wedlanceremployer.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    EditText edtreview;
    Button btnreview;
    RatingBar ratingBar;
    ProgressDialog dialog;
    String username = "", fusername = "", token;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().setTitle("Rating-Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplication().getSharedPreferences("freelancing", 0);
        token = pref.getString("token", "");
        //Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);

        username = getIntent().getStringExtra("username");
        fusername = getIntent().getStringExtra("fusername");
        //Toast.makeText(this, ""+fusername, Toast.LENGTH_SHORT).show();

        btnreview = (Button) findViewById(R.id.btnreview);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        edtreview = (EditText) findViewById(R.id.edtreview);

        btnreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float rating = ratingBar.getRating();
                String message = edtreview.getText().toString();
                String nrate = String.valueOf(rating);

                Log.e("TAG", "onClick: " + ratingBar.getRating());
                //Toast.makeText(ReviewActivity.this, token, Toast.LENGTH_SHORT).show();
                if (rating.equals("") || rating.equals("0")) {
                    Toast.makeText(ReviewActivity.this, "Give rating", Toast.LENGTH_SHORT).show();
                } else if (message.equals("")) {
                    Toast.makeText(ReviewActivity.this, "Enter Review", Toast.LENGTH_SHORT).show();
                } else {

                    //dialog.show();
                    Map<String, String> postParam = new HashMap<String, String>();
                    Log.e("TAG", "onClick:= " + nrate.substring(0, nrate.length() - 2));
                    postParam.put("rating", nrate.substring(0, nrate.length() - 2));
                    postParam.put("message", message);
                    postParam.put("employeerusername", username);
                    postParam.put("freelancerusername", fusername);
                    //API
                    JsonObjectRequest str = new JsonObjectRequest(Request.Method.POST, "https://wedlancer.azurewebsites.net/api/Reviews", new JSONObject(postParam), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //dialog.dismiss();
                            JSONObject obj = response;

                            Toast.makeText(ReviewActivity.this, "Thank You For Review !!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ReviewActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ReviewActivity.this, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        //                        //This is for Headers If You Needed
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//                            // params.put("Content-Type", "application/json; charset=UTF-8");
////                        params.put("token", token);
//                            params.put("Authorization","Bearer "+token);
//                            return params;
//                        }
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("rating", String.valueOf("5"));
                            params.put("message", "message");
                            params.put("employeerusername", username);
                            params.put("freelancerusername", fusername);
                            return params;
                        }
                    };

                    str.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                    RequestQueue q = Volley.newRequestQueue(ReviewActivity.this);
                    q.add(str);

                    //API
                }

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}