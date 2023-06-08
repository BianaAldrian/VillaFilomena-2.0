package com.example.villafilomena.Guest;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Guest_accountPage extends AppCompatActivity {
    String ipAddress;
    TextView name, contact, email, password;
    ImageView eyeOn, eyeOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_account_page);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        name = findViewById(R.id.Guest_fullname);
        contact = findViewById(R.id.Guest_contact);
        email = findViewById(R.id.Guest_email);
        password = findViewById(R.id.Guest_password);
        eyeOn = findViewById(R.id.Guest_eyeOn);
        eyeOff = findViewById(R.id.Guest_eyeOff);

        get_Infos(name, contact, email, password);

        eyeOn.setOnClickListener(v -> {
            eyeOff.setVisibility(View.VISIBLE);
            eyeOn.setVisibility(View.GONE);
            password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        });
        eyeOff.setOnClickListener(v -> {
            eyeOff.setVisibility(View.GONE);
            eyeOn.setVisibility(View.VISIBLE);
            password.setInputType(InputType.TYPE_CLASS_TEXT);
        });
    }

    @SuppressLint("SetTextI18n")
    private void get_Infos(TextView name, TextView contact, TextView email, TextView password) {
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_getGuestInfo.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            name.setText("Full Name: " + jsonObject.getString("fullname"));
                            contact.setText("Contact: " +jsonObject.getString("contact"));
                            email.setText("Email: " +jsonObject.getString("email"));
                            password.setText(jsonObject.getString("password"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", Guest_fragmentsContainer.email);
                return params;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }
}