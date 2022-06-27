package com.rd.wedlanceremployer.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlanceremployer.Models.Freelancers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerlayout;//MENU
    SharedPreferences pref;


    LinearLayout lllogin, llreg, llhome, llfreelancer, llmydashboard, llcontactus, llfaq, llaboutus, lllogout, llselectdate, lltodate, llfromdate;
    View viewlogin, viewreg, viewhome, viewdashboard, viewlogout;
    ImageView btnsdate, btnedate;
    TextView edtstartdate, edtenddate;
    Button btnsearch;
    Spinner categoryspinner, cityspinner;
    boolean isOpen = false;

    List<String> Category_ID, Category_Name, City_ID, City_Name;
    List<Freelancers> objFreelancerList;

    String selectedcity = "", selectedcategory = "", Cat_ID = "", Cat_Name = "", Ct_ID = "", Ct_Name = "", username = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getApplication().getSharedPreferences("freelancing", 0);
        username = pref.getString("username", "");
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();

        llselectdate = findViewById(R.id.llselectdate);
        lltodate = findViewById(R.id.lltodate);
        llfromdate = findViewById(R.id.llfromdate);
        lllogin = findViewById(R.id.lllogin);
        llreg = findViewById(R.id.llreg);
        llhome = findViewById(R.id.llhome);
        llfreelancer = findViewById(R.id.llfreelancer);
        llmydashboard = findViewById(R.id.llmydashboard);
        llcontactus = findViewById(R.id.llcontactus);
        llaboutus = findViewById(R.id.llaboutus);
        llfaq = findViewById(R.id.llfaq);
        lllogout = findViewById(R.id.lllogout);
        viewreg = findViewById(R.id.viewreg);
        viewlogin = findViewById(R.id.viewlogin);
        viewhome = findViewById(R.id.viewhome);
        viewdashboard = findViewById(R.id.viewdashboard);
        viewlogout = findViewById(R.id.viewlogout);
        btnsearch = findViewById(R.id.btnsearch);
        categoryspinner = findViewById(R.id.categoryspinner);
        cityspinner = findViewById(R.id.cityspinner);
        btnedate = findViewById(R.id.btnedate);
        btnsdate = findViewById(R.id.btnsdate);
        edtstartdate = findViewById(R.id.edtstartdate);
        edtenddate = findViewById(R.id.edtenddate);

        Category_ID = new ArrayList<>();
        Category_Name = new ArrayList<>();
        City_ID = new ArrayList<>();
        City_Name = new ArrayList<>();
        objFreelancerList = new ArrayList<>();


        drawerlayout = findViewById(R.id.drawerlayout);
        //pref = getApplication().getSharedPreferences("freelancing", 0);
        //username=getIntent().getStringExtra("username");
//
        if (pref.getBoolean("islogin", false)) {

            lllogin.setVisibility(View.GONE);
            llreg.setVisibility(View.GONE);
            llmydashboard.setVisibility(View.VISIBLE);
            lllogout.setVisibility(View.VISIBLE);
            viewreg.setVisibility(View.GONE);
            viewlogout.setVisibility(View.VISIBLE);
            viewlogin.setVisibility(View.GONE);
            viewhome.setVisibility(View.GONE);
            viewdashboard.setVisibility(View.VISIBLE);

        } else {
            lllogin.setVisibility(View.VISIBLE);
            llreg.setVisibility(View.VISIBLE);
            llmydashboard.setVisibility(View.GONE);
            lllogout.setVisibility(View.GONE);
            viewreg.setVisibility(View.VISIBLE);
            viewlogout.setVisibility(View.GONE);
            viewlogin.setVisibility(View.VISIBLE);
            viewhome.setVisibility(View.VISIBLE);
            viewdashboard.setVisibility(View.GONE);
        }


        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);//Menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back


        llselectdate.setOnClickListener(view -> {
            if (!isOpen) {
                lltodate.setVisibility(View.VISIBLE);
                llfromdate.setVisibility(View.VISIBLE);
                isOpen = true;
            } else {
                lltodate.setVisibility(View.GONE);
                llfromdate.setVisibility(View.GONE);
                isOpen = false;
            }
        });

        lllogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        llreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        llhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
        llfreelancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FreelancerActivity.class);
                startActivity(intent);
            }
        });
        llmydashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MydashboardActivity.class);
                startActivity(intent);
            }
        });
        llcontactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ContactusActivity.class);
                startActivity(intent);
            }
        });
        llaboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AboutusActivity.class);
                startActivity(intent);
            }
        });
        llfaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FAQsActivity.class);
                startActivity(intent);
            }
        });
        lllogout.setOnClickListener(v -> {

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("islogin", false);
            editor.putString("token", "");
            editor.putString("expiration", "");
            editor.commit();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

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

        //startdate
        btnsdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edtstartdate.setText(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        //startdate

        //enddate
        btnedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edtenddate.setText(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                            }
                        }, mYear, mMonth, mDay + 1);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);
                datePickerDialog.show();
            }
        });
        //enddate

        //Search
        btnsearch.setOnClickListener(v -> {

            if (categoryspinner.getSelectedItem().toString().trim().equals("Select Category")) {
                Toast.makeText(HomeActivity.this, "Select Category ! !", Toast.LENGTH_SHORT).show();
            } else if (cityspinner.getSelectedItem().toString().trim().equals("Select City")) {
                Toast.makeText(HomeActivity.this, "Select City ! !", Toast.LENGTH_SHORT).show();
            } else if (edtstartdate.getText().toString().equals("")) {
                Toast.makeText(HomeActivity.this, "Enter Start Date ! !", Toast.LENGTH_SHORT).show();
            } else if (edtenddate.getText().toString()
                    .equals("")) {
                Toast.makeText(HomeActivity.this, "Enter End Date ! !", Toast.LENGTH_SHORT).show();

            } else {
                String sdate = edtstartdate.getText().toString();
                String edate = edtenddate.getText().toString();
                Intent intent = new Intent(HomeActivity.this, AvailableFreelancerActivity.class);
                intent.putExtra("category", selectedcategory);
                intent.putExtra("city", selectedcity);
                intent.putExtra("startdate", sdate);
                intent.putExtra("enddate", edate);
                startActivity(intent);


            }
        });
        //Search

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
                    ArrayAdapter adapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_dropdown_item_1line, Category_Name);
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
                Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
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

        RequestQueue q = Volley.newRequestQueue(HomeActivity.this);
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

                //Toast.makeText(HomeActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        Ct_ID = obj.getString("cityId");
                        Ct_Name = obj.getString("cityName");


                        City_ID.add(Ct_ID);
                        City_Name.add(Ct_Name);


                    }

                    ArrayAdapter adapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_dropdown_item_1line, City_Name);
                    adapter.notifyDataSetChanged();
                    cityspinner.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
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

        RequestQueue q = Volley.newRequestQueue(HomeActivity.this);
        q.add(str);

        //API
    }

    //load city


    //profile
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (pref.getBoolean("islogin", true)) {

            getMenuInflater().inflate(R.menu.main, menu);

        }
        return true;

    }
    //profile


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //profile
        int id = item.getItemId();

        //Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();

        //if (pref.getBoolean("islogin", false)) {
        if (id == R.id.badge) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }
//        } else {
//            //getSupportActionBar().hide();
//            Toast.makeText(HomeActivity.this, "Login Required", Toast.LENGTH_SHORT).show();
//        }
        //profile
        //Menu
        if (item.getItemId() == android.R.id.home) {
            if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
                drawerlayout.closeDrawer(Gravity.LEFT);
            } else {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        }
        return super.onOptionsItemSelected(item);
        //Menu(Navigation drawer)

    }

}