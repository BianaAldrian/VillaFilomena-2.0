package com.example.villafilomena.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.villafilomena.R;

public class Manager_Dashboard extends AppCompatActivity {
    LinearLayout guestHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        guestHomepage = findViewById(R.id.manager_guestHomepage);

        guestHomepage.setOnClickListener(v -> {

        });
    }
}