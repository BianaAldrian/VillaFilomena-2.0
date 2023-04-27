package com.example.villafilomena.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.villafilomena.R;

public class Manager_GuestHomepage extends AppCompatActivity {
    ImageView edit, save;
    TextView editBanner, editIntro, editVideo, editImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_guest_homepage);

        edit = findViewById(R.id.guestHomepage_edit);
        save = findViewById(R.id.guestHomepage_save);
        editBanner = findViewById(R.id.guestHomepage_editBanner);
        editIntro = findViewById(R.id.guestHomepage_editIntro);
        editVideo = findViewById(R.id.guestHomepage_editVideo);
        editImage = findViewById(R.id.guestHomepage_editImage);

        edit.setOnClickListener(v -> {
            edit.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            editBanner.setVisibility(View.VISIBLE);
            editIntro.setVisibility(View.VISIBLE);
            editVideo.setVisibility(View.VISIBLE);
            editImage.setVisibility(View.VISIBLE);
        });

        save.setOnClickListener(v -> {
            edit.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            editBanner.setVisibility(View.GONE);
            editIntro.setVisibility(View.GONE);
            editVideo.setVisibility(View.GONE);
            editImage.setVisibility(View.GONE);
        });

        editBanner.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_edit_banner_page);

            Button upload = dialog.findViewById(R.id.manager_popupUploadBanner);
            ImageView close = dialog.findViewById(R.id.manager_popupClose);

            upload.setOnClickListener(v1 -> {

            });

            close.setOnClickListener(v1 -> {
                dialog.hide();
            });

            dialog.show();

        });

        editIntro.setOnClickListener(v -> {
        });

        editVideo.setOnClickListener(v -> {
        });

        editImage.setOnClickListener(v -> {
        });
    }
}