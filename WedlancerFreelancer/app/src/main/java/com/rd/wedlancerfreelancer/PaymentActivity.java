package com.rd.wedlancerfreelancer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Button btnconfirm;
    String price, username = "", packagename, paymentId = "Aby965222";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back

        pref = getApplication().getSharedPreferences("freelancing", 0);

        username = pref.getString("username", "");
        packagename = getIntent().getStringExtra("packagename");
        Toast.makeText(this, "" + packagename, Toast.LENGTH_SHORT).show();
        price = getIntent().getStringExtra("price");

        Log.e("TAG", "onCreate:==== " + price);
        btnconfirm = findViewById(R.id.btnconfirm);

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // amount that is entered by user.

                // rounding off the amount.
                int amount = Math.round(Float.parseFloat(price) * 100);

                // initialize Razorpay account.
                Checkout checkout = new Checkout();

                // set your id as below
                checkout.setKeyID("rzp_test_vBdo6hOFkZd4to");

                // set image
                //checkout.setImage(R.drawable.gfgimage);

                // initialize json object
                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", "Order Payment");
                    // put description
                    //object.put("description", "Payment");
                    // to set theme color
                    object.put("theme.color", "#d81b60");
                    // put the currency
                    object.put("currency", "INR");
                    // put amount
                    object.put("amount", amount);
                    // put mobile numbe
                    object.put("prefill.contact", "9825920828");
                    // put email
                    //object.put("prefill.email", "manan.patel4812@gmail.com");
                    // open razorpay to checkout activity
                    checkout.open(PaymentActivity.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void placeorder(String type) {
        //dialog.show();//progressdialog
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("freelancerusername", username);
        postParam.put("paymentId", paymentId);
        postParam.put("amount", price);
        postParam.put("package", packagename);
        //API
        StringRequest str = new StringRequest(Request.Method.POST, "https://wedlancer.azurewebsites.net/api/Orders", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(PlaceOrderActivity.this, "Responce"+response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    if (status.equals("yes")) {
                        Toast.makeText(PaymentActivity.this, "Payment Confirm..!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);


                        startActivity(intent);

                        finish();
                    } else {
                        Toast.makeText(PaymentActivity.this, "Wrong Input!", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaymentActivity.this, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("freelancerusername", username);
                params.put("paymentId", paymentId);
                params.put("amount", price);
                params.put("package", packagename);
                return params;
            }
        };

        RequestQueue q = Volley.newRequestQueue(PaymentActivity.this);
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

    @Override
    public void onPaymentSuccess(String s) {

    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}