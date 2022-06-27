package com.rd.wedlanceremployer.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlanceremployer.Adapter.FreelancerAdapter;
import com.rd.wedlanceremployer.Models.Freelancers;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactusActivity extends AppCompatActivity {

    EditText edtname, edtphonenumber, edtemail, edtmessage;
    Button btnsubmit;
    ProgressDialog dialog;//progressdialog
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        getSupportActionBar().setTitle("Contact us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplication().getSharedPreferences("freelancing", 0);

        //progressdialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait for response...");
        dialog.setCancelable(false);
        //progressdialog

        edtname = findViewById(R.id.edtname);
        edtphonenumber = findViewById(R.id.edtphonenumber);
        edtemail = findViewById(R.id.edtemail);
        edtmessage = findViewById(R.id.edtmessage);
        btnsubmit = findViewById(R.id.btnsubmit);


        btnsubmit.setOnClickListener(v -> {

            String name = edtname.getText().toString();
            String phonenumber = edtphonenumber.getText().toString();
            String email = edtemail.getText().toString();
            String message = edtmessage.getText().toString();

            if (edtname.getText().toString().equals("")) {

                Toast.makeText(ContactusActivity.this, "Please Enter name !", Toast.LENGTH_SHORT).show();
            } else if (edtphonenumber.getText().toString().equals("")) {
                Toast.makeText(ContactusActivity.this, "Please Enter phonenumber !", Toast.LENGTH_SHORT).show();
            } else if (edtemail.getText().toString().equals("")) {
                Toast.makeText(ContactusActivity.this, "Please Enter email !", Toast.LENGTH_SHORT).show();
            } else if (edtmessage.getText().toString().equals("")) {
                Toast.makeText(ContactusActivity.this, "Please Enter meassage !", Toast.LENGTH_SHORT).show();
            } else {
                if (pref.getBoolean("islogin", false)) {
                dialog.show();//progressdialog
                Map<String,String> postParam=new HashMap<String,String>();
                postParam.put("name",name);
                postParam.put("phonenumber",phonenumber);
                postParam.put("email",email);
                postParam.put("message",message);
                //API
                JsonObjectRequest str = new JsonObjectRequest(Request.Method.POST, "https://wedlancer.azurewebsites.net/api/Inquiries",new JSONObject(postParam), new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject obj = response;
                            String status = obj.getString("status");
                            if (status.equals("Success")) {
                                Toast.makeText(ContactusActivity.this, "Your Query Submitted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ContactusActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(ContactusActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, error -> Toast.makeText(ContactusActivity.this, "Server Error" +" "+ error.getMessage(), Toast.LENGTH_SHORT).show()) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name);
                        params.put("phonenumber", phonenumber);
                        params.put("email", email);
                        params.put("message", message);

                        return params;
                    }
                };

                //progressdialog
                str.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //progressdialog

                RequestQueue q = Volley.newRequestQueue(ContactusActivity.this);
                q.add(str);
                //API
            } else {
                Toast.makeText(ContactusActivity.this, "Login Required", Toast.LENGTH_SHORT).show();
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