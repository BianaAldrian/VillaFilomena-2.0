package com.example.villafilomena.Manager;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Adapters.Feedbacks_Adapter;
import com.example.villafilomena.Models.Feedbacks_Model;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Manager_RatesFeedbacks extends AppCompatActivity {
    String ipAddress;
    RecyclerView feedbackContainer;
    ArrayList<Feedbacks_Model> feedbacksHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_rates_feedbacks);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        feedbackContainer = findViewById(R.id.manager_feedbackContainer);

        displayFeedbacks();
    }

    private void displayFeedbacks(){
        feedbacksHolder = new ArrayList<>();
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_getFeedbacks.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Feedbacks_Model model = new Feedbacks_Model(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("guest_email"),
                                    jsonObject.getString("ratings"),
                                    jsonObject.getString("feedback"),
                                    jsonObject.getString("image_urls"),
                                    jsonObject.getString("date"));
                            feedbacksHolder.add(model);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    feedbackContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    Feedbacks_Adapter adapter = new Feedbacks_Adapter(this, feedbacksHolder, false);
                    feedbackContainer.setAdapter(adapter);
                },
                Throwable::printStackTrace);
        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }
}