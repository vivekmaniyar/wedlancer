package com.rd.wedlancerfreelancer;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edtusername, edtfn, edtln, edtemail, edtphonenumber, edtpassword;
    Button btnsubmit;
    Spinner cityspinner, categoryspinner;
    ProgressDialog dialog;//progressdialog
    List<String> Category_ID, Category_Name, City_ID, City_Name;

    String selectedcity = "", selectedcategory = "", Cat_ID = "", Cat_Name = "", Ct_ID = "", Ct_Name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progressdialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait for response...");
        dialog.setCancelable(false);
        //progressdialog

        edtusername = findViewById(R.id.edtusername);
        edtfn = findViewById(R.id.edtfn);
        edtln = findViewById(R.id.edtln);
        edtemail = findViewById(R.id.edtemail);
        edtphonenumber = findViewById(R.id.edtphonenumber);
        edtpassword = findViewById(R.id.edtpassword);
        btnsubmit = findViewById(R.id.btnsubmit);

        cityspinner = findViewById(R.id.cityspinner);
        categoryspinner = findViewById(R.id.categoryspinner);

        Category_ID = new ArrayList<>();
        Category_Name = new ArrayList<>();
        City_ID = new ArrayList<>();
        City_Name = new ArrayList<>();

        //category spinner
        loadcategory();

        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedcategory = Category_Name.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //category spinner

        //city spinner
        loadcity();
        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedcity = City_Name.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //city spinner


        btnsubmit.setOnClickListener(v -> {

            String username = edtusername.getText().toString();
            String firstName = edtfn.getText().toString();
            String lastName = edtln.getText().toString();
            String email = edtemail.getText().toString();
            String phoneNumber = edtphonenumber.getText().toString();
            String category = categoryspinner.getSelectedItem().toString();
            String city = cityspinner.getSelectedItem().toString();
            String password = edtpassword.getText().toString();

            if (edtusername.getText().toString().equals("")) {

                Toast.makeText(RegisterActivity.this, "Enter username !", Toast.LENGTH_SHORT).show();
            } else if (edtfn.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter firstname !", Toast.LENGTH_SHORT).show();
            } else if (edtln.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter lastname !", Toast.LENGTH_SHORT).show();
            } else if (edtemail.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter email !", Toast.LENGTH_SHORT).show();
            } else if (edtphonenumber.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter phonenumber !", Toast.LENGTH_SHORT).show();
//            }else if (categoryspinner.getSelectedItem().toString().equals("")) {
//                Toast.makeText(RegisterActivity.this, "Enter phonenumber !", Toast.LENGTH_SHORT).show();
//            }  else if (cityspinner.getSelectedItem().toString().trim().equals("Select City")) {
//                Toast.makeText(RegisterActivity.this, "select city !", Toast.LENGTH_SHORT).show();
            } else if (edtpassword.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "Enter password !", Toast.LENGTH_SHORT).show();
            } else {
                //dialog.show();//progressdialog
                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put("username", username);
                postParam.put("firstName", firstName);
                postParam.put("lastName", lastName);
                postParam.put("email", email);
                postParam.put("phoneNumber", phoneNumber);
                postParam.put("category", category);
                postParam.put("city", city);
                postParam.put("password", password);
                //API
                JsonObjectRequest str = new JsonObjectRequest(Request.Method.POST, "https://wedlancer.azurewebsites.net/api/Auth/registerfreelancer", new JSONObject(postParam), new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        //dialog.dismiss();//progressdialog

                        try {
                            JSONObject obj = response;
                            String status = obj.getString("status");
                            if (status.equals("Success")) {

                                Toast.makeText(RegisterActivity.this, "Register Successfull!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, error -> Toast.makeText(RegisterActivity.this, "Username Already Existed!", Toast.LENGTH_SHORT).show()) {

                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("firstName", firstName);
                        params.put("lastName", lastName);
                        params.put("phoneNumber", phoneNumber);
                        params.put("email", email);
                        params.put("category", category);
                        params.put("city", city);
                        params.put("password", password);
                        return params;
                    }
                };

                //progressdialog
                str.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //progressdialog

                RequestQueue q = Volley.newRequestQueue(RegisterActivity.this);
                q.add(str);
                //API
            }

        });
    }

    //load category
    void loadcategory() {
        //API
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Categories", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(HomeActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        Cat_ID = obj.getString("categoryId");
                        Cat_Name = obj.getString("categoryName");


                        Category_ID.add(Cat_ID);
                        Category_Name.add(Cat_Name);


                    }
                    //String deaultvalue = "Select Category";
                    ArrayAdapter adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_dropdown_item_1line, Category_Name);
                    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categoryspinner.setAdapter(adapter);
//                    if (deaultvalue != null) {
//                        int spinnerPosition = adapter.getPosition(deaultvalue);
//                        categoryspinner.setSelection(spinnerPosition);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Cat_ID", Cat_ID);
                return params;
            }
        };

        RequestQueue q = Volley.newRequestQueue(RegisterActivity.this);
        q.add(str);

        //API
    }

    //load category
    //load city
    void loadcity() {
        //API
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Cities", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(RegisterActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        Ct_ID = obj.getString("cityId");
                        Ct_Name = obj.getString("cityName");


                        City_ID.add(Ct_ID);
                        City_Name.add(Ct_Name);


                    }

                    ArrayAdapter adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_dropdown_item_1line, City_Name);
                    adapter.notifyDataSetChanged();
                    cityspinner.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Ct_ID", Ct_ID);
                return params;
            }
        };

        RequestQueue q = Volley.newRequestQueue(RegisterActivity.this);
        q.add(str);

        //API
    }

    //load city

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}