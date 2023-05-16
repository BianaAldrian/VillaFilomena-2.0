package com.example.villafilomena.Manager.LoginRegister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Manager.Manager_Dashboard;
import com.example.villafilomena.R;

import java.util.HashMap;

public class Manager_Login extends AppCompatActivity {
    String ipAddress;
    TextView signUp;
    EditText email, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        signUp = findViewById(R.id.manager_login_signUp);
        email = findViewById(R.id.manager_login_Email);
        password = findViewById(R.id.manager_login_Password);
        login = findViewById(R.id.manager_login_Login);

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, Manager_Register.class));
            finish();
        });

        login.setOnClickListener(v -> {
            login();
        });
    }

    private void login(){
        String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/retrieve/manager_login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("false")){
                Toast.makeText(this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
            }
            else if(response.equals("not match")){
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
            else if(response.equals("match")){
                startActivity(new Intent(this, Manager_Dashboard.class));
            }
        },
                error -> Toast.makeText(this, error.getMessage().toString(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",email.getText().toString());
                map.put("password",password.getText().toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}