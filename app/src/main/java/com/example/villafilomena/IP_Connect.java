package com.example.villafilomena;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Guest.Guest_fragmentsContainer;

public class IP_Connect extends AppCompatActivity {
    public static EditText IP;
    Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_connect);

        IP = findViewById(R.id.edttxtIP);
        verify = findViewById(R.id.btnVerify);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        verify.setOnClickListener(view -> {
            String url = "http://"+IP.getText().toString()+":8080"+"/VillaFilomena/check_connection.php";

            RequestQueue myRequest = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                if(response.equals("success")){
                    //Toast.makeText(getApplicationContext(), "IP is correct", Toast.LENGTH_SHORT).show();
                    String IP_Address = IP.getText().toString();

                    editor.putString("IP",IP_Address);
                    editor.apply();

                    startActivity(new Intent(IP_Connect.this, ContinueAs.class));

                    finish();
                    //Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_LONG).show();
                }
                else if(response.equals("failed")){
                    Toast.makeText(getApplicationContext(),"Can't Connect to Server", Toast.LENGTH_LONG).show();
                }
            },
                    error -> Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show());
            myRequest.add(stringRequest);
        });

    }

}

