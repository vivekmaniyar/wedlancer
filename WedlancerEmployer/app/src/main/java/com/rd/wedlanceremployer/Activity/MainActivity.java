package com.rd.wedlanceremployer.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnlogin;
    TextView txtregister,txtskipsignin;
    EditText edtusername,edtpassword;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        btnlogin=(Button)findViewById(R.id.btnlogin);
        txtregister=(TextView)findViewById(R.id.txtregister);
//        txtforgotpass=(TextView)findViewById(R.id.txtforgotpass);
        txtskipsignin=(TextView)findViewById(R.id.txtskipsignin);
        edtusername=(EditText)findViewById(R.id.edtusername);
        edtpassword=(EditText)findViewById(R.id.edtpassword);



        pref=getApplication().getSharedPreferences("freelancing",0);
        if(pref.contains("islogin")){

            if(pref.getBoolean("islogin",false)){
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtusername.getText().toString();
                String password = edtpassword.getText().toString();
                if(edtusername.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter Username !", Toast.LENGTH_SHORT).show();
                }
                else if (edtpassword.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter Password !", Toast.LENGTH_SHORT).show();
                }
                else{

                    Map<String,String> postParam=new HashMap<String,String>();
                    postParam.put("username",username);
                    postParam.put("password",password);
                    //API
                    JsonObjectRequest str=new JsonObjectRequest(Request.Method.POST, "https://wedlancer.azurewebsites.net/api/Auth/login",new JSONObject(postParam), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONObject obj=response;

                                    String token = obj.getString("token");
                                    String expiration=obj.getString("expiration");
                                    //String usernamep = obj.getString("usernamep");

                                    SharedPreferences.Editor editor=pref.edit();
                                    editor.putBoolean("islogin",true);
                                    editor.putString("token",token);
                                    editor.putString("expiration",expiration);
                                    editor.putString("username",username);

                                    editor.commit();


                                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Wrong Credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params=new HashMap<>();
                            params.put("username",username);
                            params.put("password",password);
                            return params;
                        }
                    };

                    RequestQueue q= Volley.newRequestQueue(MainActivity.this);
                    q.add(str);
                }
            }
        });
        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
//        txtforgotpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Intent intent=new Intent(MainActivity.this,Forgotpassword.class);
//                //startActivity(intent);
//            }
//        });
        txtskipsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}