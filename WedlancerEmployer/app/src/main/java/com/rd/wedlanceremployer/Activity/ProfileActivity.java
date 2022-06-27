package com.rd.wedlanceremployer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlanceremployer.Adapter.BookingAdapter;
import com.rd.wedlanceremployer.Models.Bookings;
import com.rd.wedlanceremployer.Models.Freelancers;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView edtname,edtphonenumber, edtemail,edtpassword;
    Button btnupdate;
    Freelancers freelancerlist;
    SharedPreferences pref;

    ProgressDialog dialog;//progressdialog
    //SharedPreferences pref;
    String username = "", phno, fn, ln;
    BigInteger ph;
    CircleImageView profileimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pref=getApplication().getSharedPreferences("freelancing",0);
        pref = getApplication().getSharedPreferences("freelancing", 0);
        username = pref.getString("username", "");
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();

        edtname = findViewById(R.id.edtname);
        edtphonenumber = findViewById(R.id.edtphonenumber);
        edtemail = findViewById(R.id.edtemail);
        profileimage = findViewById(R.id.profileimage);
        btnupdate = findViewById(R.id.btnupdate);
        //edtpassword = findViewById(R.id.edtpassword);

        //progressdialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        dialog.show();//progressdialog
        //API
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Profiles/profiledetails?username=" + username, response -> {
            //Toast.makeText(this, "" + response, Toast.LENGTH_SHORT).show();

            dialog.dismiss();//progressdialog

            //Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
            Gson gson = new Gson(); //father of json
            //collection type=fetch multiple data better than string
            Type collectionType = new TypeToken<List<Freelancers>>() {
            }.getType();
            freelancerlist = gson.fromJson(response, Freelancers.class);
            fn = freelancerlist.getFirstname();
            ln = freelancerlist.getLastname();
            ph = freelancerlist.getPhonenumber();
            phno = ph.toString();
            edtname.setText(fn + " " + ln);
            edtphonenumber.setText(phno);
            edtemail.setText(freelancerlist.getEmail());
            //edtpassword.setText(freelancerlist.getEmail());
            Glide.with(this).load(freelancerlist.getProfilePicture()).error(R.drawable.tic).into(profileimage);


        }, error -> Toast.makeText(ProfileActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show());

        //progressdialog
        str.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //progressdialog

        RequestQueue q = Volley.newRequestQueue(ProfileActivity.this);
        q.add(str);
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