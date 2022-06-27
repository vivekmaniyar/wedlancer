package com.rd.wedlancerfreelancer;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerlayout;//MENU
    SharedPreferences pref;


    LinearLayout lllogin, llreg, llhome, llfreelancer, llmydashboard, llmyportfolio, llpackages, llcontactus, llfaq, llaboutus, lllogout;
    View viewlogin, viewreg, viewhome, viewdashboard, viewlogout;
    ImageView imageupload;
    Button btnsubmit;

    private static final String TAG = "## Upload ##";
    private Uri imagepath;

    String username = "", token;
    EditText videolink;

    public static boolean flag = true;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getApplication().getSharedPreferences("freelancing", 0);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        username = pref.getString("username", "");
        token = pref.getString("token", "");

        lllogin = findViewById(R.id.lllogin);
        llreg = findViewById(R.id.llreg);
        llhome = findViewById(R.id.llhome);
        llfreelancer = findViewById(R.id.llfreelancer);
        llmydashboard = findViewById(R.id.llmydashboard);
        llmyportfolio = findViewById(R.id.llmyportfolio);
        llpackages = findViewById(R.id.llpackages);
        llcontactus = findViewById(R.id.llcontactus);
        llaboutus = findViewById(R.id.llaboutus);
        videolink = findViewById(R.id.videolink);
        llfaq = findViewById(R.id.llfaq);
        lllogout = findViewById(R.id.lllogout);
        viewreg = findViewById(R.id.viewreg);
        viewlogin = findViewById(R.id.viewlogin);
        viewhome = findViewById(R.id.viewhome);
        viewdashboard = findViewById(R.id.viewdashboard);
        viewlogout = findViewById(R.id.viewlogout);
        btnsubmit = findViewById(R.id.btnsubmit);
        imageupload = findViewById(R.id.imageupload);


        drawerlayout = findViewById(R.id.drawerlayout);
        pref = getApplication().getSharedPreferences("freelancing", 0);

        if (pref.getBoolean("islogin", false)) {

            lllogin.setVisibility(View.GONE);
            llreg.setVisibility(View.GONE);
            lllogout.setVisibility(View.VISIBLE);
            llmydashboard.setVisibility(View.VISIBLE);
            viewreg.setVisibility(View.GONE);
            viewlogout.setVisibility(View.VISIBLE);
            viewlogin.setVisibility(View.GONE);
            viewhome.setVisibility(View.GONE);
            viewdashboard.setVisibility(View.VISIBLE);

        } else {
            lllogin.setVisibility(View.VISIBLE);
            llreg.setVisibility(View.VISIBLE);
            lllogout.setVisibility(View.GONE);
            llmydashboard.setVisibility(View.GONE);
            viewreg.setVisibility(View.VISIBLE);
            viewlogout.setVisibility(View.GONE);
            viewlogin.setVisibility(View.VISIBLE);
            viewhome.setVisibility(View.VISIBLE);
            viewdashboard.setVisibility(View.GONE);
        }


        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);//Menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back


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
        llmyportfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PortfolioActivity.class);
                startActivity(intent);
            }
        });
        llpackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PackageActivity.class);
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

        //ImageUpload
        initconfig();

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestpermission();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick:12 " + imagepath);

                if (videolink.getText().toString().isEmpty() || videolink.getText().toString() == null) {
                    videolink.setError("Enter Valid URL.");
                } else {
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
                            postParam.put("video", videolink.getText().toString());
                            //API
                            JsonObjectRequest str = new JsonObjectRequest(Request.Method.POST, "https://wedlancer.azurewebsites.net/api/Portfolios", new JSONObject(postParam), new Response.Listener<JSONObject>() {

                                public void onResponse(JSONObject response) {
                                    //dialog.dismiss();//progressdialog

                                    JSONObject obj = response;

                                    Toast.makeText(HomeActivity.this, "Upload Successfull!", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//                                startActivity(intent);
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    videolink.setText("");
                                    imageupload.setImageResource(R.drawable.ic_image_upload);

                                }
                            }, error -> Toast.makeText(HomeActivity.this, "Something went wrong" + error.getMessage(), Toast.LENGTH_SHORT).show()) {
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
                                    params.put("image", String.valueOf(imagepath));
                                    params.put("video", videolink.getText().toString());
                                    return params;
                                }
                            };

//                        //progressdialog
//                        str.setRetryPolicy(new DefaultRetryPolicy(
//                                10000,
//                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                        //progressdialog

                            RequestQueue q = Volley.newRequestQueue(HomeActivity.this);
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
                }

            }
        });
        //ImageUpload


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
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{
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
                        imageupload.setImageBitmap(selectedimagebitmap);
                    }
                }
            }
    );
    //IMG_UPLOAD_Methods


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


        if (id == R.id.badge) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
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