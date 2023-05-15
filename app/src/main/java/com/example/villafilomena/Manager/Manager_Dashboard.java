package com.example.villafilomena.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.villafilomena.R;

public class Manager_Dashboard extends AppCompatActivity {
    LinearLayout guestHomepage, roomCottage_details, employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        guestHomepage = findViewById(R.id.manager_guestHomepage);
        roomCottage_details = findViewById(R.id.manager_RoomCottage_details);
        employee = findViewById(R.id.manager_Employee);

        guestHomepage.setOnClickListener(v -> {
            startActivity(new Intent(this, Manager_GuestHomepage.class));
        });
        roomCottage_details.setOnClickListener(v -> {
            startActivity(new Intent(this, Manager_room_cottage_details.class));
        });
        employee.setOnClickListener(v -> {
            startActivity(new Intent(this, Manager_FrontdeskUser.class));
        });

    }
}