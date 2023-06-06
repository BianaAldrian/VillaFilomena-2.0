package com.example.villafilomena.FrontDesk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Guest.Guest_fragmentsContainer;
import com.example.villafilomena.R;

import java.util.HashMap;

public class FrontDesk_Login extends AppCompatActivity {
    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontdesk_login);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");


    }

    private void Login(){
        String url = "http://"+ipAddress+"/VillaFilomena/frontdesk_dir/retrieve/frontdesk_login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("not_exist")){
                Toast.makeText(this, "Username doesn't exist", Toast.LENGTH_LONG).show();

            } else if(response.equals("true")){
                startActivity(new Intent(this, Guest_fragmentsContainer.class));
                //Guest_fragmentsContainer.email = email.getText().toString();

                finish();

            } else if (response.equals("false")) {
                Toast.makeText(this, "Password Incorrect", Toast.LENGTH_LONG).show();
            }
        },
                error -> Toast.makeText(this, error.getMessage().toString(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
               /* map.put("email",email.getText().toString().trim());
                map.put("password",password.getText().toString().trim());*/

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}