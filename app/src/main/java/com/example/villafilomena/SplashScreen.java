package com.example.villafilomena;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Guest.Guest_fragmentsContainer;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //this token is for github account
        //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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String IP = preferences.getString("IP", "").trim();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            //startActivity(new Intent(SplashScreen.this, ContinueAs.class));
            /*if(!IP.equalsIgnoreCase("")){
                String url = "http://"+IP+":8080"+"/VillaFilomena/check_connection.php";

                RequestQueue myRequest = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    if(response.equals("success")){
                        //Toast.makeText(getApplicationContext(), "IP is correct", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashScreen.this, ContinueAs.class));
                        finish();
                    }
                    else if(response.equals("failed")){
                        preferences.edit().clear().commit();
                        startActivity(new Intent(SplashScreen.this, IP_Connect.class));
                        Toast.makeText(getApplicationContext(),"Can't Connect to Server", Toast.LENGTH_LONG).show();
                    }
                },
                        error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show());
                myRequest.add(stringRequest);
            }else{
                startActivity(new Intent(SplashScreen.this, IP_Connect.class));
            }*/
            finish();
        },3000);
    }
}