package com.example.villafilomena;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class TermsPrivacy extends AppCompatActivity {

    Button cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_privacy);

        cont.setOnClickListener(v -> {
            startActivity(new Intent(this,ContinueAs.class));
            finish();
        });
    }
}