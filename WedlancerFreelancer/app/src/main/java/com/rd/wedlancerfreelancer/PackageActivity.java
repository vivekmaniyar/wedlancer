package com.rd.wedlancerfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlancerfreelancer.Adapter.PackageAdapter;
import com.rd.wedlancerfreelancer.Models.Packages;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class PackageActivity extends AppCompatActivity {

    SharedPreferences pref;
    RecyclerView packagerecyclerview;
    List<Packages> objPackageList;//Model name
    PackageAdapter adapter;
    ProgressDialog dialog;//progressdialog
    String username="";
    TextView notfound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        getSupportActionBar().setTitle("Packages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplication().getSharedPreferences("freelancing", 0);

        username = pref.getString("username", "");

        //progressdialog
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        packagerecyclerview=findViewById(R.id.packagerecyclerview);
        notfound=findViewById(R.id.notfound);
        objPackageList=new ArrayList<>();
        adapter=new PackageAdapter(PackageActivity.this,objPackageList, username);
        packagerecyclerview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        packagerecyclerview.setLayoutManager(linearLayoutManager);
        objPackageList = new ArrayList<Packages>();


        //dialog.show();//progressdialog
        //API
        StringRequest str = new StringRequest(Request.Method.GET, "https://wedlancer.azurewebsites.net/api/Packages", response -> {


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
            Type collectionType = new TypeToken<List<Packages>>() {
            }.getType();
            objPackageList = gson.fromJson(response, collectionType);
            adapter = new PackageAdapter(this, objPackageList,username);
            packagerecyclerview.setAdapter(adapter);


        }, error -> Toast.makeText(PackageActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show());

        //progressdialog
        str.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //progressdialog

        RequestQueue q = Volley.newRequestQueue(PackageActivity.this);
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