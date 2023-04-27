package com.example.villafilomena.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.villafilomena.R;

public class Manager_GuestHomepage extends AppCompatActivity {
    ImageView edit;
    TextView editBanner, editIntro, editVideo, editImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_guest_homepage);

        edit = findViewById(R.id.guestHomepage_edit);
        editBanner = findViewById(R.id.guestHomepage_editBanner);
        editIntro = findViewById(R.id.guestHomepage_editIntro);
        editVideo = findViewById(R.id.guestHomepage_editVideo);
        editImage = findViewById(R.id.guestHomepage_editImage);

        edit.setOnClickListener(v -> {
        });

        editBanner.setOnClickListener(v -> {
        });

        editIntro.setOnClickListener(v -> {
        });

        editVideo.setOnClickListener(v -> {
        });

        editImage.setOnClickListener(v -> {
        });
    }
}