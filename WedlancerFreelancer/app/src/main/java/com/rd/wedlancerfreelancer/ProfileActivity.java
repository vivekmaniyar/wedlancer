package com.rd.wedlancerfreelancer;

import static com.rd.wedlancerfreelancer.HomeActivity.flag;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.wedlancerfreelancer.Models.Freelancers;
import com.rd.wedlancerfreelancer.Utils.Config;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView edtname,edtphonenumber, edtemail;
    Button btnupdate;
    Freelancers freelancerlist;
    SharedPreferences pref;

    private static final String TAG = "## Upload ##";
    private Uri imagepath;

    ProgressDialog dialog;//progressdialog
    //SharedPreferences pref;
    String username = "", phno, fn, ln, token;
    BigInteger ph;
    CircleImageView profileimage;


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pref=getApplication().getSharedPreferences("freelancing",0);
        pref = getApplication().getSharedPreferences("freelancing", 0);
        username = pref.getString("username", "");
        token = pref.getString("token", "");
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        initconfig();
        edtname = findViewById(R.id.edtname);
        edtphonenumber = findViewById(R.id.edtphonenumber);
        profileimage = findViewById(R.id.profileimage);
        edtemail = findViewById(R.id.edtemail);
        btnupdate = findViewById(R.id.btnupdate);
        //edtpassword = findViewById(R.id.edtpassword);

        //progressdialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        //progressdialog

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageUpload

                requestpermission();
//                                                imageupload.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//
//                                                    }
//                                                });
            }
        });


        //

        //


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

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //

                String email = edtemail.getText().toString();
                String phoneNumber = edtphonenumber.getText().toString();


//                dialog.show();//progressdialog

                Log.e(TAG, "onClick:12 " + imagepath);


                if (imagepath != null) {
                    MediaManager.get().upload(imagepath).callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            Log.d(TAG, "onstart : " + "started");
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            Log.d(TAG, "onProgress: " + "uploading...");
                            progressDialog.show();
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            Log.d(TAG, "-----UPLOAD SUCCESS-----");

                            Log.d(TAG, "Version: " + "v" + resultData.get("version"));
                            Log.d(TAG, "File name: " + resultData.get("public_id") + "." + resultData.get("format"));
                            Log.d(TAG, "Image Link for API: " + "v" + resultData.get("version") + "/" + resultData.get("public_id") + "." + resultData.get("format"));
                            Log.d(TAG, "URL: " + resultData.get("secure_url"));


                            Map<String, String> postParam = new HashMap<String, String>();
                            postParam.put("username", username);
                            postParam.put("image", String.valueOf(resultData.get("secure_url")));
                            postParam.put("email", email);
                            postParam.put("phoneNumber", phoneNumber);
                            // postParam.put("video", videolink.getText().toString());
                            //API
                            JsonObjectRequest str = new JsonObjectRequest(Request.Method.PUT, "https://wedlancer.azurewebsites.net/api/Profiles/updateprofile?username=" + username, new JSONObject(postParam), new Response.Listener<JSONObject>() {

                                public void onResponse(JSONObject response) {
                                    //dialog.dismiss();//progressdialog

                                    JSONObject obj = response;

                                    Toast.makeText(ProfileActivity.this, "Update Successfull!", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//                                startActivity(intent);
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

//                                            videolink.setText("");
//                                           profileimage.setImageResource(R.drawable.ic_image_upload);

                                }
                            }, error -> Toast.makeText(ProfileActivity.this, "Something went wrong" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
                                //This is for Headers If You Needed
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    // params.put("Content-Type", "application/json; charset=UTF-8");
//                        params.put("token", token);
                                    params.put("Authorization", "Bearer " + Config.token);
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    return params;
                                }

                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("username", username);
                                    params.put("image", String.valueOf(imagepath));
                                    params.put("email", email);
                                    params.put("phoneNumber", phoneNumber);
                                    //params.put("video", videolink.getText().toString());
                                    return params;
                                }
                            };

//                        //progressdialog
//                        str.setRetryPolicy(new DefaultRetryPolicy(
//                                10000,
//                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                        //progressdialog

                            RequestQueue q = Volley.newRequestQueue(ProfileActivity.this);
                            q.add(str);
                            //API
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Log.d(TAG, "onError: " + error.getDescription());
                            Log.d(TAG, "onError: " + error.getCode());
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Log.d(TAG, " onReschedule: " + error);
                        }
                    }).dispatch();
                } else {

                    progressDialog.show();
                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("username", username);
//                    postParam.put("image", String.valueOf(resultData.get("secure_url")));
                    postParam.put("email", email);
                    postParam.put("phoneNumber", phoneNumber);
                    // postParam.put("video", videolink.getText().toString());
                    //API
                    JsonObjectRequest str = new JsonObjectRequest(Request.Method.PUT, "https://wedlancer.azurewebsites.net/api/Profiles/updateprofile?username=" + username, new JSONObject(postParam), new Response.Listener<JSONObject>() {

                        public void onResponse(JSONObject response) {
                            //dialog.dismiss();//progressdialog

                            JSONObject obj = response;

                            Toast.makeText(ProfileActivity.this, "Upload Successfull!", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//                                startActivity(intent);
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

//                                            videolink.setText("");
//                                           profileimage.setImageResource(R.drawable.ic_image_upload);

                        }
                    }, error -> Toast.makeText(ProfileActivity.this, "Something went wrong" + error.getMessage(), Toast.LENGTH_SHORT).show()) {
                        //This is for Headers If You Needed
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            // params.put("Content-Type", "application/json; charset=UTF-8");
//                        params.put("token", token);
                            params.put("Authorization", "Bearer " + token);
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            return params;
                        }

                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", username);
//                            params.put("image", String.valueOf(imagepath));
                            params.put("email", email);
                            params.put("phoneNumber", phoneNumber);
                            //params.put("video", videolink.getText().toString());
                            return params;
                        }
                    };

//                        //progressdialog
//                        str.setRetryPolicy(new DefaultRetryPolicy(
//                                10000,
//                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                        //progressdialog

                    RequestQueue q = Volley.newRequestQueue(ProfileActivity.this);
                    q.add(str);
                }


            }
        });


    }

    //IMG_UPLOAD_Methods
    public void initconfig() {
        if (flag) {
            Map config = new HashMap();
            config.put("cloud_name", "dvml1uyhb");
            config.put("api_key", "284174922512743");
            config.put("api_secret", "IeFVQw8esemzTp3nAuznReHmtzM");
            MediaManager.init(this, config);
            flag = false;
        }

    }

    private void requestpermission() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launchsomeactivity.launch(intent);
    }

    ActivityResultLauncher<Intent> launchsomeactivity
            = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imagepath = data.getData();
                        Bitmap selectedimagebitmap = null;
                        try {
                            selectedimagebitmap = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(), imagepath
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        profileimage.setImageBitmap(selectedimagebitmap);
                    }
                }
            }
    );
    //IMG_UPLOAD_Methods

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}