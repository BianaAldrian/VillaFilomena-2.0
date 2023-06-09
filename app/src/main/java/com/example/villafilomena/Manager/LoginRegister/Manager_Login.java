package com.example.villafilomena.Manager.LoginRegister;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Manager.Manager_Dashboard;
import com.example.villafilomena.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Objects;

public class Manager_Login extends AppCompatActivity {
    String token;
    String ipAddress;
    TextInputEditText email, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    token = task.getResult();
                });

        /*signUp = findViewById(R.id.manager_login_signUp);*/
        email = findViewById(R.id.manager_login_Email);
        password = findViewById(R.id.manager_login_Password);
        login = findViewById(R.id.manager_login_Login);

        login.setOnClickListener(v -> login());
    }

    private void login(){
        String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/retrieve/manager_login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            switch (response) {
                case "false":
                    Toast.makeText(this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
                    break;
                case "not match":
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    break;
                case "match":
                    updateToken();
                    startActivity(new Intent(this, Manager_Dashboard.class));
                    finish();
                    break;
            }
        },
                Throwable::printStackTrace)
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("email", Objects.requireNonNull(email.getText()).toString());
                map.put("password", Objects.requireNonNull(password.getText()).toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateToken() {
        String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/update/manager_updateToken.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("success")){
                Toast.makeText(this, "Token Updated", Toast.LENGTH_LONG).show();
            } else if(response.equals("failed")){
                Toast.makeText(this, "Token Update Failed", Toast.LENGTH_LONG).show();
            }
        },
                Throwable::printStackTrace)
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("email", Objects.requireNonNull(email.getText()).toString().trim());
                map.put("token",token);

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}