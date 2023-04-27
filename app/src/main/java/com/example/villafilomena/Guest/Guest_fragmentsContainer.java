package com.example.villafilomena.Guest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.villafilomena.R;
import com.google.android.material.appbar.AppBarLayout;

public class Guest_fragmentsContainer extends AppCompatActivity {
    ImageView banner;
    Button home, book;
    AppBarLayout appbar;
    NestedScrollView nestedScrllView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_fragments_container);

        banner = findViewById(R.id.imgviewBanner);
        home = findViewById(R.id.btnHome);
        book = findViewById(R.id.btnBook);
        appbar = findViewById(R.id.appbar);
        nestedScrllView = findViewById(R.id.nestedScrllView);

        home.setPaintFlags(home.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        home.setPaintFlags(home.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        replace_home(new Guest_homePage());

        home.setOnClickListener(view -> {
            nestedScrllView.fullScroll(View.FOCUS_UP);
            toggle(true);
            nestedScrllView.setNestedScrollingEnabled(true);

            home.setPaintFlags(home.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            home.setPaintFlags(home.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

            if((book.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) == Paint.UNDERLINE_TEXT_FLAG &&
                    (book.getPaintFlags() & Paint.FAKE_BOLD_TEXT_FLAG) == Paint.FAKE_BOLD_TEXT_FLAG){
                book.setPaintFlags(book.getPaintFlags() ^ Paint.UNDERLINE_TEXT_FLAG);
                book.setPaintFlags(book.getPaintFlags() ^ Paint.FAKE_BOLD_TEXT_FLAG);

                replace_home(new Guest_homePage());


            }
        });

        book.setOnClickListener(view -> {
            nestedScrllView.fullScroll(View.FOCUS_UP);
            toggle(false);
            nestedScrllView.setNestedScrollingEnabled(false);

            book.setPaintFlags(book.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            book.setPaintFlags(book.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

            if((home.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) == Paint.UNDERLINE_TEXT_FLAG &&
                    (home.getPaintFlags() & Paint.FAKE_BOLD_TEXT_FLAG) == Paint.FAKE_BOLD_TEXT_FLAG){
                home.setPaintFlags(home.getPaintFlags() ^ Paint.UNDERLINE_TEXT_FLAG);
                home.setPaintFlags(home.getPaintFlags() ^ Paint.FAKE_BOLD_TEXT_FLAG);

                replace_book(new Guest_bookingPage());
            }
        });
    }

    private void replace_home(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.guestFragmentContainer,fragment).commit();
    }
    private void replace_book(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.guestFragmentContainer,fragment).commit();
    }
    private void toggle(boolean show) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        params.height = banner.getHeight();
        appbar.setLayoutParams(params);
        appbar.setExpanded(show);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}